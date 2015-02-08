package com.liang.albums.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liang.albums.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/2/3.
 */
public class FacebookSettingListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<ItemContent> mContents;

    private int mSelectItem;

    private Context mContext;

    public FacebookSettingListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContents = new ArrayList<>();
        mSelectItem = 0;
        mContext = context;

        mContents.add(new ItemContent(R.drawable.icon_friends,
                R.string.menu_facebook_people));
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

            convertView = mInflater.inflate(R.layout.list_item_facebook, null);
            holder.desc = (TextView) convertView.findViewById(R.id.text_facebook_item_desc);
            holder.icon = (ImageView) convertView.findViewById(R.id.img_facebook_item_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 67);
        convertView.setLayoutParams(lp);

        holder.desc.setText(mContext.getString( mContents.get(position).descResourceId) );

        holder.icon.setImageResource(mContents.get(position).imgResourceId);

        return convertView;
    }

    private final class ViewHolder{
        public TextView desc;
        public ImageView icon;
    }

    public final class ItemContent{
        public int imgResourceId;
        public int descResourceId;

        public ItemContent(int imgResourceId, int descResourceId){
            this.imgResourceId = imgResourceId;
            this.descResourceId = descResourceId;
        }
    }
}
