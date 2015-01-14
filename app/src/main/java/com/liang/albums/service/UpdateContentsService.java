package com.liang.albums.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;

/**
 * Created by liang on 15/1/13.
 * 用于维护离线图片库
 */
public class UpdateContentsService extends Service implements SocialEventsHandler{

    private SocialAccountsReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new SocialAccountsReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {

    }

    @Override
    public void onSignOut(String account) {

    }

    @Override
    public void onContentListChanged(String account) {

    }
}
