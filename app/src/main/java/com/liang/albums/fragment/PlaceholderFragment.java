package com.liang.albums.fragment;

/**
 * Created by liang on 15/1/3.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liang.albums.R;
import com.liang.albums.activity.MainActivity;
import com.liang.albums.util.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, Context ctx) {

        PlaceholderFragment fragment = null;// = new PlaceholderFragment();

        switch (sectionNumber){
            case Constants.Intent.ACTIVITY_INTENT_ALBUMS:
                fragment = new AlbumsShowFragment();
                break;
            case Constants.Intent.ACTIVITY_INTENT_INSTAGRAM:
                fragment = new InstagramManagementFragment();
                break;
            case Constants.Intent.ACTIVITY_INTENT_WIFI:
                fragment = new WifiSettingFragment();
                break;
            case Constants.Intent.ACTIVITY_INTENT_FACEBOOK:
            case Constants.Intent.ACTIVITY_INTENT_FLICKR:
            default:
                fragment = new PlaceholderFragment();
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
