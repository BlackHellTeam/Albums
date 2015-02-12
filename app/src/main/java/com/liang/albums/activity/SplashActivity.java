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
 * 1、Open Wifi Connection
 * 2、Get all accounts, then login them
 *
 */
public class SplashActivity extends FragmentActivity implements SocialEventsHandler {

    private final String TAG = "SplashActivity";

    private List<WifiConfiguration> mWifiCfgs;
//    private ScanResult mScanResult;
    private List<ScanResult> mListResult;
    private WifiUtil mWifiUtil;
    private SocialAuthAdapter mAuthAdapter;

    private Handler mHandler;

    private Integer mLoginCounter = new Integer(0);
    private boolean mLoginStates = false;

    private SocialAccountsReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mWifiUtil = AlbumsApp.getInstance().getWifiUtil();
        mAuthAdapter = AlbumsApp.getInstance().getAuthInstagramAdapter();

        mReceiver = new SocialAccountsReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        registerReceiver(mReceiver, filter);

        //mHandler = new Handler();

        //mHandler.post(rInitApp);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    Runnable rInitApp = new Runnable() {
        @Override
        public void run() {
            // find wifi results and connect the known one
            if(!mWifiUtil.getWifiManager().isWifiEnabled()){
                mWifiUtil.getWifiManager().setWifiEnabled(true);
            }

            // wait for wifi enabled
            while (mWifiUtil.getWifiManager().getWifiState() != WifiManager.WIFI_STATE_ENABLED);

            connectKnownWifi();

//            connectSocialAccounts();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private boolean connectKnownWifi(){
        // scan for wifi
        mListResult = mWifiUtil.getScanResults();
        // then connect the known wifi
        mWifiCfgs = mWifiUtil.getConfiguredConnections();

        return  true;
    }

    private void connectSocialAccounts(){

        boolean hasInsAccount = AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
        boolean hasFBAccount = AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_FACEBOOK, false);
        boolean hasFlickerAccount = AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_FLICKER, false);

        if( !hasFBAccount && !hasInsAccount && !hasFlickerAccount ){
            Log.d(TAG, "startActivity : GuideActivity");
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        }
        Log.d(TAG, "startActivity : MainActivity");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {
        // one account login success the goto the albums show
        // CounterSub();
        synchronized (mLoginCounter){
            mLoginCounter--;
        }

        if(state == Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS){
            mLoginStates = mLoginStates|true;
        }else{
            mLoginStates = mLoginStates|false;
        }

        if(mLoginCounter == 0 && mLoginStates){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onSignOut(String account) {

    }

    @Override
    public void onContentListChanged(String account) {

    }
//
//    private synchronized void CounterAdd(){
//        mLoginCounter++;
//        Log.d(TAG, "ADD : Login Counter = " + mLoginCounter);
//    }
//
//    private synchronized void CounterSub(){
//        mLoginCounter--;
//        Log.d(TAG, "SUB : Login Counter = " + mLoginCounter);
//    }
//
//    private synchronized int getLoginCounter(){
//        Log.d(TAG, "GET : Login Counter = " + mLoginCounter);
//        return mLoginCounter;
//    }
}
