package com.liang.albums.util;

/**
 * Created by liang on 15/1/3.
 */
public class Constants {

    public static final class PreferenceConstants{
        // albums auto login flag
        public static final String LOGIN_INSTAGRAM = "INSTAGRAM.LOGIN";
    }

    public static final class Extra {
//        public static final String FRAGMENT_INDEX = "UNIVERSALIMAGELOADER.FRAGMENT_INDEX";
        public static final String IMAGE_POSITION = "UNIVERSALIMAGELOADER.IMAGE_POSITION";
    }

    public static final class Intent{
        public static final String ACTIVITY_INTENT = "INTENT.ACTIVITY";
        public static final int ACTIVITY_INTENT_ALBUMS = 0;
        public static final int ACTIVITY_INTENT_FACEBOOK = 1;
        public static final int ACTIVITY_INTENT_INSTAGRAM = 2;
        public static final int ACTIVITY_INTENT_FLICKR = 3;
        public static final int ACTIVITY_INTENT_WIFI = 4;
    }
}
