package com.liang.albums.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.liang.albums.R;
import com.liang.albums.activity.PickerActivity;
import com.liang.albums.adapter.FacebookSettingListAdapter;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/1/3.
 */
public class FacebookSettingFragment extends Fragment
        implements SocialEventsHandler {
    private static final String TAG = "FacebookManagementFragment";

    private static final int REQUEST_CODE_FRIENDS = 2626;

    private LoginButton btnLogin;
    private ImageButton btnBack;
    private TextView textName;
    private ImageView imageHead;
    private ListView mMenuList;
    private GraphUser user;
    private Session mSession;
    private FacebookSettingListAdapter mMenuAdapter;
    private AlbumsApp mApp;

    private SocialAccountsReceiver mReceiver;

    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG, "Session.StatusCallback "+state.toString());
            if(state.isOpened()){
                sendBroadcast(Constants.Broadcasts.ACTION_LOGIN);
            }else {
                sendBroadcast(Constants.Broadcasts.ACTION_LOGOUT);
            }
        }
    };
    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        mReceiver = new SocialAccountsReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = AlbumsApp.getInstance();
        View rootView = inflater.inflate(R.layout.fragment_facebook_setting, container, false);

        textName = (TextView) rootView.findViewById(R.id.text_facebook_name);
        imageHead = (ImageView) rootView.findViewById(R.id.img_facebook_head);
        mMenuList = (ListView) rootView.findViewById(R.id.list_facebook_menu);

        mMenuAdapter = new FacebookSettingListAdapter(getActivity());
        mMenuList.setAdapter(mMenuAdapter);

        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // select friends
                if(FacebookSettingFragment.this.user != null) {
                    Intent intent = new Intent();
                    intent.setData(PickerActivity.FRIEND_PICKER);
                    intent.setClass(getActivity(), PickerActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_FRIENDS);
                }
            }
        });

        List<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("user_photos");
        btnLogin = (LoginButton)rootView.findViewById(R.id.btn_facebook_login);
        btnLogin.setFragment(this);
        btnLogin.setReadPermissions(permissions);

        btnLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                //Log.d(TAG, "UserInfoChangedCallback : "+user.getUsername());
                FacebookSettingFragment.this.user = user;
                mSession = Session.getActiveSession();
                if (user != null) {
                    textName.setText(user.getName());
                    String uri = "https://graph.facebook.com/" + user.getId() + "/picture";
                    ImageLoader.getInstance().displayImage(uri, imageHead);
                    mApp.getSelectedUsers().add(user);
                }
            }
        });

        btnBack = (ImageButton) rootView.findViewById(R.id.imgbtn_facebook_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnBack pressed");
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {
        if(this.user!=null){
            textName.setText( user.getName() );
            String uri = "https://graph.facebook.com/"+user.getId()+"/picture";
            ImageLoader.getInstance().displayImage(uri, imageHead);
        }
    }

    @Override
    public void onSignOut(String account) {
        textName.setText( "User Name" );
        imageHead.setImageBitmap(null);
    }

    @Override
    public void onContentListChanged(String account) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 64206) {
            uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
        }
        if(requestCode == REQUEST_CODE_FRIENDS && resultCode == Activity.RESULT_OK){
            mApp.getSelectedUsers().add(user);
        }
    }

    private void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);//
        intent.putExtra(Constants.Intent.EX_ACCOUNT, Constants.SocialInfo.ACCOUNT_FACEBOOK);
        intent.putExtra(Constants.Intent.EX_LOGIN_STATES,
                Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS);
        getActivity().sendBroadcast(intent);
    }

}
