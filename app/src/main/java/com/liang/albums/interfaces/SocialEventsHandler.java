package com.liang.albums.interfaces;

import com.liang.albums.util.Constants;

/**
 * Created by liang on 15/1/11.
 */
public abstract interface SocialEventsHandler {
    public abstract void onSignIn(String account, Constants.SocialInfo.LoginStates state);
    public abstract void onSignOut(String account);
    public abstract void onContentListChanged(String account);
}
