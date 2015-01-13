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

        switch (intent.getAction()){
            case Constants.Broadcasts.ACTION_LOGIN: {
                Constants.SocialInfo.LoginStates states = (Constants.SocialInfo.LoginStates) intent
                        .getSerializableExtra(Constants.Intent.EX_LOGIN_STATES);
                String account = (String) intent.getStringExtra(Constants.Intent.EX_ACCOUNT);
                switch (states) {
                    case EX_LOGIN_SUCCESS:
                        eventsHandler.onSignIn(account, Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS);
                        break;
                    case EX_LOGIN_ERROR:
                        eventsHandler.onSignIn(account, Constants.SocialInfo.LoginStates.EX_LOGIN_ERROR);
                        break;
                    case EX_LOGIN_BACK:
                        eventsHandler.onSignIn(account, Constants.SocialInfo.LoginStates.EX_LOGIN_BACK);
                        break;
                    case EX_LOGIN_CANCEL:
                        eventsHandler.onSignIn(account, Constants.SocialInfo.LoginStates.EX_LOGIN_CANCEL);
                        break;
                }
                break;
            }
            case Constants.Broadcasts.ACTION_LOGOUT: {
                String account = (String) intent.getStringExtra(Constants.Intent.EX_ACCOUNT);
                eventsHandler.onSignOut(account);
                break;
            }
            case Constants.Broadcasts.ACTION_CONTENTLIST_CHANGED: {
                String account = (String) intent.getStringExtra(Constants.Intent.EX_ACCOUNT);
                eventsHandler.onContentListChanged(account);
                break;
            }
        }
    }
}
