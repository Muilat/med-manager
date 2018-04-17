package com.muilat.android.medication.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by my computer on 26-Mar-18.
 */

public class MedicationContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.muilat.android.medication";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "medications" directory
    public static final String PATH_MEDICATIONS = "medications";

    public static final class MedicationEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATIONS).build();

        public static final String TABLE_NAME = "med_manager";
        // COLUMN_MED_NAME -> medName
        public static final String COLUMN_MED_NAME = "medName";
        // COLUMN_MED_DESCRIPTION -> medDescription
        public static final String COLUMN_MED_DESCRIPTION = "medDescription";
        // COLUMN_FREQUENCY -> medFrequency
        public static final String COLUMN_FREQUENCY = "medFrequency";
        // COLUMN_START_DATE -> startDate
        public static final String COLUMN_START_DATE = "startDate";
        // COLUMN_END_DATE -> endDate
        public static final String COLUMN_END_DATE = "endDate";

    };
}
