package com.liang.albums.app;

import android.app.AlarmManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.liang.albums.service.UpdateContentsService;
import com.liang.albums.util.Constants;
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
    private static final String TAG = "AlbumsApp";

    private static AlbumsApp mApplication;

    private SocialAuthAdapter mAuthInstagramAdapter;
    private PreferenceUtil preferenceUtil;
    private WifiUtil wifiUtil;
    private UpdateContentsService mContentService; // 后续对service的轮询时间进行设置
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mContentService = ((UpdateContentsService.UpdateContentsServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mContentService = null;
        }
    };

    private void bindMutiService(){
        Intent intent = new Intent(this, UpdateContentsService.class);
//        startService(intent);
        bindService(intent,mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unbindMutiService(){
        try {
            unbindService(mServiceConnection);
        }catch (IllegalArgumentException e){
            Log.d(TAG, "Exception : Service wasn't bind!");
        }
    }

    public synchronized UpdateContentsService getContentService(){
        return mContentService;
    }

    public synchronized static AlbumsApp getInstance(){
        return mApplication;
    }

    public synchronized SocialAuthAdapter initAuthAdapter(Context ctx){
        if (mAuthInstagramAdapter == null){
            mAuthInstagramAdapter = new SocialAuthAdapter(new ResponseListener(ctx, Constants.SocialInfo.ACCOUNT_INSTAGRAM));
        }
        return mAuthInstagramAdapter;
    }

    public synchronized SocialAuthAdapter getAuthInstagramAdapter(){
        return mAuthInstagramAdapter;
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
        mAuthInstagramAdapter = initAuthAdapter(this);
        initImageLoader(getApplicationContext());

        bindMutiService();
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
