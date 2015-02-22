package com.liang.albums.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;
import com.liang.albums.util.WifiUtil;

import org.brickred.socialauth.android.SocialAuthAdapter;

import java.util.List;

/**
 * Created by liang on 15/1/8.
 * Splash activity
 *
 * Open Wifi Connection
 *
 */
public class SplashActivity extends FragmentActivity {

    private final String TAG = "SplashActivity";

    private WifiUtil mWifiUtil;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2s 闪屏
        setContentView(R.layout.activity_splash);
        // open wifi module
        mWifiUtil = AlbumsApp.getInstance().getWifiUtil();
        if(!mWifiUtil.getWifiManager().isWifiEnabled()){
            mWifiUtil.getWifiManager().setWifiEnabled(true);
        }
        // start activity after 2s
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
