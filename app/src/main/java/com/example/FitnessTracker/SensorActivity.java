package com.example.FitnessTracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private Toolbar toolbar;
    private Sensor sensor; // This variable stores the step counter sensor.
    private SensorManager sensorManager; // This manages all sensors in the device.
    private SensorEventListener sensorEventListener;
    private TextView stepstaken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        toolbar=findViewById(R.id.toolbar3);
        stepstaken=findViewById(R.id.steptaken);
        stepstaken.setText("1000");

        //creating the back arrow to go back in the previous activity of the app
        setSupportActionBar(toolbar);// Set the Toolbar as the ActionBar for the activity
        if (getSupportActionBar()!=null)// Check if the ActionBar is available (it could be null in some cases)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Enable the back arrow button in the ActionBar
            getSupportActionBar().setDisplayShowHomeEnabled(true);// Ensure that the home (back) button will be visible


            Drawable upArrow = AppCompatResources.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);// Get the default back arrow drawable from AppCompat resources
            if (upArrow != null)// If the upArrow drawable is successfully loaded
            {
                upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);// Change the color of the back arrow to black
                /*
     PorterDuff.Mode.SRC_ATOP means:
    - The black color will be applied only to the non-transparent areas of the image (upArrow).
    - The transparent parts of the image will remain unaffected.
     - The original image's transparency is preserved while filling the visible parts with black color.

                 */
                getSupportActionBar().setHomeAsUpIndicator(upArrow);// Set the back arrow as the indicator for the home button in the ActionBar
            }
        }

        // writing code for sensor
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE); // Get access to the device's sensor service.
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);  // Get the step counter sensor from the device.

        }

    @Override
    // This method handles the item clicks in the ActionBar (like the back button)
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home) // Check if the clicked item is the home (back) button
        {
            getOnBackPressedDispatcher().onBackPressed(); // Handle the back button press by finishing the current activity
            return true;// Return true to indicate the action has been handled
        }
        return super.onOptionsItemSelected(item);// If the item is not the back button, call the default implementation
    }

    @Override
    protected void onResume() {
        //onResume(): This method runs when the user opens the app or comes back to it.
        super.onResume();

        if (sensorManager==null) // Check if SensorManager is available.
        {
            Toast.makeText(this, "Sensormanager is not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sensor==null) // Check if the step counter sensor is available.
        {
            Toast.makeText(this, "Sensor not found !", Toast.LENGTH_SHORT).show();
            Log.e("mytag","Sensor is not available");
        }

        else {
            Log.d("tag", "Step counter sensor is available");
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL); // Start listening for step count updates.
            //This tells the phone: "Hey, let me know whenever the step count changes.
            //SENSOR_DELAY_NORMAL means we don't need updates too frequently—normal speed is fine.
        }
    }


    @Override
    protected void onPause(){
        //onPause(): This runs when the user leaves the app (e.g., presses home or switches apps)
        super.onPause();
        if(sensorManager!=null)
        {
            sensorManager.unregisterListener(this);// Stop listening when the app is in the background.
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //int steps= (int) event.values[0];
        int steps=1000;
//        if (stepstaken!=null) {
//            stepstaken.setText(String.valueOf(steps));
//        }
//        else {
//            Log.e("sensoractivity", "Stepstaken is null");
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //This method is needed for the interface (SensorEventListener), but step counters don't require accuracy checks.

    }
}