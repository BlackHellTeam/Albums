package com.liang.albums.adapter;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.AccessPoint;
import com.liang.albums.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by liang on 15/2/3.
 */
public class TimeSettingListAdapter extends BaseAdapter {

    private static final String TAG = "TimeSettingListAdapter";

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

        // get current info
        long diff = AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefLong(Constants.PreferenceConstants.TIME_DIFF_SECONDS, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis()+diff);
        TimeZone tz = cal.getTimeZone();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Log.d(TAG, "Time zone=" + tz.getDisplayName());

        switch (position){
            case 0:
                // Time Zone
                String timeZoneName = AlbumsApp.getInstance().getPreferenceUtil()
                        .getPrefString(Constants.PreferenceConstants.TIME_ZONE_NAME, "");
                if(timeZoneName.equals("")){
                    timeZoneName = tz.getDisplayName();
                }
                holder.info.setText(timeZoneName);
                break;
            case 1:
                // Date
                holder.info.setText(dateFormat.format(date));
                break;
            case 2:
                // Time
                holder.info.setText(timeFormat.format(date));
                break;
        }

        return convertView;
    }


    private final class ViewHolder{
        public TextView title;
        public TextView info;
    }
}
