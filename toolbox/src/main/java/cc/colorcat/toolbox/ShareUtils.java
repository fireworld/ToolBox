package cc.colorcat.toolbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 17-8-15.
 * xx.ch@outlook.com
 */
public abstract class ShareUtils {
    private Activity mActivity;

    private PopupWindow mShareWindow;

    private Handler mHandler;
    private WindowManager.LayoutParams mWindowAttr;

    private void initFadeAnimation() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (mWindowAttr == null) {
            mWindowAttr = mActivity.getWindow().getAttributes();
        }
    }

    // 弹出分享窗口时，使背景变暗
    private void fadeIn(@IntegerRes int durationResId, final float destAlpha, final float offset) {
        initFadeAnimation();
        int duration = mActivity.getResources().getInteger(durationResId);
        final int interval = calculateInterval(duration, destAlpha, offset);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mWindowAttr.alpha <= destAlpha) return;
                mWindowAttr.alpha -= offset;
                if (mWindowAttr.alpha < destAlpha) mWindowAttr.alpha = destAlpha;
                mActivity.getWindow().setAttributes(mWindowAttr);
                mHandler.postDelayed(this, interval);
            }
        });
    }

    // 分享窗口消失时，使背景恢复原状
    private void fadeOut(@IntegerRes int durationResId, final float offset) {
        initFadeAnimation();
        int duration = mActivity.getResources().getInteger(durationResId);
        final int interval = calculateInterval(duration, mWindowAttr.alpha, offset);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mWindowAttr.alpha >= 1.0F) return;
                mWindowAttr.alpha += offset;
                if (mWindowAttr.alpha > 1.0F) mWindowAttr.alpha = 1.0F;
                mActivity.getWindow().setAttributes(mWindowAttr);
                mHandler.postDelayed(this, interval);
            }
        });
    }

    /**
     * 计算使背景逐渐变暗或变亮每两次改变之间的时间间隔
     *
     * @param duration  变暗或变亮整个过程持续的时间
     * @param destAlpha 变暗时最终背景的 alpha 值，取值范围 (0.0F, 1.0F)
     * @param offset    背景逐渐变暗或变亮，alpha 值每次减少或增加的值，取值范围 (0.0F, 1.0F)
     * @return 背景逐渐变暗或变亮，每两次改变之间的时间间隔
     */
    private int calculateInterval(int duration, float destAlpha, float offset) {
        int count = (int) ((1.0F - destAlpha) / offset);
        return duration / count;
    }

    public void showShareWindow() {
        if (mShareWindow == null) {
            @SuppressLint("InflateParams")
            View root = mActivity.getLayoutInflater().inflate(R.layout.layout_share, null);
            root.findViewById(R.id.btn_qq).setOnClickListener(mClick);
            root.findViewById(R.id.btn_we_chat).setOnClickListener(mClick);
            root.findViewById(R.id.btn_wei_bo).setOnClickListener(mClick);
            root.findViewById(R.id.btn_circle_of_friends).setOnClickListener(mClick);
            mShareWindow = new PopupWindow(root,
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            mShareWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mShareWindow.setOutsideTouchable(true);
            mShareWindow.setAnimationStyle(R.style.PopupWindowBottom);
            mShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    fadeOut(android.R.integer.config_shortAnimTime, 0.02F);
                }
            });
        }
        fadeIn(android.R.integer.config_shortAnimTime, 0.5F, 0.02F);
        mShareWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    // 库当中 R 文件定义的不是 final 常量，故不能使用 switch
    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btn_qq) {
//                mPresenter.toShareWithQq();
            } else if (i == R.id.btn_we_chat) {
//                mPresenter.toShareWithWeChat();
            } else if (i == R.id.btn_wei_bo) {
//                mPresenter.toShareWithWeiBo();
            } else if (i == R.id.btn_circle_of_friends) {
//                mPresenter.toShareWithCircleOfFriends();
            }
        }
    };

    private List<ComponentName> mQqComponents;

    private void initQqComponents() {
        if (mQqComponents == null) {
            mQqComponents = new ArrayList<>(4);
            mQqComponents.add(new ComponentName("com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity"));     // qq 普通版
            mQqComponents.add(new ComponentName("com.tencent.qqlite",
                    "com.tencent.mobileqq.activity.JumpActivity"));     // qq 轻聊版
            mQqComponents.add(new ComponentName("com.tencent.mobileqqi",
                    "com.tencent.mobileqq.activity.JumpActivity"));     // qq 国际版
            mQqComponents.add(new ComponentName("com.tencent.tim",
                    "com.tencent.mobileqq.activity.JumpActivity"));     // tim
        }
    }

    public boolean shareWithQq(ShareInfo info) {
        initQqComponents();
        Intent intent = getShareIntent(info.formatToPlain(), null, "text/plain");
        for (int i = 0, size = mQqComponents.size(); i < size; ++i) {
            intent.setComponent(mQqComponents.get(i));
            if (tryStart(intent)) return true;
        }
        return false;
    }

    public boolean shareWithWeChat(ShareInfo info) {
        Intent intent = getShareIntent(info.formatToHtml(), null, "text/plain");
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        return tryStart(intent);
    }

    public boolean shareWithCircleOfFriends(ShareInfo info) {
//        Intent i = getShareIntent(null, ClientHelper.icLauncher(), "image/*");
        Intent i = getShareIntent(null, getIcon(), "image/*");
        i.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        i.putExtra("Kdescription", info.formatToPlain());
        return tryStart(i);
    }

    private List<ComponentName> mWeiBoComponents;

    private void initWeiBoComponents() {
        if (mWeiBoComponents == null) {
            mWeiBoComponents = new ArrayList<>(4);
            mWeiBoComponents.add(new ComponentName("com.sina.weibo",
                    "com.sina.weibo.composerinde.ComposerDispatchActivity")); // 微博普通版
            mWeiBoComponents.add(new ComponentName("com.weico.international",
                    "com.weico.international.activity.compose.SeaComposeActivity")); // 微博国际版
            mWeiBoComponents.add(new ComponentName("com.sina.weibog3",
                    "com.sina.weibo.composerinde.ComposerDispatchActivity")); // 微博 4g 版
            mWeiBoComponents.add(new ComponentName("com.sina.weibolite",
                    "com.sina.weibo.ComposerDispatchActivity")); // 微博轻版
        }
    }

    public boolean shareWithWeiBo(ShareInfo info) {
        initWeiBoComponents();
        Intent intent = getShareIntent(info.formatToPlain(), getIcon(), "image/*");
        for (int i = 0, size = mWeiBoComponents.size(); i < size; ++i) {
            intent.setComponent(mWeiBoComponents.get(i));
            if (tryStart(intent)) return true;
        }
        return false;
    }

    private Intent getShareIntent(String content, Uri uri, @NonNull String mimeType) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setType(mimeType);
        if (content != null) {
            i.putExtra(Intent.EXTRA_TEXT, content);
        }
        if (uri != null) {
            i.putExtra(Intent.EXTRA_STREAM, uri);
        }
        return i;
    }

    /**
     * Intent 设置过 Component 后调用 {@link Intent#resolveActivity(PackageManager)}
     * 会直接返回设置的 {@link ComponentName}, 故无法以此判断是否能够启动 {@link android.app.Activity}
     *
     * @param i 尝试启动一个 app 的 {@link Intent}
     * @return 启动成功返回 true, 否则返回 false
     */
    private boolean tryStart(Intent i) {
        try {
            mActivity.startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract Uri getIcon();

    public static class ShareInfo {
        public final String title;
        public final String msg;
        public final String url;

        public ShareInfo(String title, String msg, String url) {
            this.title = title;
            this.msg = msg;
            this.url = url;
        }

        public String formatToPlain() {
            return title + "\n" + msg + ": " + url;
        }

        public String formatToHtml() {
            return title + "\n<a href=\"" + url + "\">" + msg + "</a>";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ShareInfo shareInfo = (ShareInfo) o;

            if (title != null ? !title.equals(shareInfo.title) : shareInfo.title != null) return false;
            if (msg != null ? !msg.equals(shareInfo.msg) : shareInfo.msg != null) return false;
            return url != null ? url.equals(shareInfo.url) : shareInfo.url == null;

        }

        @Override
        public int hashCode() {
            int result = title != null ? title.hashCode() : 0;
            result = 31 * result + (msg != null ? msg.hashCode() : 0);
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ShareInfo{" +
                    "title='" + title + '\'' +
                    ", msg='" + msg + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
