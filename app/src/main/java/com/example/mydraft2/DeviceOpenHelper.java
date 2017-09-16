package com.example.mydraft2;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dikki on 25/02/2017.
 */

/* package */
class DeviceOpenHelper extends SQLiteOpenHelper {
    private static final String TAG =DeviceOpenHelper.class.getSimpleName();
    private static final int SCHEMA_VERSION = 1;
    private static final String DB_NAME = "mydb.Relationdb";

    private final Context context;

    private static DeviceOpenHelper instance;

    public synchronized static DeviceOpenHelper getInstance(Context ctx) {
        Log.d(TAG, "getInstance: Called");
        if (instance == null) {
            Log.d(TAG, "getInstance: instance Called");
            instance = new DeviceOpenHelper(ctx.getApplicationContext());
            
        }
        return instance;
    }

/*
    public SQLiteDatabase getDeviceWritableDatabase()
    {
        return  getWritableDatabase();
    }

    public SQLiteDatabase getDeviceReadableDatabase()
    {
        return getReadableDatabase();
    }
*/

    public boolean delete(String table, String whereColumn , String[] values )
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(table, whereColumn , values) > 0;
    }

    public void deviceInsert(String table, String nullColumnHack, ContentValues values)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table,nullColumnHack,values);
    }

    public Cursor deviceRawQuery(String sql, String[] selectionArgs)
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,selectionArgs);
    }

    /**
     * Creates a new instance of the simple open helper.
     *
     * @param context Context to read assets. This will be helped by the
     *                instance.
     */
    private DeviceOpenHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA_VERSION);
        Log.d(TAG, "DeviceOpenHelper:  Inside");
        this.context = context;

        // This will happen in onConfigure for API >= 16
        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            SQLiteDatabase db = getWritableDatabase();
            db.enableWriteAheadLogging();
           db.execSQL("PRAGMA foreign_keys = ON;");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: called");
        for (int i = 1; i <= SCHEMA_VERSION; i++) {
            applySqlFile(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        for (int i = (oldVersion + 1); i <= newVersion; i++) {
            applySqlFile(db, i);
        }
    }
/*
    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }*/

    private void applySqlFile(SQLiteDatabase db, int version) {
        BufferedReader reader = null;

        try {
            String filename = "db.sql."+ version;
            Log.d(TAG, "applySqlFile: Filename is "+filename);
            final InputStream inputStream =
                    context.getAssets().open(filename);
            reader =   new BufferedReader(new InputStreamReader(inputStream));

            final StringBuilder statement = new StringBuilder();

            for (String line; (line = reader.readLine()) != null;) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Reading line -> " + line);
                }

                // Ignore empty lines
                if (!TextUtils.isEmpty(line) && !line.startsWith("--")) {
                    statement.append(line.trim());
                }

                if (line.endsWith(";")) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Running statement " + statement);
                    }

                    db.execSQL(statement.toString());
                    statement.setLength(0);
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "Could not apply SQL file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(TAG, "Could not close reader", e);
                }
            }
        }
    }
}


