package com.liang.albums.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.liang.albums.R;
import com.liang.albums.activity.GuideActivity;
import com.liang.albums.activity.SettingActivity;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class LoginDrawerFragment extends Fragment implements SocialEventsHandler {

    private static final String TAG = "LoginDrawerFragment";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private Button mBtnSetting;
    private Button mBtnFacebook;
    private Button mBtnInstagram;
    private TextView mTextViewFacebook;
    private TextView mTextViewInstagram;

    private SocialAccountsReceiver mReceiver;
    private SocialAuthAdapter mAuthAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.fragment_login_drawer, container, false);

        mBtnSetting = (Button) loginView.findViewById(R.id.btn_login_setting);
        mBtnFacebook = (Button) loginView.findViewById(R.id.btn_login_facebook);
        mBtnInstagram = (Button) loginView.findViewById(R.id.btn_login_instagram);
        mTextViewFacebook = (TextView) loginView.findViewById(R.id.text_login_facebook);
        mTextViewInstagram = (TextView) loginView.findViewById(R.id.text_login_instagram);

        mAuthAdapter = AlbumsApp.getInstance().getAuthInstagramAdapter();

        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GuideActivity.class));
            }
        });

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
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        mReceiver = new SocialAccountsReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, intentFilter);

        return loginView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.hide();

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {
        if(state != Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS) {
            Toast.makeText(getActivity(), "Login failed!", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(account.equals(Constants.SocialInfo.ACCOUNT_INSTAGRAM)){
            mTextViewInstagram.setText("logout");
        }
    }

    @Override
    public void onSignOut(String account) {
        if(account.equals(Constants.SocialInfo.ACCOUNT_INSTAGRAM)){
            mTextViewInstagram.setText("login");
        }
    }

    @Override
    public void onContentListChanged(String account) {

    }

    // facebook auth adapter
    // call back
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG, "Session.StatusCallback "+state.toString());
            if(state.isOpened()){
                Intent intent = new Intent();
                intent.setAction(Constants.Broadcasts.ACTION_LOGIN);
                intent.putExtra(Constants.Intent.EX_ACCOUNT, Constants.SocialInfo.ACCOUNT_FACEBOOK);
                intent.putExtra(Constants.Intent.EX_LOGIN_STATES,
                        Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS);
                getActivity().sendBroadcast(intent);
            }
        }
    };
}
