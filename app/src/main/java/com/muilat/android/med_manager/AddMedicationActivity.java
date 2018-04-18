package com.muilat.android.med_manager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.muilat.android.med_manager.data.MedicationContract;
import com.muilat.android.med_manager.data.MedicationDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;

public class AddMedicationActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;

    EditText mNewMedicationNameEditText , mNewMedicationDescriptionEditText, mNewFrequencyEditText;
    EditText mNewStartdateEditText, mNewEnddateEditText, mNewStarttimeEditText;
    String startdate, enddate, starttime= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);



        mNewMedicationNameEditText = (EditText)findViewById(R.id.med_name_edit_text);
        mNewMedicationDescriptionEditText = (EditText)findViewById(R.id.med_description_edit_text);
        mNewFrequencyEditText = (EditText)findViewById(R.id.frequency_edit_text);
        mNewStartdateEditText = (EditText)findViewById(R.id.startdate_edit_text);
        mNewStarttimeEditText = (EditText)findViewById(R.id.starttime_edit_text);
        mNewEnddateEditText = (EditText)findViewById(R.id.enddate_edit_text);

        mNewStartdateEditText.setText("");
        mNewStarttimeEditText.setText("");
        mNewEnddateEditText.setText("");


        ////////format stsrt date and end date
        mNewStartdateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = mNewStartdateEditText.getText().toString();
                int len = data.length();
                switch (len){
//                    case 1:
//                        mNewStartdateEditText.setText("0"+s.toString());
                    case 2:
                        mNewStartdateEditText.append("/");
                        break;
                    case 5:
                        mNewStartdateEditText.append("/");
                        break;
                    case 10:
//                        mNewStartdateEditText.setText(s.toString()+"/");
                        startdate = data.replace("/",".");
                        break;

                    default:
                        break;
                        //

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewEnddateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = mNewEnddateEditText.getText().toString();
                int len = data.length();
                switch (len){
                    case 2:
                        mNewEnddateEditText.append("/");
                        break;
                    case 5:
                        mNewEnddateEditText.append("/");
                        break;
                    case 10:
//                        mNewEnddateEditText.setText(s.toString()+"/");
                        enddate = data.replace("/",".");

                        break;

                    default:
                        break;
                    //

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewStarttimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = mNewStarttimeEditText.getText().toString();
                int len = data.length();
                switch (len){
//                    case 1:
//                        mNewStarttimeEditText.setText("0"+s.toString());
                    case 2:
                        mNewStarttimeEditText.append(":");
//                        starttime = s.toString()+" ";
                        break;
                    case 5:
                        starttime = data;
                        break;

                    default:
                        break;
                    //

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        int frequency = 5;//default/minimal frequency
        long mStartDate = 0, mEndDate = 0;

        if(starttime.length() != 5)
            starttime = " 00:00";

        if (mNewMedicationNameEditText.getText().length() == 0 ||
                mNewMedicationDescriptionEditText.getText().length() == 0 ||
                mNewFrequencyEditText.getText().length() == 0 ||
                startdate.length() != 10 ||

                enddate.length() != 10
                ) {
            return;
        }

        try {
            //mNewFrequencyEditText inputType="number", so this should always work
            frequency = parseInt(mNewFrequencyEditText.getText().toString());

            mStartDate = formatDate(startdate,starttime);
            mEndDate  = formatDate(enddate,"");
        } catch (NumberFormatException ex) {
            Log.e("MainActivity", "Failed to parse frequency text to number: " + ex.getMessage());
        }

        addNewMedication(mNewMedicationNameEditText.getText().toString(), mNewMedicationDescriptionEditText.getText().toString(), frequency, mStartDate, mEndDate);

//        Toast.makeText(this,slt+" added to medication",Toast.LENGTH_LONG).show();

    }

    public long formatDate(String mdate, String mTime){
        if (mTime ==""){
            mTime = "23:59";
        }

        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        f.setLenient(false);
        try {
            Date ddd = f.parse(mdate +", "+ mTime);
             return  ddd.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public  void addNewMedication(String medName, String medDescription,  int frequency, long mStartDate, long mEndDate){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_MED_NAME,medName);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_MED_DESCRIPTION,medDescription);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_FREQUENCY,frequency);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_START_DATE,mStartDate);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_END_DATE,mEndDate);
        contentValues.put(MedicationContract.MedicationEntry.COLUMN_NEXT_TIMING,mStartDate+(int) (TimeUnit.HOURS.toSeconds(frequency)));

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