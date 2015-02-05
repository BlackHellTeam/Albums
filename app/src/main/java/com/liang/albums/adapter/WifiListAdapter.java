package com.liang.albums.adapter;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.albums.R;
import com.liang.albums.util.AccessPoint;

import java.util.List;

/**
 * Created by liang on 15/2/3.
 */
public class WifiListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<AccessPoint> mData;

    public WifiListAdapter(Context context, List<AccessPoint> objects) {
        mInflater = LayoutInflater.from(context);
        mData = objects;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.list_item_wifi, null);
            holder.checked = (ImageView) convertView.findViewById(R.id.img_wifi_checked);
            holder.icon = (ImageView) convertView.findViewById(R.id.img_wifi_icon);
            holder.info = (TextView) convertView.findViewById(R.id.textview_wifi_ssid);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.info.setText(mData.get(position).ssid);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 67);
        convertView.setLayoutParams(lp);

        int level = mData.get(position).getLevel();

        switch (level){
            case 0:
                holder.icon.setImageResource(R.drawable.ic_wifi_signal_1);
                break;
            case 1:
                holder.icon.setImageResource(R.drawable.ic_wifi_signal_2);
                break;
            case 2:
                holder.icon.setImageResource(R.drawable.ic_wifi_signal_3);
                break;
            case 3:
                holder.icon.setImageResource(R.drawable.ic_wifi_signal_3);
                break;
        }

        if(mData.get(position).getState() == NetworkInfo.DetailedState.CONNECTED){
            holder.checked.setVisibility(View.VISIBLE);
            holder.checked.setImageResource(R.drawable.selector_radio);
        }else {
            holder.checked.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }


    private final class ViewHolder{
        public ImageView checked;
        public ImageView icon;
        public TextView info;
    }
}
