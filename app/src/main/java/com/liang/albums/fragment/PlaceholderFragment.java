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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected static final String ARG_SECTION_NUMBER = "section_number";

    protected static final int SECTION_NUMBER_ALBUMS = 0;
    protected static final int SECTION_NUMBER_FACEBOOK = 1;
    protected static final int SECTION_NUMBER_INSTAGRAM = 2;
    protected static final int SECTION_NUMBER_FLICKR = 3;

    protected Context mContext;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, Context ctx) {

        PlaceholderFragment fragment = null;// = new PlaceholderFragment();

        switch (sectionNumber){
            case SECTION_NUMBER_ALBUMS:
                fragment = new AlbumsShowFragment();
                break;
            case SECTION_NUMBER_INSTAGRAM:
                fragment = new InstagramManagementFragment();
                break;
            case SECTION_NUMBER_FACEBOOK:
            case SECTION_NUMBER_FLICKR:
            default:
                fragment = new PlaceholderFragment();
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setContext(ctx);

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

    public void setContext(Context ctx){
        this.mContext = ctx;
    }
}
