package com.liang.albums.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.fragment.NavigationDrawerFragment;
import com.liang.albums.fragment.PlaceholderFragment;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.android.SocialAuthAdapter;

/**
 * Created by liang on 15/2/3.
 */
public class ShowActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #//restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static final String TAG = "ShowActivity";

    private SocialAuthAdapter authAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));

        authAdapter = AlbumsApp.getInstance().getAuthInstagramAdapter();

        if (AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
            // has a account
            try {
                authAdapter.authorize(this, SocialAuthAdapter.Provider.INSTAGRAM);
            }catch (Exception e){
                Log.d(TAG, e.getMessage());
                AlbumsApp.getInstance().getPreferenceUtil()
                        .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
            }
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_main, PlaceholderFragment.
                        newInstance(this.castPositionToSection(position)))
                .commit();
    }

    private Constants.Extra.FragmentSection castPositionToSection(int position){
        switch (position){
            case 0:
                return Constants.Extra.FragmentSection.ACTIVITY_SECTION_ALBUMS;
            case 1:
                return Constants.Extra.FragmentSection.ACTIVITY_SECTION_FACEBOOK;
            case 2:
                return Constants.Extra.FragmentSection.ACTIVITY_SECTION_INSTAGRAM;
            case 3:
                return Constants.Extra.FragmentSection.ACTIVITY_SECTION_FLICKR;
        }
        return Constants.Extra.FragmentSection.ACTIVITY_SECTION_ALBUMS;
    }

    public void onSectionAttached(Constants.Extra.FragmentSection section) {
        switch (section) {
            case ACTIVITY_SECTION_ALBUMS:
                mTitle = getString(R.string.title_section0);
                break;
            case ACTIVITY_SECTION_FACEBOOK:
                mTitle = getString(R.string.title_section1);
                break;
            case ACTIVITY_SECTION_INSTAGRAM:
                mTitle = getString(R.string.title_section2);
                break;
            case ACTIVITY_SECTION_FLICKR:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }


}
