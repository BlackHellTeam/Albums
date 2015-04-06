package com.liang.albums.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.liang.albums.R;
import com.liang.albums.fragment.AccountsSettingFragment;
import com.liang.albums.fragment.FacebookSettingFragment;

/**
 * Created by liang on 15/1/7.
 */
public class SettingActivity extends FragmentActivity {

    private static final String TAG = "SettingWifiActivity";
    public static final String EXTRA_KEY = "SettingActivity_EXTRA";

    public static final String EXTRA_FACEBOOK = "FACEBOOK";
    public static final String EXTRA_INSTAGRAM = "INSTAGRAM";
    public static final String EXTRA_ACCOUNTSETTING = "ACCOUNTSETTING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Log.d(TAG, "onCreate");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new AccountsSettingFragment();

        String key = getIntent().getStringExtra(EXTRA_KEY);

        switch (key){
            case EXTRA_FACEBOOK:
                fragment = new FacebookSettingFragment();
                break;
            case EXTRA_INSTAGRAM:
//                fragment = new FacebookSettingFragment();
                break;
            case EXTRA_ACCOUNTSETTING:
                fragment = new AccountsSettingFragment();
                break;
        }



        fragmentManager.beginTransaction()
                .add(R.id.container_setting, fragment).commit();
    }
}
