package com.liang.albums.view;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.liang.albums.R;
import com.liang.albums.adapter.ZoneListAdapter;
import com.liang.albums.util.AccessPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by liang on 15/2/4.
 */
public class TimeZoneDialog extends AbstractDialog {

    private static final String TAG = "TimeZoneDialog";

    private ImageButton mBtnCancel;
    private ImageButton mBtnDone;
    private ListView mListView;
    private ZoneListAdapter mListAdapter;
    private AlarmManager mAlarmManager;

    public TimeZoneDialog(Context context, AlarmManager am) {
        super(context, R.style.alert_dialog_style);
        mListAdapter = new ZoneListAdapter(context);
        mAlarmManager = am;
    }

    @Override
    protected void onCreateDialog() {
        setContentView(R.layout.dialog_time_zone);

        mListView = (ListView) findViewById(R.id.lv_time_zone);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.setItemChecked(position);
            }
        });

        mBtnCancel = (ImageButton) findViewById(R.id.btn_close);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeZoneDialog.this.dismiss();
            }
        });

        mBtnDone = (ImageButton) findViewById(R.id.btn_done);
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoneListAdapter.ItemContent item = (ZoneListAdapter.ItemContent) mListAdapter
                        .getItem(mListAdapter.getSelectItem());

                Calendar current = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
                TimeZone timezone = TimeZone.getTimeZone(item.zone);
                String TimeZoneName = timezone.getDisplayName();

                int TimeZoneOffset = timezone.getRawOffset()
                        / (60 * 1000);

                int hrs = TimeZoneOffset / 60;
                int mins = TimeZoneOffset % 60;
                long miliSeconds = current.getTimeInMillis();
                miliSeconds = miliSeconds + timezone.getRawOffset();

                Date resultdate = new Date(miliSeconds);
                Log.d(TAG, sdf.format(resultdate));


                mAlarmManager.setTimeZone(timezone.getID());
                TimeZoneDialog.this.dismiss();
            }
        });

    }

    @Override
    protected Button findOkButton() {
        return null;
    }

    @Override
    protected Button findCancelButton() {
        return null;
    }


}
