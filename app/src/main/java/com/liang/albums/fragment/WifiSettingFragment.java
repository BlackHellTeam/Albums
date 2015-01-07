package com.liang.albums.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liang.albums.R;

/**
 * Created by liang on 15/1/8.
 */
public class WifiSettingFragment extends PlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_wifi, container, false);

        return rootView;
    }
}
