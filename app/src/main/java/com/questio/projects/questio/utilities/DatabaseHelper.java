package com.questio.projects.questio.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context c) {
        super(c, "questio.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(LOG_TAG, "onCreate() is called");
        String query;
        query = "CREATE TABLE IF NOT EXISTS place ( placeid INTEGER PRIMARY KEY," +
                " placename TEXT," +
                " placefullname TEXT," +
                " qrcode INTEGER," +
                " sensorid INTEGER," +
                " latitude TEXT," +
                " longitude TEXT," +
                " radius REAL," +
                " placetype TEXT, " +
                " imageurl TEXT )";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        onCreate(database);
    }



}
