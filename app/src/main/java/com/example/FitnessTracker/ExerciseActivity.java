package com.example.FitnessTracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ExerciseActivity extends AppCompatActivity {

    private Button button_for_jump;//created an object of button which is associated with jumping jack exercise animation
    private Button button_for_squats;//created an object of button which is associated with squats exercise animation
    private Button button_for_lunges;//created an object of button which is associated with lunges exercise animation
    private Button button_for_pushups;//created an object of button which is associated with push ups exercise animation
    private Button button_for_situps;//created an object of button which is associated with sit ups exercise animation
    private Button button_for_Tplanks;//created an object of button which is associated with T planks exercise animation
    private Button button_for_crunches;//created an object of button which is associated with crunches exercise animation
    private Button button_for_burpees;//created an object of button which is associated with burpees exercise animation
    private Button button_for_highknees;//created an object of button which is associated with high knees exercise animation
    private Button button_for_benchdips;//created an object of button which is associated with bench dips  exercise animation
    public static final String animationResId="com.example.FitnessTracker_animation_file";//created a key to pass the animation file and the string is the unique value of this key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        button_for_jump=findViewById(R.id.start_jumping);
        button_for_squats=findViewById(R.id.start_squat);
        button_for_lunges=findViewById(R.id.start_lunges);
        button_for_pushups=findViewById(R.id.start_pushups);
        button_for_situps=findViewById(R.id.start_situps);
        button_for_Tplanks=findViewById(R.id.start_Tplank);
        button_for_crunches=findViewById(R.id.start_crunches);
        button_for_burpees=findViewById(R.id.start_burpees);
        button_for_highknees=findViewById(R.id.start_highknees);
        button_for_benchdips=findViewById(R.id.start_Bench_Dips);

        Intent intent=new Intent(ExerciseActivity.this, Exercise_sectionActivity.class);
        button_for_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.jumping_jack);//passing the animation file which is jumping_jack you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });

        button_for_squats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.squats);//passing the animation file which is squats you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_lunges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.lunges);//passing the animation file which is lunges you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_pushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.push_ups);//passing the animation file which is push ups you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_situps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.sit_ups);//passing the animation file which is sit ups you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_Tplanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.t_planks);//passing the animation file which is T planks you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_crunches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.crunches);//passing the animation file which is crunches you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_burpees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.burpees);//passing the animation file which is burpees you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_highknees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.high_knees);//passing the animation file which is high knees  you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });
        button_for_benchdips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(animationResId,R.raw.bench_dips);//passing the animation file which is bench dips   you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });

    }
}