package com.example.FitnessTracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BmiActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);// Set the Toolbar as the ActionBar for the activity
        if (getSupportActionBar() != null)// Check if the ActionBar is available (it could be null in some cases)
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
    }

    @Override
    // This method handles the item clicks in the ActionBar (like the back button)
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Check if the clicked item is the home (back) button
        {
            getOnBackPressedDispatcher().onBackPressed(); // Handle the back button press by finishing the current activity
            return true;// Return true to indicate the action has been handled
        }
        return super.onOptionsItemSelected(item);// If the item is not the back button, call the default implementation
    }
}