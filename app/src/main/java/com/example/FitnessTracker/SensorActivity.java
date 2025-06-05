package com.example.FitnessTracker;

// Import necessary Android and Google Fit libraries
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class SensorActivity extends AppCompatActivity {

    // Request code for Google Fit permissions
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;

    // Tag for logging
    private static final String TAG = "SensorActivity";

    // UI Elements
    private TextView stepstaken; // Text view to show step count
    private TextView caloriesBurned; // Text view to show calories burned
    private TextView distanceWalked;   // Text view to show the total distance walked
    private TextView heartPoint;// Text view to show heart points
    private TextView desCription;
    private Toolbar toolbar;         // App toolbar
    private CircularProgressBar circularProgressBar;// Circular progress bar for animated steps
    private CircularProgressBar progressBar;
    private boolean IsstepReached=false;
    private boolean IsHeartpointReached=false;
    private View rootview;

    // Target step goal for the progress bar (can be changed dynamically)
    private final int maxSteps = 5000;
    private final int maxheartpoints=25;

    // Google Fit API variables
    private FitnessOptions fitnessOptions;
    private GoogleSignInClient googleSignInClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Link UI components with layout
        toolbar = findViewById(R.id.toolbar3);
        stepstaken = findViewById(R.id.steptaken); //link steps textview from layout
        caloriesBurned = findViewById(R.id.caloriesBurned); //link calories textview from layout
        distanceWalked = findViewById(R.id.distanceWalked); //link distance walked textview from layout

        heartPoint = findViewById(R.id.heartPoint); //link heart points textview from layout
        desCription=findViewById(R.id.heartpointdescrip);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        progressBar=findViewById(R.id.progressBar2);

        rootview=findViewById(R.id.main);

        // Set initial value
        stepstaken.setText("0");
        caloriesBurned.setText("Calories: 0 cal"); //calories intial value
        distanceWalked.setText("0 km"); //distance walk intial value

        heartPoint.setText("0 points"); //heart points intial value
        desCription.setText("According to WHO scoring 150 Heart Points a week can help you live longer, sleep better and boost your mood");

        // Setup circular progress bar maximum steps
        circularProgressBar.setProgressMax(maxSteps);
        progressBar.setProgressMax(maxheartpoints);
        circularProgressBar.setProgress(0); // start from zero
        progressBar.setProgress(0);


        // Setup toolbar
        setSupportActionBar(toolbar);//This sets the Toolbar as the app's ActionBar so you can customize it (like adding a back button, changing title, etc.).
        if (getSupportActionBar() != null) //Checks if the ActionBar is not null (i.e., was set successfully) before customizing it.
             {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Shows the back arrow (←) in the ActionBar, which allows users to navigate "up" (usually to the previous screen).
            getSupportActionBar().setDisplayShowHomeEnabled(true);//When you want to show the back/up arrow, Android needs to know that it's allowed to show the home-related icon area.
                 //So even though the back arrow is triggered by: setDisplayHomeAsUpEnabled(true); …it won’t appear visually unless:
                 //setDisplayShowHomeEnabled(true); is also set — because it allows the system to draw that space in the ActionBar.

            Drawable upArrow= AppCompatResources.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);//Loads the default back arrow icon (abc_ic_ab_back_material) as a Drawable from AppCompat resources.
            if (upArrow!=null) // Makes sure the back arrow drawable was loaded successfully (not null) before using it.
            {
                upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);//Changes the arrow color to black using a color filter. You can change this to white or any other color if needed.
                getSupportActionBar().setHomeAsUpIndicator(upArrow);//Sets your customized arrow (with color) as the back button icon in the ActionBar.
            }
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
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)    // ✅ Add Distance cover
                .addDataType(DataType.TYPE_HEART_POINTS, FitnessOptions.ACCESS_READ) //Add heart points
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

        // ✅ Get total distance walked today
        Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        float totalDistance = dataSet.isEmpty()
                                ? 0
                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();

                        updateDistanceCounter(totalDistance);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to read distance", e);
                    }
                });

        // ✅ Fetch today's Heart Points
        Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_HEART_POINTS)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        float totalHeartPoints= 0f;
                        if (!dataSet.isEmpty())
                        {
                           totalHeartPoints=dataSet.getDataPoints().get(0).getValue(dataSet.getDataPoints().get(0).getDataType().getFields().get(0)).asFloat();
                        }
                        updateHeartPoints((long)totalHeartPoints);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to read heart points", e);
                    }
                });


    }



    // Update text and circular progress bar with animation
    private void updateStepCounter(int steps) {
//        steps=18000;
//        final long sj=steps;
      if (stepstaken != null && circularProgressBar != null) {

            // Create animation from 0 to actual step value
            ValueAnimator animator = ValueAnimator.ofInt(0, steps);
            animator.setDuration(3000); // 2.5 second duration
            animator.setInterpolator(new DecelerateInterpolator());

            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();

                // Update circular progress bar and text view during animation
                circularProgressBar.setProgressWithAnimation((float) animatedValue, (long) 4000);

                stepstaken.setText("Steps: "+animatedValue + "");

                if (steps>4999 && !IsstepReached)
                {
                    IsstepReached=true;
                    ShowGoalMessage();
                }
            });

            // Start animation
            animator.start();
        }
    }
    // Update calories burned
    private void updateCaloriesCounter(float calories) {
        if (caloriesBurned != null) {
            caloriesBurned.setText(String.format("Calories: %.1f cal", calories));
        }
    }
    private void updateDistanceCounter(float distanceInMeters) {
        float distanceInKm = distanceInMeters / 1000f; // ✅ Convert meters to kilometers
        if (distanceWalked != null) {
            distanceWalked.setText(String.format("Distance: %.2f km", distanceInKm));
        }
    }
    private void updateHeartPoints(long heartPoints) {
//   heartPoints=2;
//   final long point =heartPoints;
        if (stepstaken != null && progressBar != null) {

            // Create animation from 0 to actual step value
            ValueAnimator animator = ValueAnimator.ofInt(0, (int)heartPoints);
            animator.setDuration(3000); // 2.5 second duration
            animator.setInterpolator(new DecelerateInterpolator());

            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();

                // Update circular progress bar and text view during animation
                progressBar.setProgressWithAnimation((int) animatedValue, (long) 4000);

                heartPoint.setText("Heart Point: "+animatedValue + "");

                if (heartPoints>24 && !IsHeartpointReached)
                {
                    IsHeartpointReached=true;
                    ShowGoalMessage();
                }
            });

            // Start animation
            animator.start();
        }
    }



    private void ShowGoalMessage()
    {
        if (IsstepReached && IsHeartpointReached)
        {
            Snackbar snackbar= Snackbar.make(rootview,"Well done you have achieved both your goals today",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v -> snackbar.dismiss());
            snackbar.show();
        }

        else if (IsstepReached)
        {
            Snackbar snackbar=Snackbar.make(rootview, "Well done you have achieved your today's step goal", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v -> snackbar.dismiss());
            snackbar.show();
        }
        else if (IsHeartpointReached)
        {
            Snackbar snackbar = Snackbar.make(rootview, "Well done you have achieved your today's heart point goal", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v -> snackbar.dismiss());
            snackbar.show();
        }

    }


    // Handle back arrow press from toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)   // This overrides the method to handle what happens when a menu item (including the back arrow) is clicked.
    {
        if (item.getItemId() == android.R.id.home)  //Checks if the item clicked is the back arrow (home/up button in ActionBar).
        {
            getOnBackPressedDispatcher().onBackPressed(); //Calls the back navigation behavior — same as pressing the physical back button.
            return true; //Tells the system: “Yes, we handled this event ourselves.”
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
