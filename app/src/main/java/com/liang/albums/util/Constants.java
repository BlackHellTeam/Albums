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

        public static enum FragmentSection {
            ACTIVITY_SECTION_ALBUMS, ACTIVITY_SECTION_FACEBOOK, ACTIVITY_SECTION_INSTAGRAM,
            ACTIVITY_SECTION_FLICKR, ACTIVITY_SECTION_WIFI
        };
    }

    public static final class Intent{
        public static final String ACTIVITY_INTENT = "INTENT.ACTIVITY";

        public static class ActivityIntents {
            public static String ACTIVITY_INTENT_FACEBOOK = "ACTIVITY_SECTION_FACEBOOK";
            public static String ACTIVITY_INTENT_INSTAGRAM = "ACTIVITY_SECTION_INSTAGRAM";
            public static String ACTIVITY_INTENT_FLICKR = "ACTIVITY_SECTION_FLICKR";
        };

        public static final String EX_ACCOUNT = "ACCOUNT";

        public static final String EX_LOGIN_STATES = "BROADCAST.EXTRA.LOGIN.STATES";

    }

    public static final class Broadcasts{
        public static final String ACTION_LOGIN = "BROADCAST.ACTION.LOGIN";
        public static final String ACTION_LOGOUT = "BROADCAST.ACTION.LOGOUT";
        public static final String ACTION_CONTENTLIST_CHANGED = "BROADCAST.ACTION.CONTENTLIST.CHANGED";
    }


    public static final class SocialInfo{
        public static final String ACCOUNT_INSTAGRAM = "ACCOUNT.INSTAGRAM";
        public static final String ACCOUNT_FACEBOOK = "ACCOUNT.FACEBOOK";
        public static final String ACCOUNT_FLICKER = "ACCOUNT.FLICKER";

        public static enum LoginStates{
            EX_LOGIN_SUCCESS , EX_LOGIN_ERROR, EX_LOGIN_CANCEL, EX_LOGIN_BACK,
        }
    }

}
