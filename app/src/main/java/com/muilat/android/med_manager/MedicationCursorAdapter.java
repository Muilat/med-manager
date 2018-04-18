package com.muilat.android.med_manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muilat.android.med_manager.data.MedicationContract;

/**
 * Created by my computer on 17-Apr-18.
 */


public class MedicationCursorAdapter extends CursorAdapter {

    public MedicationCursorAdapter(Activity context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.medication_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Determine the values of the wanted data
        String med_name = mCursor.getString(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_MED_NAME));
        String med_description = mCursor.getString(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_MED_DESCRIPTION));
        int med_frequency = mCursor.getInt(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_FREQUENCY));
        String start_date = mCursor.getString(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_START_DATE));
        String end_date = mCursor.getString(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_END_DATE));
        String next_timing = mCursor.getString(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_NEXT_TIMING));
        long id =  mCursor.getInt(mCursor.getColumnIndex(MedicationContract.MedicationEntry.COLUMN_FREQUENCY));


        TextView medNameTextView = (TextView) view.findViewById(R.id.med_name_text_view);
        TextView medDescriptionTextView = (TextView) view.findViewById(R.id.med_description_text_view);
//            medFrequencyTextView = (TextView) view.findViewById(R.id.med_frequency_text_view);
        TextView    startDateTextView = (TextView) view.findViewById(R.id.startdate);
        TextView    endDateTextView = (TextView) view.findViewById(R.id.endtdate);
            TextView nextTimingTextView = (TextView) view.findViewById(R.id.nexttiming);


        // Display the medication name
        medNameTextView.setText(med_name);
        // Display the medication description
        medDescriptionTextView.setText(med_description);
//        // Display the medication start date
//        startDateTextView.setText(start_date);
        // Display the medication ebd date
//        endDateTextView.setText(end_date);
        nextTimingTextView.setText(next_timing);
        // Display the medication frequency
//        medFrequencyTextView.setText(med_frequency);



    }

}