package cc.colorcat.toolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cxx on 17-6-22.
 * xx.ch@outlook.com
 */
public class ImageManager {
    private File saveDir;
    private Downloader downloader;
    private Indexer indexer;
    private Resources resources;
    private float density;

    private ImageManager(Builder builder) {
        this.saveDir = builder.saveDir;
        this.downloader = builder.downloader;
        this.indexer = builder.indexer;
        this.resources = builder.resources;
        this.density = builder.density;
    }

    @Nullable
    public Drawable getSelector(String selectedName, String unselectedName, int widthDp, int heightDp) {
        return getSelector(selectedName, unselectedName, widthDp * density, heightDp * density);
    }

    @Nullable
    public Drawable getSelector(String selectedName, String unselectedName, float widthPx, float heightPx) {
        int[][] states = {{android.R.attr.state_selected}, {-android.R.attr.state_selected}};
        String[] names = {selectedName, unselectedName};
        return getStatefulDrawable(states, names, widthPx, heightPx);
    }

    @Nullable
    public Drawable getStatefulDrawable(int[][] states, String[] names, int widthDp, int heightDp) {
        return getStatefulDrawable(states, names, widthDp * density, heightDp * density);
    }

    @Nullable
    public Drawable getStatefulDrawable(int[][] states, String[] names, float widthPx, float heightPx) {
        if (states.length != names.length) {
            throw new IllegalArgumentException("states.length != names.length");
        }
        if (widthPx < 1.0F || heightPx < 1.0F) {
            throw new IllegalArgumentException("widthPx and heightPx must be not less than 1.0");
        }
        StateListDrawable result = new StateListDrawable();
        for (int size = states.length, i = 0; i < size; ++i) {
            Bitmap bitmap = getBitmap(names[i], widthPx, heightPx);
            if (bitmap == null) return null;
            result.addState(states[i], new BitmapDrawable(resources, bitmap));
        }
        return result;
    }

    @Nullable
    public Bitmap getBitmap(String name, int widthDp, int heightDp) {
        return getBitmap(name, widthDp * density, heightDp * density);
    }

    @Nullable
    public Bitmap getBitmap(String name, float widthPx, float heightPx) {
        Bitmap bitmap = getBitmap(name);
        if (bitmap != null) {
            bitmap = uniformScale(bitmap, widthPx, heightPx);
        }
        return bitmap;
    }


    @Nullable
    public Bitmap getBitmap(String name) {
        File path = new File(saveDir, md5(name));
        return BitmapFactory.decodeFile(path.getAbsolutePath());
    }

    public void saveImage(final String name, final String url) {
        if (Utils.isHttpUrl(url)) {
            String md5Name = md5(name);
            if (!indexer.exists(md5Name, url)) {
                download(md5Name, url);
            }
        }
    }

    public void clear() {
        clearFile(saveDir);
        indexer.clear();
    }

    public long cacheSize() {
        return size(saveDir);
    }

    private void download(final String md5Name, final String url) {
        final File downloadPath = new File(saveDir, "download_" + md5Name);
        downloader.clone().from(url).to(downloadPath).go(new Downloader.Listener() {
            @Override
            public void onSuccess(String url, File savePath) {
                L.i("download image success, url = " + url);
                File realPath = new File(saveDir, md5Name);
                if (!realPath.exists() || realPath.delete()) {
                    if (savePath.exists() && savePath.renameTo(realPath)) {
                        indexer.save(md5Name, url);
                    }
                }
            }

            @Override
            public void onFailure(String url) {
                L.i("download image failure, url = " + url);
                downloadPath.deleteOnExit();
            }
        });
    }

    public Builder newBuilder() {
        return new Builder(this);
    }


    public static class Builder {
        private File saveDir;
        private Downloader downloader;
        private Indexer indexer;
        private Resources resources;
        private float density;

        public Builder(Context context) {
            if (context == null) throw new NullPointerException("context == null");
            this.saveDir = createImageDir(context);
            this.downloader = new DefaultDownloader();
            this.indexer = new DefaultIndexer(context);
            this.resources = context.getResources();
            this.density = this.resources.getDisplayMetrics().density;
        }

