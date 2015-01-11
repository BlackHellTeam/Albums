package com.liang.albums.interfaces;

import com.liang.albums.util.Constants;

/**
 * Created by liang on 15/1/11.
 */
public abstract interface SocialEventsHandler {
    public abstract void onSignIn(Constants.Broadcasts.LoginStates state);
    public abstract void onSignOut();
    public abstract void onContentListChanged();
}
