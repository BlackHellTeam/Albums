package com.liang.albums.adapter;

import android.content.Context;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.albums.R;
import com.liang.albums.util.AccessPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/2/3.
 */
public class TimeSettingListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<String> mTitles;

    public TimeSettingListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mTitles = new ArrayList<>();
        mTitles.add("Time Zone");
        mTitles.add("Date");
        mTitles.add("Time");
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mTitles.get(position);
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

            convertView = mInflater.inflate(R.layout.list_item_time, null);
            holder.title = (TextView) convertView.findViewById(R.id.textview_time_title);
            holder.info = (TextView) convertView.findViewById(R.id.textview_time_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 67);
        convertView.setLayoutParams(lp);

        holder.title.setText(mTitles.get(position));

//        switch (position){
//            case 0:
//                holder.title.setText("Time Zone");
//                break;
//            case 1:
//                holder.title.setText("Date");
//                break;
//            case 2:
//                holder.title.setText("Time");
//                break;
//        }

        return convertView;
    }


    private final class ViewHolder{
        public TextView title;
        public TextView info;
    }
}
