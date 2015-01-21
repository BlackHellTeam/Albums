package com.liang.albums.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by liang on 15/1/14.
 */
public class InstagramProvider extends BaseSocialProvider {

    private static final String TAG = "InstagramProvider";

    public final static String TABLE_NAME = "instagram";
    public final static String AUTHORITY = "com.liang.albums.provider.instagram";
    public final static Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    public InstagramProvider(){

        super(TABLE_NAME);

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SocialDatabaseHelper(getContext(), TABLE_NAME);
        return super.onCreate();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return super.query(uri,projection,selection,selectionArgs,sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return super.getType(uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return super.insert(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return super.update(uri, values, selection, selectionArgs);
    }

}
