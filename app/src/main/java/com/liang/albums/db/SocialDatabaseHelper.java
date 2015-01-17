package com.liang.albums.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liang on 15/1/18.
 */
public class SocialDatabaseHelper extends SQLiteOpenHelper {

    private String TABLE_NAME = "";
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 6;

    public SocialDatabaseHelper(Context context, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = tableName;
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
