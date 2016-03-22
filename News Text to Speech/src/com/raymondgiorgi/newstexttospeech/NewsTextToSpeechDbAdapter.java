package com.raymondgiorgi.newstexttospeech;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;

/**
 * Created by no-vivisimo on 3/21/2016.
 */
public class NewsTextToSpeechDbAdapter {

    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(DATABASE_TAG, "Database create");
            db.execSQL(NEWS_SOURCE_CREATE);
            db.execSQL(NEWS_ENTRY_CREATE);
            for (int i = 0; i < NEWS_SOURCES_INITIAL_INSERTS.length; i++) {
                db.execSQL(NEWS_SOURCES_INITIAL_INSERTS[i]);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public NewsTextToSpeechDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NewsTextToSpeechDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx, DATABASE_TAG, null, 1);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context mCtx;

    public static final String DATABASE_TAG = "database";

    public static final String DATABASE_NAME = "news_database";
    public static final int DATABASE_VERSION = 1;

    public static final String NEWS_SOURCE_CREATE = "create table news_sources(_id integer primay key autoincrement, name text not null unique, url text not null unique)";
    public static final String NEWS_ENTRY_CREATE = "create table news_entries(_id integer primary key autoincrement, title text not null unique, url text not null unique, article text not null, published_on text not null, read boolean not null default false)";

    public static final String[] NEWS_SOURCES_INITIAL_INSERTS = {"insert into news_sources(name, url) values ('Naked Capitalism', '')"};

    public static final String NEWS_SOURCES_TABLE = "news_sources";
    public static final String NEWS_SOURCES_ID = "_id";
    public static final String NEWS_SOURCES_NAME = "name";
    public static final String NEWS_SOURCES_URL = "url";

    public static final String NEWS_ENTRIES_TABLE = "news_entries";
    public static final String NEWS_ENTRIES_ID = "_id";
    public static final String NEWS_ENTRIES_TITLE = "title";
    public static final String NEWS_ENTRIES_URL = "url";
    public static final String NEWS_ENTRIES_ARTICLE = "article";
    public static final String NEWS_ENTRIES_DATE = "published_on";
    public static final String NEWS_ENTRIES_READ = "read";

    public long createNewsEntry(String title, String url, String article, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NEWS_ENTRIES_TITLE, title);
        initialValues.put(NEWS_ENTRIES_URL, url);
        initialValues.put(NEWS_ENTRIES_ARTICLE, article);
        initialValues.put(NEWS_ENTRIES_DATE, date);

        long insert = mDb.insert(NEWS_ENTRIES_TABLE, null, initialValues);
        return insert;
    }
}
