package com.example.FitnessTracker;

// Import necessary Android and Google Fit libraries
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class SensorActivity extends AppCompatActivity {

    // Request code for Google Fit permissions
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;

    // Tag for logging
    private static final String TAG = "SensorActivity";

    // UI Elements
    private TextView stepstaken; // Text view to show step count
    private TextView caloriesBurned; // Text view to show calories burned
    private Toolbar toolbar;         // App toolbar
    private CircularProgressBar circularProgressBar; // Circular progress bar for animated steps

    // Target step goal for the progress bar (can be changed dynamically)
    private final int maxSteps = 5000;

    // Google Fit API variables
    private FitnessOptions fitnessOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Link UI components with layout
        toolbar = findViewById(R.id.toolbar3);
        stepstaken = findViewById(R.id.steptaken);
        caloriesBurned = findViewById(R.id.caloriesBurned); //link calories textview from layout
        circularProgressBar = findViewById(R.id.circularProgressBar);

        // Set initial value
        stepstaken.setText("0");
        caloriesBurned.setText("Calories: 0 kcal"); //calories intial value

        // Setup circular progress bar maximum steps
        circularProgressBar.setProgressMax(maxSteps);
        circularProgressBar.setProgress(0); // start from zero


        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // Back arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Ask permission to read step data from Google Fit
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ) //Add calories burned
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();

        // Check permissions and proceed
        checkGoogleFitPermissions();
    }

    // Check if user has granted permission to Google Fit
    private void checkGoogleFitPermissions() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            // If not signed in, initiate sign-in
            signInToGoogle();
        } else if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            // If not granted fitness permissions, request them
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            // If already signed in and permissions granted, fetch steps
            accessGoogleFit();
        }
    }

    // Launch Google Sign-In activity
    private void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE);
    }

    // Handle result of permission/sign-in request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Permission granted, access step data
                accessGoogleFit();
            } else {
                // User denied permission
                Toast.makeText(this, "Google Fit permissions required to track steps", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Request daily step count from Google Fit
    private void accessGoogleFit() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            Toast.makeText(this, "Google Sign in required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch total steps taken today
        Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        int totalSteps = dataSet.isEmpty()
                                ? 0
                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                        // Update the UI with animation
                        updateStepCounter(totalSteps);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to read daily steps", e);
                        Toast.makeText(SensorActivity.this, "Failed to read step count", Toast.LENGTH_SHORT).show();
                    }
                });
        // Fetch today's calories burned
        Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        float totalCalories = dataSet.isEmpty()
                                ? 0
                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
                        updateCaloriesCounter(totalCalories);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to read calories", e);
                        Toast.makeText(SensorActivity.this, "Failed to read calorie data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Update text and circular progress bar with animation
    private void updateStepCounter(int steps) {
        if (stepstaken != null && circularProgressBar != null) {

            // Create animation from 0 to actual step value
            ValueAnimator animator = ValueAnimator.ofInt(0, steps);
            animator.setDuration(2500); // 2.5 second duration
            animator.setInterpolator(new DecelerateInterpolator());

            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();

                // Update circular progress bar and text view during animation
                circularProgressBar.setProgressWithAnimation((float) animatedValue, (long) 200);

                stepstaken.setText(animatedValue + "");
            });

            // Start animation
            animator.start();
        }
    }
    // Update calories burned
    private void updateCaloriesCounter(float calories) {
        if (caloriesBurned != null) {
            caloriesBurned.setText(String.format("Calories: %.1f kcal", calories));
        }
    }

    // Handle back arrow press from toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Refresh data when returning to this activity
    @Override
    protected void onResume() {
        super.onResume();
        accessGoogleFit(); // Refresh step data
    }
}
