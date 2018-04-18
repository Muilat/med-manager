package com.muilat.android.med_manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.muilat.android.med_manager.data.MedicationContract;

public class MainActivity  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int RC_SIGN_IN = 100;
    Button signInButton;

    private static final int MEDICATION_LOADER_ID = 100;

    private static final String TAG = MainActivity.class.getSimpleName();



    private MedicationCursorAdapter mAdapter;
    Cursor mCursor;

    ListView listView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if(auth.getCurrentUser() == null){
//            // Start signed in activity
////            startActivity(SignedInActivity.createIntent(this, null));
////            finish();
//
//            List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
//            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
//            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
//            startActivityForResult(
//                    AuthUI.getInstance().createSignInIntentBuilder()
//                            .setIsSmartLockEnabled(true)
//                            .setProviders(selectedProviders)
//                            .build(),
//                    RC_SIGN_IN);
//        }
//        else {
            getSupportLoaderManager().initLoader(MEDICATION_LOADER_ID, null, this);

            listView = (ListView) findViewById(R.id.list_item);
            mAdapter = new MedicationCursorAdapter(this, mCursor);
            listView.setAdapter(mAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MedList.this, MedDetails.class);
//                startActivity(intent);

                    Toast.makeText(MainActivity.this,id+"",Toast.LENGTH_LONG).show();;
                }
            });
//        }

//        signInButton = (Button) findViewById(R.id.signInButton);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
////                selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
////                selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
////                startActivityForResult(
////                        AuthUI.getInstance().createSignInIntentBuilder()
////                                .setIsSmartLockEnabled(true)
////                                .setProviders(selectedProviders)
////                                .build(),
////                        RC_SIGN_IN);
//            }
//        });

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
                startActivity(intent);
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);
            return;
        }
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        Toast toast;
        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startActivity(SignedInActivity.createIntent(this, response));
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                toast = Toast.makeText(this, "Sign in was cancelled!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
        toast.show();
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MEDICATION_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the medication data
            Cursor mMedicationData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMedicationData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMedicationData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MedicationContract.MedicationEntry.COLUMN_FREQUENCY);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMedicationData = data;
                mCursor = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
