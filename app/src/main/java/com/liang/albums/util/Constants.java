package com.liang.albums.util;

/**
 * Created by liang on 15/1/3.
 */
public class Constants {

    public static final class PreferenceConstants{
        // albums auto login flag
        public static final String LOGIN_INSTAGRAM = "INSTAGRAM.PREFERENCE.LOGIN";
        public static final String LOGIN_FACEBOOK = "FACEBOOK.PREFERENCE.LOGIN";
        public static final String LOGIN_FLICKER = "FLICKER.PREFERENCE.LOGIN";
    }

    public static final class Extra {
//        public static final String FRAGMENT_INDEX = "UNIVERSALIMAGELOADER.FRAGMENT_INDEX";
        public static final String IMAGE_POSITION = "UNIVERSALIMAGELOADER.IMAGE_POSITION";
    }

    public static final class Intent{
        public static final String ACTIVITY_INTENT = "INTENT.ACTIVITY";
        public static enum ActivityIntents {
        ACTIVITY_INTENT_ALBUMS, ACTIVITY_INTENT_FACEBOOK,
        ACTIVITY_INTENT_INSTAGRAM, ACTIVITY_INTENT_FLICKR, ACTIVITY_INTENT_WIFI
        };
    }

    public static final class Broadcasts{
        public static final String INSTAGRAM_ACTION_LOGIN = "INSTAGRAM.BROADCAST.ACTION.LOGIN";
        public static final String FACEBOOK_ACTION_LOGIN = "FACEBOOK.BROADCAST.ACTION.LOGIN";
        public static final String FLICKER_ACTION_LOGIN = "FLICKER.BROADCAST.ACTION.LOGIN";

        public static final String EX_LOGIN_STATES = "BROADCAST.EXTRA.LOGIN.STATES";
        public static enum LoginStates{
            EX_LOGIN_SUCCESS , EX_LOGIN_ERROR, EX_LOGIN_CANCEL, EX_LOGIN_BACK,
        }
    }

}
