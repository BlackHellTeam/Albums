package com.liang.albums.app;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.liang.albums.util.PreferenceUtil;
import com.liang.albums.util.WifiUtil;
import com.liang.albums.util.oauth.ResponseListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Created by liang on 15/1/3.
 */
public class AlbumsApp extends Application{

    private static AlbumsApp mApplication;

    private SocialAuthAdapter authAdapter;
    private PreferenceUtil preferenceUtil;
    private WifiUtil wifiUtil;

    public synchronized static AlbumsApp getInstance(){
        return mApplication;
    }

    public synchronized SocialAuthAdapter initAuthAdapter(Context ctx){
        if (authAdapter == null){
            authAdapter = new SocialAuthAdapter(new ResponseListener(ctx));
        }
        return authAdapter;
    }

    public synchronized SocialAuthAdapter getAuthAdapter(){
        return authAdapter;
    }

    public synchronized PreferenceUtil getPreferenceUtil(){
        if (preferenceUtil == null){
            preferenceUtil = new PreferenceUtil(this.getApplicationContext());
        }
        return preferenceUtil;
    }

    public synchronized WifiUtil getWifiUtil(){
        return wifiUtil;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        wifiUtil = new WifiUtil(this);
        SocialAuthAdapter.Provider.INSTAGRAM.setCallBackUri("http://test.com");
        authAdapter = initAuthAdapter(this);
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
