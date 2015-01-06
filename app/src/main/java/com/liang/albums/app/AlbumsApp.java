package com.liang.albums.app;

import android.app.Application;
import android.content.Context;

import com.liang.albums.util.PreferenceUtil;
import com.liang.albums.util.oauth.ResponseListener;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Created by liang on 15/1/3.
 */
public class AlbumsApp extends Application{

    private static AlbumsApp mApplication;

    private SocialAuthAdapter authAdapter;
    private PreferenceUtil preferenceUtil;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        SocialAuthAdapter.Provider.INSTAGRAM.setCallBackUri("http://test.com");
    }
}
