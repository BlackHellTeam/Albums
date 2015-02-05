package com.liang.albums.adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.albums.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by liang on 15/2/3.
 */
public class ZoneListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<ItemContent> mContents;

    private int mSelectItem;

    public ZoneListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContents = new ArrayList<>();
        mSelectItem = 0;
        for(String zone : TimeZone.getAvailableIDs()){
            ItemContent item = new ItemContent(zone, false);
            mContents.add(item);
        }
        setItemChecked(mSelectItem);
    }

    @Override
    public int getCount() {
        return mContents.size();
    }

    @Override
    public Object getItem(int position) {
        return mContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.list_item_zone, null);
            holder.title = (TextView) convertView.findViewById(R.id.textview_zone_name);
            holder.box = (CheckBox) convertView.findViewById(R.id.checkbox_zone_check);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemChecked(position);
            }
        });

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 67);
        convertView.setLayoutParams(lp);

        holder.title.setText(mContents.get(position).zone);

        holder.box.setChecked(mContents.get(position).checked);

        return convertView;
    }

    public void setItemChecked(int position){
        for(ItemContent item : mContents){
            item.checked = false;
        }
        mContents.get(position).checked = true;
        mSelectItem = position;
        this.notifyDataSetChanged();
    }

    public int getSelectItem() {
        return mSelectItem;
    }

    private final class ViewHolder{
        public TextView title;
        public CheckBox box;
    }

    public final class ItemContent{
        public String zone;
        public boolean checked;

        public ItemContent(String zone, boolean checked){
            this.zone = zone;
            this.checked = checked;
        }
    }
}
