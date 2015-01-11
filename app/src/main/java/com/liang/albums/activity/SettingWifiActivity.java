package com.liang.albums.activity;

import android.content.Intent;
import android.os.Bundle;
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

        Intent intent = this.getIntent();

        fragmentManager.beginTransaction()
                .add(R.id.container_setting,
                     PlaceholderFragment.newInstance(Constants.Intent.ActivityIntents.ACTIVITY_INTENT_WIFI));

    }
}
