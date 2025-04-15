package com.example.FitnessTracker;

// Import necessary Android and Google Fit libraries
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class SensorActivity extends AppCompatActivity {

    // Constants for Google Fit permissions and logging
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private static final String TAG = "SensorActivity";

//    UI elements
    private TextView stepstaken;
    private Toolbar toolbar;

    // Google Fit related objects
    private FitnessOptions fitnessOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Initialize UI elements
        toolbar = findViewById(R.id.toolbar3);
        stepstaken = findViewById(R.id.steptaken);
        stepstaken.setText("0"); // Set initial step count to 0

        // Set initial step count to 0
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set up Google Fit permissions for step counting
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        // Check if we have necessary permissions
        checkGoogleFitPermissions();
    }

    // Check if we have Google Fit permissions, if not request them
    private void checkGoogleFitPermissions() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            signInToGoogle();
        } else if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }
    }

    // Start Google Sign-In process
    private void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE);
    }

    // Handle the result of Google Sign-In
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                accessGoogleFit();
            } else {
                Toast.makeText(this, "Google Fit permissions required to track steps", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Access Google Fit data to get step count
    private void accessGoogleFit() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast.makeText(this, "Google Sign in required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Read today's step count
        Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        long total = dataSet.isEmpty()
                                ? 0
                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        updateStepCounter((int) total);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to read daily steps", e);
                        Toast.makeText(SensorActivity.this, "Failed to read step count", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Update the UI with new step count
    private void updateStepCounter(int steps) {
        if (stepstaken != null) {
            stepstaken.setText(String.format("Steps today: %d", steps));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Handle back button press // Refresh step count when activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        accessGoogleFit();
    }
}