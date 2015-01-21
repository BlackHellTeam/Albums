package com.liang.albums.db;

import android.provider.BaseColumns;

/**
 * Created by liang on 15/1/16.
 */
public class SocialColums implements BaseColumns {

    public String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.liang.";
    public String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.liang.";
    public static final String DEFAULT_SORT_ORDER = "_id ASC";

    public static final String IMG_ID = "img_id";
    public static final String FROM = "author";
    public static final String MESSAGE = "message";
    public static final String SCREEN_NAME = "screen_name";
    public static final String DATE = "date";

    public SocialColums(String tableName){
        CONTENT_TYPE += tableName;
        CONTENT_ITEM_TYPE += tableName;
    }

    public static String[] getRequiredColumns(){
        String strs[] = {
                IMG_ID,
                FROM,
                MESSAGE,
                DATE
        };
        return strs;
    }
}