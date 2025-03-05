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
    public static final String animationResId="com.example.FitnessTracker_animation_file";//created a key to pass the animation file and the string is the unique value of this key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        button_for_jump=findViewById(R.id.start_jumping);
        button_for_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExerciseActivity.this, Exercise_sectionActivity.class);
                intent.putExtra(animationResId,R.raw.jumping_jack);//passing the animation file which is jumping_jack you can find this file in the raw folder
                //here the animationResId is used as a key to pass the file to the Exercise_sectionActivity.java file a key with a unique string value
                startActivity(intent);
            }
        });




    }
}