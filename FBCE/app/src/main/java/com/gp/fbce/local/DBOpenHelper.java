package com.gp.fbce.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MW on 2/8/2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_CARDS = "cards";

    public static final String CARD_ID = "_id";
    public static final String CARD_GLOBAL_ID = "global_id";
    public static final String CARD_NAME = "name";
    public static final String CARD_PHONE = "phone";
    public static final String CARD_EMAIL = "email";
    public static final String CARD_ADDRESS = "address";
    public static final String CARD_COMPANY = "company";
    public static final String CARD_TITLE = "title";
    public static final String CARD_NOTE = "note";
    public static final String CARD_WEBSITE = "website";
    public static final String CARD_CREATED = "cardCreated";;

    public static final String[] ALL_COLUMNS = {
            CARD_ID, CARD_GLOBAL_ID, CARD_NAME, CARD_PHONE, CARD_EMAIL, CARD_ADDRESS,
            CARD_COMPANY, CARD_TITLE, CARD_NOTE, CARD_WEBSITE, CARD_CREATED };

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CARDS + " (" +
                    CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CARD_GLOBAL_ID + " TEXT, " +
                    CARD_NAME + " TEXT, " +
                    CARD_PHONE + " TEXT, " +
                    CARD_EMAIL + " TEXT, " +
                    CARD_ADDRESS + " TEXT, " +
                    CARD_COMPANY + " TEXT, " +
                    CARD_TITLE + " TEXT, " +
                    CARD_NOTE + " TEXT, " +
                    CARD_WEBSITE + " TEXT, " +
                    CARD_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DBOpenHelper(Context context) {
        
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }
}
