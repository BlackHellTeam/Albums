package com.liang.albums.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liang.albums.R;
import com.liang.albums.util.AccessPoint;

import java.util.List;

/**
 * Created by liang on 15/2/4.
 */
public class WifiPasswordDialog extends AbstractDialog {

    private View mView;
    private Button mBtnCancel;
    private Button mBtnDone;
    private EditText mEditPassword;
    private AccessPoint mAccessPoint;
    private int mSecurity;
    private WifiManager mWifiManager;
    private List<AccessPoint> mAccessPoints;
    private int mLastPriority;

    private static final int INVALID_NETWORK_ID = -1;

    public WifiPasswordDialog(Context context, AccessPoint accessPoint, List<AccessPoint> points, WifiManager wifiMgr) {
        super(context, R.style.alert_dialog_style);
        mAccessPoint = accessPoint;
        mSecurity = (accessPoint == null) ? AccessPoint.SECURITY_NONE : accessPoint.security;
        mWifiManager = wifiMgr;
        mAccessPoints = points;
        mLastPriority = 0;
    }

    @Override
    protected void onCreateDialog() {
        setContentView(R.layout.dialog_wifi_password);

        mEditPassword = (EditText) findViewById(R.id.edit_wifi_password);

        mBtnCancel = (Button) findViewById(R.id.btn_close);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiPasswordDialog.this.dismiss();
            }
        });

        mBtnDone = (Button) findViewById(R.id.btn_done);
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiConfiguration config = getConfig();

                if (config == null) {
                    if (mAccessPoint != null && !requireKeyStore(mAccessPoint.getConfig())) {
                        connect(mAccessPoint.networkId);
                    }
                } else if (config.networkId != INVALID_NETWORK_ID) {
                    if (mAccessPoint != null) {
                        mWifiManager.updateNetwork(config);
                        saveNetworks();
                    }
                } else {
                    int networkId = mWifiManager.addNetwork(config);
                    if (networkId != INVALID_NETWORK_ID) {
                        mWifiManager.enableNetwork(networkId, false);
                        config.networkId = networkId;
                        if (true || requireKeyStore(config)) {
                            saveNetworks();
                        } else {
                            connect(networkId);
                        }
                    }
                }
                WifiPasswordDialog.this.dismiss();
            }
        });

    }

    @Override
    protected Button findOkButton() {
        return (Button) findViewById(R.id.btn_done);
    }

    @Override
    protected Button findCancelButton() {
        return (Button) findViewById(R.id.btn_close);
    }

    private boolean requireKeyStore(WifiConfiguration config) {
        return false;
    }

    public WifiConfiguration getConfig() {
        if (mAccessPoint != null && mAccessPoint.networkId != -1 && !true) {
            return null;
        }

        WifiConfiguration config = new WifiConfiguration();

        if (mAccessPoint == null) { // not support hidden SSID
//            config.SSID = AccessPoint.convertToQuotedString(mSsid.getText().toString());
//            // If the user adds a network manually, assume that it is hidden.
//            config.hiddenSSID = true;
        } else if (mAccessPoint.networkId == -1) {
            config.SSID = AccessPoint.convertToQuotedString(mAccessPoint.ssid);
        } else {
            config.networkId = mAccessPoint.networkId;
        }

        switch (mSecurity) {
            case AccessPoint.SECURITY_NONE:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                // FIXME save hub user & pwd
                mAccessPoint.mHubUser = "";
                mAccessPoint.mHubPassword = "";
                return config;

            case AccessPoint.SECURITY_WEP:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                if (mEditPassword.length() != 0) {
                    int length = mEditPassword.length();
                    String password = mEditPassword.getText().toString();
                    // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                    if ((length == 10 || length == 26 || length == 58) &&
                            password.matches("[0-9A-Fa-f]*")) {
                        config.wepKeys[0] = password;
                    } else {
                        config.wepKeys[0] = '"' + password + '"';
                    }
                }
                return config;

            case AccessPoint.SECURITY_PSK:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                if (mEditPassword.length() != 0) {
                    String password = mEditPassword.getText().toString();
                    if (password.matches("[0-9A-Fa-f]{64}")) {
                        config.preSharedKey = password;
                    } else {
                        config.preSharedKey = '"' + password + '"';
                    }
                }
                return config;

            case AccessPoint.SECURITY_EAP:
                return null;
        }
        return null;
    }

    private void saveNetworks() {
        // Always save the configuration with all networks enabled.
        mWifiManager.saveConfiguration();
    }

    private void connect(int networkId) {
        if (networkId == INVALID_NETWORK_ID) {
            return;
        }

        // Reset the priority of each network if it goes too high.
        if (mLastPriority > 1000000) {
            for (int i = mAccessPoints.size() - 1; i >= 0; --i) {
                AccessPoint accessPoint = mAccessPoints.get(i);
                if (accessPoint.networkId != INVALID_NETWORK_ID) {
                    WifiConfiguration config = new WifiConfiguration();
                    config.networkId = accessPoint.networkId;
                    config.priority = 0;
                    mWifiManager.updateNetwork(config);
                }
            }
            mLastPriority = 0;
        }

        // Set to the highest priority and save the configuration.
        WifiConfiguration config = new WifiConfiguration();
        config.networkId = networkId;
        config.priority = ++mLastPriority;
        mWifiManager.updateNetwork(config);
        saveNetworks();

        // Connect to network by disabling others.
        mWifiManager.enableNetwork(networkId, true);
        mWifiManager.reconnect();
    }

}
