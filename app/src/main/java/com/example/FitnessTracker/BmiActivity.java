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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

public class BmiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextWeight, editTextHeight, editTextAge;
    private Button buttonCalculate, buttonReset;
    private TextView textViewResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        // Find views by ID
        toolbar = findViewById(R.id.toolbar);
        editTextWeight = findViewById(R.id.weight);
        editTextHeight = findViewById(R.id.height);
        editTextAge = findViewById(R.id.age);
        buttonCalculate = findViewById(R.id.button2);
        buttonReset = findViewById(R.id.button_reset);
        textViewResult = findViewById(R.id.BMI_answer);

        // Setup toolbar as action bar with back arrow colored black
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            Drawable upArrow = AppCompatResources.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }

        // Calculate BMI on button click
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Parse inputs
                    double weight = Double.parseDouble(editTextWeight.getText().toString());
                    double height = Double.parseDouble(editTextHeight.getText().toString()) / 100.0; // convert cm to m
                    String ageText = editTextAge.getText().toString();

                    if (ageText.isEmpty()) {
                        Toast.makeText(BmiActivity.this, "Please enter your age", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int age = Integer.parseInt(ageText);

                    // Validate inputs
                    if (age <= 0 || age > 120) {
                        Toast.makeText(BmiActivity.this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (weight <= 0 || height <= 0) {
                        Toast.makeText(BmiActivity.this, "Please enter positive values for weight and height", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Calculate BMI
                    double bmi = weight / (height * height);

                    // Get BMI category message
                    String message = getBmiCategoryMessage(bmi);

                    // Add age-specific notes
                    if (age < 18) {
                        message += "\nNote: BMI for children and teens differs. Consult a pediatrician.";
                    } else if (age > 65) {
                        message += "\nNote: For older adults, maintain a healthy lifestyle and consult a doctor.";
                    }

                    // Display result
                    textViewResult.setText("BMI = " + String.format("%.2f", bmi) + "\n\n" + message);

                } catch (NumberFormatException e) {
                    Toast.makeText(BmiActivity.this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Reset inputs and result on button click
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextWeight.setText("");
                editTextHeight.setText("");
                editTextAge.setText("");
                textViewResult.setText("Your BMI result will appear here.");
            }
        });
    }

    // Returns BMI category message based on BMI value
    private String getBmiCategoryMessage(double bmi) {
        if (bmi < 16) {
            return "Severe Thinness: Unhealthy, improve your diet and lifestyle.";
        } else if (bmi >= 16 && bmi < 17) {
            return "Moderate Thinness: Focus on improving your nutrition.";
        } else if (bmi >= 17 && bmi < 18.5) {
            return "Mild Thinness: Consider gaining some weight for better health.";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Normal: Great job! You have a healthy BMI.";
        } else if (bmi >= 25 && bmi < 30) {
            return "Overweight: Focus on a balanced diet and regular exercise.";
        } else if (bmi >= 30 && bmi < 35) {
            return "Obese Class I: Important to focus on weight loss.";
        } else if (bmi >= 35 && bmi < 40) {
            return "Obese Class II: Consult a healthcare provider for a weight loss plan.";
        } else {
            return "Obese Class III: Serious health condition. Seek professional help immediately.";
        }
    }

    // Handle toolbar back button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
