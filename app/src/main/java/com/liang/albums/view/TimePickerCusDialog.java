package com.liang.albums.view;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.Constants;

import java.util.Calendar;

/**
 * Created by liang on 15/2/5.
 */
public class TimePickerCusDialog extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerCusDialog";

    private Calendar calendar = Calendar.getInstance();
    private DialogInterface.OnDismissListener mDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        // Log Time
        if(view.isShown()) {
            Log.d(TAG, "HOUR_OF_DAY=" + hourOfDay + " MINUTE=" + minute);
            long ts = calendar.getTime().getTime();
            long diff = AlbumsApp.getInstance().getPreferenceUtil()
                    .getPrefLong(Constants.PreferenceConstants.TIME_DIFF_SECONDS, 0);
            calendar.setTimeInMillis(ts+diff);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            long newTs = calendar.getTime().getTime();
            AlbumsApp.getInstance().getPreferenceUtil()
                    .setSettingLong(Constants.PreferenceConstants.TIME_DIFF_SECONDS, newTs-ts);
        }
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener listener) {
        mDismissListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismissListener.onDismiss(dialog);
    }
}
