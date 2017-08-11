package cc.colorcat.demo;

import android.app.Application;

import com.squareup.picasso.Picasso;

import cc.colorcat.util.L;
import cc.colorcat.vangogh.VanGogh;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        L.init(BuildConfig.DEBUG);
        VanGogh vanGogh = new VanGogh.Builder(this)
                .enableLog(true)
                .debug(true)
                .defaultLoading(R.drawable.ic_cloud_download_black_24dp)
                .defaultError(R.drawable.ic_error_black_24dp)
                .build();
        VanGogh.setSingleton(vanGogh);

        Picasso picasso = new Picasso.Builder(this)
                .loggingEnabled(true)
                .build();
        Picasso.setSingletonInstance(picasso);
    }
}
