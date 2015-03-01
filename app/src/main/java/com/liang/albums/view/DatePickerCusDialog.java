package com.liang.albums.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.DatePicker;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.Constants;

import java.util.Calendar;

/**
 * Created by liang on 15/2/5.
 */
public class DatePickerCusDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, DialogInterface.OnDismissListener {

    private static final String TAG = "DatePickerCusDialog";

    private Calendar calendar = Calendar.getInstance();

    private DialogInterface.OnDismissListener mDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        // Log time
        if (view.isShown()) {
            Log.d(TAG, " year=" + year + " month=" + month + " day=" + day);
            long ts = calendar.getTime().getTime();
            long diff = AlbumsApp.getInstance().getPreferenceUtil()
                    .getPrefLong(Constants.PreferenceConstants.TIME_DIFF_SECONDS, 0);
            calendar.setTimeInMillis(ts+diff);
            calendar.set(year, month, day);
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
