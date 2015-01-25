package com.liang.albums.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.liang.albums.R;
import com.liang.albums.fragment.PlaceholderFragment;
import com.liang.albums.util.Constants;

/**
 * Created by liang on 15/1/7.
 */
public class SettingWifiActivity extends FragmentActivity {

    private static final String TAG = "SettingWifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Log.d(TAG, "onCreate");

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.container_setting,
                     PlaceholderFragment.newInstance(Constants.Extra.FragmentSection.ACTIVITY_SECTION_WIFI));

        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "request code : "+requestCode+"  result code : "+resultCode);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
