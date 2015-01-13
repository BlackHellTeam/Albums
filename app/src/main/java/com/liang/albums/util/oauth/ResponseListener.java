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
    private String mAccount;

    public ResponseListener(Context ctx, String account){
        this.mContext = ctx;
        this.mAccount = account;
    }

    @Override
    public void onComplete(Bundle values) {

        Log.d("Custom-UI", "Successful");

        // Get the provider
        String providerName = values.getString(SocialAuthAdapter.PROVIDER);
        Log.d("Custom-UI", "providername = " + providerName);
        String token = AlbumsApp.getInstance().getAuthInstagramAdapter()
                .getCurrentProvider().getAccessGrant().getKey();
        sendBroadcast(Constants.Broadcasts.ACTION_LOGIN, Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS);
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, true);

    }

    @Override
    public void onError(SocialAuthError error) {
        Log.d("Custom-UI", "Error");
        error.printStackTrace();
        sendBroadcast(Constants.Broadcasts.ACTION_LOGIN, Constants.SocialInfo.LoginStates.EX_LOGIN_ERROR);
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onCancel() {
        Log.d("Custom-UI", "Cancelled");
        sendBroadcast(Constants.Broadcasts.ACTION_LOGIN, Constants.SocialInfo.LoginStates.EX_LOGIN_CANCEL);
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    @Override
    public void onBack() {
        Log.d("Custom-UI", "Dialog Closed by pressing Back Key");
        sendBroadcast(Constants.Broadcasts.ACTION_LOGIN, Constants.SocialInfo.LoginStates.EX_LOGIN_BACK);
        AlbumsApp.getInstance().getPreferenceUtil()
                .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
    }

    private void sendBroadcast(String action, Constants.SocialInfo.LoginStates state){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(Constants.Intent.EX_ACCOUNT, mAccount);
        intent.putExtra(Constants.Intent.EX_LOGIN_STATES, state);
        mContext.sendBroadcast(intent);
    }
}
