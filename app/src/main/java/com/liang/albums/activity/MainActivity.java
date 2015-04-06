package com.liang.albums.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.fragment.PlaceholderFragment;
import com.liang.albums.util.Constants;



public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (AlbumsApp.getInstance().getPreferenceUtil()
//                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
//            // has a account
//            try {
//                //
////                authAdapter.authorize(this, SocialAuthAdapter.Provider.INSTAGRAM);
//            }catch (Exception e){
//                Log.d(TAG, e.getMessage());
//                AlbumsApp.getInstance().getPreferenceUtil()
//                        .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
//            }
//        }
        imageButton = (ImageButton) findViewById(R.id.imgbtn_main_settings);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_main, PlaceholderFragment.
                        newInstance(this.castPositionToSection(0)))
                .commit();

        if(AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.FIRST_USE_FLAG, true)){
            startActivity(new Intent(this, GuideActivity.class));
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra(SettingActivity.EXTRA_KEY, SettingActivity.EXTRA_ACCOUNTSETTING);
                startActivity(intent);
            }
        });

//        initializeActiveSessionWithCachedToken(this);

    }

    private Constants.Extra.FragmentSection castPositionToSection(int position){
        switch (position){
            case 0:
                return Constants.Extra.FragmentSection.ACTIVITY_SECTION_ALBUMS;
        }
        return Constants.Extra.FragmentSection.ACTIVITY_SECTION_ALBUMS;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
