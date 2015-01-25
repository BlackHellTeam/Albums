package com.liang.albums.fragment;

/**
 * Created by liang on 15/1/3.
 */

import android.app.Activity;
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
    public static PlaceholderFragment newInstance(Constants.Extra.FragmentSection section) {

        PlaceholderFragment fragment = null;// = new PlaceholderFragment();

        switch (section){
            case ACTIVITY_SECTION_ALBUMS:
                fragment = new AlbumsShowFragment();
                break;
            case ACTIVITY_SECTION_INSTAGRAM:
                fragment = new InstagramManagementFragment();
                break;
            case ACTIVITY_SECTION_WIFI:
                fragment = new WifiSettingFragment();
                break;
            case ACTIVITY_SECTION_FACEBOOK:
            case ACTIVITY_SECTION_FLICKR:
            default:
                fragment = new PlaceholderFragment();
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_NUMBER, section);
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
                (Constants.Extra.FragmentSection)
                        getArguments().getSerializable(ARG_SECTION_NUMBER));
    }

}
