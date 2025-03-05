package com.example.FitnessTracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class BmiActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editText1;
    private EditText editText2;
    private Button button;
    private TextView textView;
    private  Snackbar snackbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        toolbar = findViewById(R.id.toolbar);
        editText1=findViewById(R.id.weight);
       editText2=findViewById(R.id.height);
       button=findViewById(R.id.button2);
       textView=findViewById(R.id.BMI_answer);
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    double weight = Double.parseDouble(editText1.getText().toString());
                    double height = (Double.parseDouble(editText2.getText().toString())) / 100;
                    double ans = weight / (height * height);
                    String message = " ";
                    if (ans < 16) {
                        message = "According to your BMI, you fit in the Severe Thinness class. It's not healthy. You should improve your diet and lifestyle for a healthy BMI.";
                    } else if (ans >= 16 && ans < 17) {
                        message = "According to your BMI, you fit in the Moderate Thinness class. It's time to focus on improving your diet and nutrition.";
                    } else if (ans >= 17 && ans < 18.5) {
                        message = "According to your BMI, you fit in the Mild Thinness class. You may need to gain some weight for better health.";
                    } else if (ans >= 18.5 && ans < 25) {
                        message = "According to your BMI, you fit in the Normal class. Great job! You're maintaining a healthy BMI.";
                    } else if (ans >= 25 && ans < 30) {
                        message = "According to your BMI, you fit in the Overweight class. Consider focusing on a balanced diet and regular exercise.";
                    } else if (ans >= 30 && ans < 35) {
                        message = "According to your BMI, you fit in Obese Class I. It's important to focus on weight loss for better health.";
                    } else if (ans >= 35 && ans < 40) {
                        message = "According to your BMI, you fit in Obese Class II. You should consult with a healthcare provider for a weight loss plan.";
                    } else if (ans >= 40) {
                        message = "According to your BMI, you fit in Obese Class III. This is a serious health condition. Consult a healthcare professional immediately.";
                    }
//                    Toast.makeText(BmiActivity.this, message, Toast.LENGTH_LONG).show();


//                    new AlertDialog.Builder(BmiActivity.this).setTitle("BMI classification").setMessage(message).setPositiveButton("OK",null).show();
                    textView.setText(" BMI =  " + String.format("%.2f",ans)+"\n\n"+message);
                }
                catch (Exception e)
                {
                    Toast.makeText(BmiActivity.this, "Please Enter a valid input", Toast.LENGTH_SHORT).show();
                }
            }

        });

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