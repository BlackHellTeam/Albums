package com.liang.albums.util.oauth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.PreferenceConstants;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

/**
 * Created by liang on 15/1/3.
 */
// To receive the response after authentication
public final class ResponseListener implements DialogListener {
    private Context mContext;

    public ResponseListener(Context ctx){
        this.mContext = ctx;
    }

    @Override
    public void onComplete(Bundle values) {

        Log.d("Custom-UI", "Successful");

        // Changing Sign In Text to Sign Out
//            View v = listview.getChildAt(pos - listview.getFirstVisiblePosition());
//            TextView pText = (TextView) v.findViewById(R.id.signstatus);
//            pText.setText("Sign Out");

        // Get the provider
        String providerName = values.getString(SocialAuthAdapter.PROVIDER);
        Log.d("Custom-UI", "providername = " + providerName);
        String token = AlbumsApp.getInstance().getAuthAdapter()
                .getCurrentProvider().getAccessGrant().getKey();
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(PreferenceConstants.LOGIN_INSTAGRAM, true);

//            Toast.makeText(InstagramManagementFragment.this, providerName + " connected", Toast.LENGTH_SHORT).show();

//            int res = getResources().getIdentifier(providerName + "_array", "array", mContext.getPackageName());

//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("Select Options");
//            builder.setCancelable(true);
//            builder.setIcon(android.R.drawable.ic_menu_more);
//
//            mDialog = new ProgressDialog(mContext);
//            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            mDialog.setMessage("Loading...");
//
//            builder.setSingleChoiceItems(new DialogAdapter(InstagramManagementFragment.this, R.layout.provider_options, getResources()
//                    .getStringArray(res)), 0, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int item) {
//
//                    Events(item, providerName);
//                    dialog.dismiss();
//                }
//            });
//            dialog = builder.create();
//            dialog.show();

    }

    @Override
    public void onError(SocialAuthError error) {
        Log.d("Custom-UI", "Error");
        error.printStackTrace();
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onCancel() {
        Log.d("Custom-UI", "Cancelled");
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onBack() {
        Log.d("Custom-UI", "Dialog Closed by pressing Back Key");
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(PreferenceConstants.LOGIN_INSTAGRAM, false);
    }
}
