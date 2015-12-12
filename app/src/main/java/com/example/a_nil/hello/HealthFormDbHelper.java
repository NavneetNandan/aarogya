package com.example.a_nil.hello;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Navneet on 07-11-2015.
 */
 class HealthFormContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public HealthFormContract() {}

    /* Inner class that defines the table contents */
    public static abstract class HealthEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_DOE = "doe";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_HAEMOGLOBIN = "haemoglobin";
        public static final String COLUMN_NAME_BLOODPRESSURE = "bloodpressure";
        public static final String COLUMN_NAME_BLOODSUGAR = "bloodsugar";
        public static final String COLUMN_NAME_MARTIALSTATUS = "martialstatus";
        public static final String COLUMN_NAME_VISION = "vision";
        public static final String COLUMN_NAME_THYROID = "thyroid";
    }
}

public class HealthFormDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.

    final String TEXT_TYPE = " TEXT";
    final String COMMA_SEP = ",";
    final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HealthFormContract.HealthEntry.TABLE_NAME + " (" +
                    HealthFormContract.HealthEntry._ID + " INTEGER PRIMARY KEY," +
                    HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_DOE + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS + TEXT_TYPE+COMMA_SEP+
                    HealthFormContract.HealthEntry.COLUMN_NAME_VISION + TEXT_TYPE + COMMA_SEP +
                    HealthFormContract.HealthEntry.COLUMN_NAME_THYROID + TEXT_TYPE +
                    " )";

    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HealthFormContract.HealthEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HealthForm.db";

    public HealthFormDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
