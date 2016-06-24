package com.gp.fbce.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by MW on 2/8/2016.
 */
public class CardsProvider extends ContentProvider {

    private static final String AUTHORITY = "com.gp.fbce.cardsprovider";
    private static final String BASE_PATH = "cards";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    public static long cardID;

    // Constant to identify the requested operation
    private static final int CARDS = 1;
    private static final int CARDS_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(AUTHORITY, BASE_PATH, CARDS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CARDS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return database.query(DBOpenHelper.TABLE_CARDS, projection, selection,selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        cardID ++ ;

        long id = database.insert(DBOpenHelper.TABLE_CARDS, null, values);
        return Uri.parse ( BASE_PATH + "/" + id );
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return database.delete(DBOpenHelper.TABLE_CARDS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return database.update(DBOpenHelper.TABLE_CARDS, values, selection, selectionArgs);
    }
}
