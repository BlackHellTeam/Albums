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
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by liang on 15/1/14.
 */
public class BaseSocialProvider extends ContentProvider {

    private static final String TAG = "BaseSocialProvider";

    public final static String TABLE_NAME = "base";
    public final static String AUTHORITY = "com.liang.albums.provider.base";
    public final static Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int MESSAGES = 1;
    private static final int MESSAGE_ID = 2;

    static {
        URI_MATCHER.addURI(AUTHORITY, "images", MESSAGES);
        URI_MATCHER.addURI(AUTHORITY, "images/#", MESSAGE_ID);
    }

    private SQLiteOpenHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new SocialDatabaseHelper(getContext());

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
                return SocialColums.CONTENT_TYPE;
            case MESSAGE_ID:
                return SocialColums.CONTENT_ITEM_TYPE;
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

    private static class SocialDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "chat.db";
        private static final int DATABASE_VERSION = 6;

        public SocialDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + SocialColums._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SocialColums.IMG_ID + " TEXT);"
                    + SocialColums.DATE + " INTEGER,"
                    + SocialColums.MESSAGE + " TEXT,"
                    + SocialColums.FROM + " TEXT,"
                    + SocialColums.SCREEN_NAME + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            switch (oldVersion) {
                case 3:
                    db.execSQL("UPDATE " + TABLE_NAME + " SET READ=1");
                case 4:
                    db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD "
                            + SocialColums.IMG_ID + " TEXT");
                    break;
                default:
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(db);
            }
        }

    }

    public static final class SocialColums implements BaseColumns{

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.liang.base";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.liang.base";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String IMG_ID = "img_id";
        public static final String FROM = "FROM";
        public static final String MESSAGE = "message";
        public static final String SCREEN_NAME = "screen_name";
        public static final String DATE = "date";

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
}
