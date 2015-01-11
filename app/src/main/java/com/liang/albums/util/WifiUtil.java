package com.liang.albums.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by liang on 15/1/8.
 */
public class WifiUtil {

    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private Context mContext;

    public WifiUtil(Context ctx){
        mContext = ctx;
        initWifiManager();
    }

    public WifiManager getWifiManager(){
        if(mWifiManager == null){
            initWifiManager();
        }
        return mWifiManager;
    }

    public WifiInfo getWifiInfo(){
        return mWifiInfo;
    }

    public void initWifiManager(){
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public List<ScanResult> getScanResults(){
        mWifiManager.startScan();
        return mWifiManager.getScanResults();
    }

    public List<WifiConfiguration> getConfiguredConnections(){
        return mWifiManager.getConfiguredNetworks();
    }
}
