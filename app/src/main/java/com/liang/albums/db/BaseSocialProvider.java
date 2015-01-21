package com.liang.albums.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by liang on 15/1/14.
 */
public class BaseSocialProvider extends ContentProvider {

    private static final String TAG = "BaseSocialProvider";

    public String TABLE_NAME = "base";
    public String AUTHORITY = "com.liang.albums.provider.";
    public Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int MESSAGES = 1;
    private static final int MESSAGE_ID = 2;

    static {

    }

    public SQLiteOpenHelper mOpenHelper;

    public SocialColums mSocialColums;

    public BaseSocialProvider(String tableName){
        mSocialColums = new SocialColums(tableName);

        TABLE_NAME = tableName;
        AUTHORITY += TABLE_NAME;
        CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);


        URI_MATCHER.addURI(AUTHORITY, "instagram", MESSAGES);
//        URI_MATCHER.addURI(AUTHORITY, "images", MESSAGES);
        URI_MATCHER.addURI(AUTHORITY, "instagram/#", MESSAGE_ID);

    }

    @Override
    public boolean onCreate() {
//        Context ctx = getContext();
//        mOpenHelper = new SocialDatabaseHelper(ctx, TABLE_NAME);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        int match = URI_MATCHER.match(uri);

        switch (match) {
            case MESSAGES:
                qBuilder.setTables(TABLE_NAME);
                break;
            case MESSAGE_ID:
                qBuilder.setTables(TABLE_NAME);
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere(uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }

        String orderBy = "";
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = SocialColums.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = qBuilder.query(db, projection, selection, selectionArgs,
                null, null, orderBy);

        if (ret == null) {
            Log.d(TAG, "ChatProvider.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return ret;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case MESSAGES:
                return mSocialColums.CONTENT_TYPE;
            case MESSAGE_ID:
                return mSocialColums.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (URI_MATCHER.match(uri) != MESSAGES) {
            throw new IllegalArgumentException("Cannot insert into URL: " + uri);
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        for (String colName : SocialColums.getRequiredColumns()) {
            if (values.containsKey(colName) == false) {
                throw new IllegalArgumentException("Missing column: " + colName);
            }
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId = db.insert(TABLE_NAME, SocialColums.DATE, values);

        if (rowId < 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }

        Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(noteUri, null);

        return noteUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (URI_MATCHER.match(uri)) {

            case MESSAGES:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case MESSAGE_ID:
                String segment = uri.getPathSegments().get(1);

                if (TextUtils.isEmpty(selection)) {
                    selection = "_id=" + segment;
                } else {
                    selection = "_id=" + segment + " AND (" + selection + ")";
                }

                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        long rowId = 0;
        int match = URI_MATCHER.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (match) {
            case MESSAGES:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case MESSAGE_ID:
                String segment = uri.getPathSegments().get(1);
                rowId = Long.parseLong(segment);
                count = db.update(TABLE_NAME, values, "_id=" + rowId, null);
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
