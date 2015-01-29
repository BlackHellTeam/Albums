package com.liang.albums.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.liang.albums.R;
import com.liang.albums.adapter.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/1/29.
 */
public class GuideActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<View> mPageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        mPageList = initPageList();
        mViewPager.setAdapter(new GuidePagerAdapter(mPageList));
    }

    private List<View> initPageList(){
        // To init language page and wifi selection page
        List<View> list = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(this);
        View pageView1 = inflater.inflate(R.layout.page_item_language, null);
        View pageView2 = inflater.inflate(R.layout.page_item_wifi, null);

        mPageList.add(pageView1);
        mPageList.add(pageView2);

        return list;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
