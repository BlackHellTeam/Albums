package com.liang.albums.fragment;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.liang.albums.R;
import com.liang.albums.activity.GuideActivity;
import com.liang.albums.activity.SettingActivity;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Created by liang on 15/4/5.
 */
public class AccountsSettingFragment extends Fragment {

    private static final String TAG = "AccountsSettingFragment";

    ImageButton mBtnFacebook;
    ImageButton mBtnInstagram;

    ImageButton mBtnClose;
    ImageButton mBtnGuide;

    private SocialAuthAdapter mAuthAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthAdapter = AlbumsApp.getInstance().getAuthInstagramAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_account_setting, container, false);

        mBtnFacebook  = (ImageButton)rootView.findViewById(R.id.imgbtn_setting_facebook);
        mBtnInstagram = (ImageButton)rootView.findViewById(R.id.imgbtn_setting_instagram);
        mBtnClose = (ImageButton)rootView.findViewById(R.id.imgbtn_account_setting_close);
        mBtnGuide = (ImageButton)rootView.findViewById(R.id.imgbtn_account_setting_guide);

        mBtnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "album app login instagram ");
                if(AlbumsApp.getInstance().getPreferenceUtil()
                        .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
                    Log.d(TAG, "logout ");
                    // logout
                    mAuthAdapter.signOut(getActivity(), SocialAuthAdapter.Provider.INSTAGRAM.toString());
                    AlbumsApp.getInstance().getPreferenceUtil()
                            .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
                    Intent intent = new Intent();
                    intent.setAction(Constants.Broadcasts.ACTION_LOGOUT);
                    intent.putExtra(Constants.Intent.EX_ACCOUNT,
                            Constants.SocialInfo.ACCOUNT_INSTAGRAM);
                    getActivity().sendBroadcast(intent);
                }else {
                    Log.d(TAG, "login ");
                    // login
                    try {
                        mAuthAdapter.authorize(getActivity(), SocialAuthAdapter.Provider.INSTAGRAM);
                    }catch (Exception e){
                        Log.d(TAG, "authAdapter.authorize failed : " + e.getMessage());
                    }
                }
            }
        });


        mBtnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra(SettingActivity.EXTRA_KEY, SettingActivity.EXTRA_FACEBOOK);
                startActivity(intent);
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mBtnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GuideActivity.class));
            }
        });

        return rootView;
    }
}
