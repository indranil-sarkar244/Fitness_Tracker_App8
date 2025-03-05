package com.example.FitnessTracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity2 extends AppCompatActivity {
    ImageView imageViewbmi;
    private LottieAnimationView steps;
 private LottieAnimationView bmi;
 private LottieAnimationView exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       steps=findViewById(R.id.step);
        bmi=findViewById(R.id.jumping_jack);
        exercise=findViewById(R.id.Exercise);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity2.this, BmiActivity.class);
               startActivity(intent);
               }
       });

        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity2.this, SensorActivity.class);
               startActivity(intent);
               }
       });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this, ExerciseActivity.class);
                startActivity(intent);
            }
        });



    }
}