        private Builder(ImageManager manager) {
            this.saveDir = manager.saveDir;
            this.downloader = manager.downloader;
            this.indexer = manager.indexer;
            this.resources = manager.resources;
            this.density = manager.density;
        }

        public Builder saveDir(File saveDir) {
            if (saveDir == null || !saveDir.exists()) {
                throw new IllegalArgumentException("saveDir not exists.");
            }
            this.saveDir = saveDir;
            return this;
        }

        public Builder downloader(Downloader downloader) {
            if (downloader == null) throw new NullPointerException("downloader == null");
            this.downloader = downloader;
            return this;
        }

        public Builder indexer(Indexer indexer) {
            if (indexer == null) throw new NullPointerException("indexer == null");
            this.indexer = indexer;
            return this;
        }

        public ImageManager build() {
            return new ImageManager(this);
        }
    }


    public interface Downloader extends Cloneable {

        Downloader from(String url);

        Downloader to(File path);

        void go(Listener listener);

        Downloader clone();

        interface Listener {

            void onSuccess(String url, File savePath);

            void onFailure(String url);
        }
    }


    public interface Indexer {

        boolean exists(String name, String url);

        void save(String name, String url);

        void remove(String name);

        void clear();
    }


    private static class DefaultIndexer implements Indexer {
        private static final String DATA = "image_data";
        private Context context;

        private DefaultIndexer(Context context) {
            this.context = context;
        }

        @Override
        public boolean exists(String name, String url) {
            return read(name).equals(url);
        }

        @Override
        public void save(String name, String url) {
            SharedPreferences.Editor editor = sp().edit();
            editor.putString(name, url);
            editor.apply();
        }

        @Override
        public void remove(String name) {
            SharedPreferences.Editor editor = sp().edit();
            editor.remove(name);
            editor.apply();
        }

        @Override
        public void clear() {
            SharedPreferences.Editor editor = sp().edit();
            editor.clear();
            editor.apply();
        }

        private String read(String name) {
            return sp().getString(name, "");
        }

        private SharedPreferences sp() {
            return context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        }
    }


    private static class DefaultDownloader implements Downloader {
        private String url;
        private File savePath;

        private DefaultDownloader() {
        }

        @Override
        public Downloader from(String url) {
            this.url = url;
            return this;
        }

        @Override
        public Downloader to(File path) {
            this.savePath = path;
            return this;
        }

        @Override
        public void go(final Listener listener) {
//            ApiService.download(url, savePath, new MiListener<File>() {
//                @Override
//                public void onSuccess(File result) {
//                    if (listener != null) {
//                        listener.onSuccess(url, savePath);
//                    }
//                }
//
//                @Override
//                public void onFailure(int code, String msg) {
//                    if (listener != null) {
//                        listener.onFailure(url);
//                    }
//                }
//            });
        }

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public Downloader clone() {
            return new DefaultDownloader();
        }
    }


    private static File createImageDir(Context ctx) {
        File cache = ctx.getExternalCacheDir();
        if (cache == null) cache = ctx.getCacheDir();
        File image = new File(cache, "image");
        if (image.exists() || image.mkdirs()) return image;
        return cache;
    }

    private static void clearFile(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                } else {
                    clearFile(file);
                }
            }
        }
    }

    private static long size(File file) {
        if (file.isFile()) return file.length();
        long size = 0L;
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                if (child.isFile()) {
                    size += child.length();
                } else {
                    size += size(child);
                }
            }
        }
        return size;
    }

    private static String md5(@NonNull String resource) {
        String result = resource;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(resource.getBytes());
            byte[] bytes = digest.digest();
            int len = bytes.length << 1;
            StringBuilder sb = new StringBuilder(len);
            for (byte b : bytes) {
                sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(b & 0x0f, 16));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return result;
    }

    private static Bitmap uniformScale(final Bitmap src, final float widthPx, final float heightPx) {
        final int oldWidth = src.getWidth(), oldHeight = src.getHeight();
        // calculate the scale
        final float scale = Math.min(widthPx / oldWidth, heightPx / oldHeight);
        // create a matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, oldWidth, oldHeight, matrix, true);
    }
}
