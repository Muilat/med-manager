package com.muilat.android.medication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.muilat.android.medication.data.MedicationContract;
import com.muilat.android.medication.data.MedicationDbHelper;

public class AddMedicationActivity extends AppCompatActivity {

   private SQLiteDatabase mDb;

    EditText mNewMedicationNameEditText , mNewMedicationDescriptionEditText, mNewFrequencyEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        mNewMedicationNameEditText = (EditText)findViewById(R.id.med_name_edit_text);
        mNewMedicationDescriptionEditText = (EditText)findViewById(R.id.med_description_edit_text);
        mNewFrequencyEditText = (EditText)findViewById(R.id.frequency_edit_text);


        // Create a DB helper (this will create the DB if run for the first time)
        MedicationDbHelper dbHelper = new MedicationDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();



    }



    /**
     * This method is called when user clicks on the Save Medication button
     *
     * @param view The calling view (button)
     */
    public void saveMedication(View view) {
        int frequency = 5;
        if (mNewMedicationNameEditText.getText().length() == 0 ||
                mNewMedicationDescriptionEditText.getText().length() == 0 ||
                    mNewFrequencyEditText.getText().length() == 0) {
            return;
        }

        try {
            //mNewFrequencyEditText inputType="number", so this should always work
            frequency = Integer.parseInt(mNewFrequencyEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e("MainActivity", "Failed to parse frequency text to number: " + ex.getMessage());
        }

       addNewMedication(mNewMedicationNameEditText.getText().toString(), mNewMedicationDescriptionEditText.getText().toString(), frequency);

//        Toast.makeText(this,slt+" added to medication",Toast.LENGTH_LONG).show();

    }



    public  void addNewMedication(String medName, String medDescription,  int frequency){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_MED_NAME,medName);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_MED_DESCRIPTION,medDescription);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_FREQUENCY,frequency);

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MedicationContract.MedicationEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        finish();
    }


}