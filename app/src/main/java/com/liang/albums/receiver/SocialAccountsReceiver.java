package com.liang.albums.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.util.Constants;

/**
 * Created by liang on 15/1/11.
 */
public class SocialAccountsReceiver extends BroadcastReceiver {

    private SocialEventsHandler eventsHandler;

    public SocialAccountsReceiver(SocialEventsHandler ev){
        eventsHandler = ev;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Constants.Broadcasts.LoginStates states = (Constants.Broadcasts.LoginStates) intent
                .getSerializableExtra(Constants.Broadcasts.EX_LOGIN_STATES);
        switch (states){
            case EX_LOGIN_SUCCESS:
                eventsHandler.onSignIn(Constants.Broadcasts.LoginStates.EX_LOGIN_SUCCESS);
                break;
            case EX_LOGIN_ERROR:
                eventsHandler.onSignIn(Constants.Broadcasts.LoginStates.EX_LOGIN_ERROR);
                break;
            case EX_LOGIN_BACK:
                eventsHandler.onSignIn(Constants.Broadcasts.LoginStates.EX_LOGIN_BACK);
                break;
            case EX_LOGIN_CANCEL:
                eventsHandler.onSignIn(Constants.Broadcasts.LoginStates.EX_LOGIN_CANCEL);
                break;
        }
    }
}
