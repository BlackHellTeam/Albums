package com.liang.albums.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.liang.albums.R;
import com.liang.albums.adapter.GuidePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by liang on 15/1/29.
 */
public class GuideActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "GuideActivity";

    private ViewPager mViewPager;
    private List<View> mPageList;

    private ArrayAdapter<String> mTimeZoneAdapter;
    private Spinner mTimeZoneSpinner;

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];

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
        View pageView3 = inflater.inflate(R.layout.page_item_datetime, null);

        // language page
        Button btnNext = (Button)pageView1.findViewById(R.id.btn_pager_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        //

        TextView tvDone = (TextView)pageView3.findViewById(R.id.text_pager_done);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                GuideActivity.this.finish();
            }
        });

        mTimeZoneSpinner = (Spinner)pageView3.findViewById(R.id.spinner_pager_time);
        mTimeZoneAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TimeZone.getAvailableIDs());
        mTimeZoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedId = (String) (parent
                        .getItemAtPosition(position));
                TimeZone timezone = TimeZone.getTimeZone(selectedId);
                String TimeZoneName = timezone.getDisplayName();

                int TimeZoneOffset = timezone.getRawOffset()
                        / (60 * 1000);

                int hrs = TimeZoneOffset / 60;
                int mins = TimeZoneOffset % 60;
                long miliSeconds = current.getTimeInMillis();
                miliSeconds = miliSeconds + timezone.getRawOffset();

                Date resultdate = new Date(miliSeconds);
                Log.d(TAG, sdf.format(resultdate));

                AlarmManager am = (AlarmManager)getApplication().getSystemService(Service.ALARM_SERVICE);
                am.setTimeZone(timezone.getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTimeZoneSpinner.setAdapter(mTimeZoneAdapter);


        list.add(pageView1);
        list.add(pageView2);
        list.add(pageView3);

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

    private class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = ((wifiScanList.get(i)).toString());
            }

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,wifis));
        }
    }
}
