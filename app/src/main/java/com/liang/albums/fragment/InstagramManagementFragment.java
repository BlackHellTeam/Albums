package com.liang.albums.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liang.albums.R;
import com.liang.albums.activity.MainActivity;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Created by liang on 15/1/3.
 */
public class InstagramManagementFragment extends PlaceholderFragment {

    private SocialAuthAdapter authAdapter;
    private Button btnLogin;
    private Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authAdapter = AlbumsApp.getInstance().getAuthAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instagram, container, false);

        btnLogin = (Button)rootView.findViewById(R.id.btn_insmgr_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authAdapter.authorize(getActivity(), SocialAuthAdapter.Provider.INSTAGRAM);
                btnLogin.setClickable(false);
                btnLogout.setClickable(true);
            }
        });

        btnLogout = (Button) rootView.findViewById(R.id.btn_insmgr_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authAdapter.signOut(getActivity(), SocialAuthAdapter.Provider.INSTAGRAM.name());
                AlbumsApp.getInstance().getPreferenceUtil()
                        .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
                btnLogin.setClickable(true);
                btnLogout.setClickable(false);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
            btnLogin.setClickable(false);
            btnLogout.setClickable(true);
        }else{
            btnLogin.setClickable(true);
            btnLogout.setClickable(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                (Constants.Intent.ActivityIntents)getArguments().getSerializable(ARG_SECTION_NUMBER));
    }

}
