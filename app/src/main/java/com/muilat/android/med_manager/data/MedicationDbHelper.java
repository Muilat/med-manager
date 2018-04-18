package com.muilat.android.med_manager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muilat.android.med_manager.data.MedicationContract.MedicationEntry;

/**
 * Created by my computer on 26-Mar-18.
 */

public class MedicationDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "med_manager.db";
    static final int DATABASE_VERSION = 1;

    public MedicationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MED_MANAGER_TABLE = "CREATE TABLE " + MedicationContract.MedicationEntry.TABLE_NAME + "( "+
                MedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MedicationEntry.COLUMN_MED_NAME + " TEXT NOT NULL, " +
                MedicationEntry.COLUMN_MED_DESCRIPTION + " TEXT NOT NULL, " +
                MedicationEntry.COLUMN_FREQUENCY + " INTEGER NOT NULL, " +
                MedicationEntry.COLUMN_START_DATE + " LONG," +
                MedicationEntry.COLUMN_END_DATE + " LONG, " +
                MedicationEntry.COLUMN_NEXT_TIMING + " LONG" +
                "); ";

        db.execSQL(SQL_CREATE_MED_MANAGER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + MedicationEntry.TABLE_NAME);
        onCreate(db);
    }
}
