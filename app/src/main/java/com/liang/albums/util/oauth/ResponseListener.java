package com.liang.albums.util.oauth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.Constants;

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

        // Get the provider
        String providerName = values.getString(SocialAuthAdapter.PROVIDER);
        Log.d("Custom-UI", "providername = " + providerName);
        String token = AlbumsApp.getInstance().getAuthAdapter()
                .getCurrentProvider().getAccessGrant().getKey();
        Intent intent = new Intent();
        intent.setAction(Constants.Broadcasts.INSTAGRAM_ACTION_LOGIN);
        intent.putExtra(Constants.Broadcasts.EX_LOGIN_STATES, Constants.Broadcasts.LoginStates.EX_LOGIN_SUCCESS);
        mContext.sendBroadcast(intent);
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, true);

    }

    @Override
    public void onError(SocialAuthError error) {
        Log.d("Custom-UI", "Error");
        error.printStackTrace();
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onCancel() {
        Log.d("Custom-UI", "Cancelled");
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onBack() {
        Log.d("Custom-UI", "Dialog Closed by pressing Back Key");
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }
}
