package com.liang.albums.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by liang on 15/2/3.
 */
public class WifiListAdapter extends ArrayAdapter<ScanResult> {

    public WifiListAdapter(Context context, int textViewResourceId, List<ScanResult> objects) {
        super(context, textViewResourceId, objects);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return super.getView(position, convertView, parent);
    }
}
