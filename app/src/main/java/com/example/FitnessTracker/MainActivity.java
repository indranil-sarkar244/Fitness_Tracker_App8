package com.example.FitnessTracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new  Handler(Looper.getMainLooper()).postDelayed(new Runnable() { //Handler as a tool that helps run some code after a delay.  Handler is used to schedule tasks to be executed on a specific thread.
            // Looper.getMainLooper() makes sure this happens on the main UI thread (so the app doesn’t freeze).
            // postDelayed : This tells the app: "Wait for 3 seconds, then do something."
            // 10000 means 3000 milliseconds, which is 10 seconds.
            @Override
            public void run() { //The run() method runs.
                //A new Intent is created to switch from MainActivity to MainActivity2.
                Intent intent =new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                finish();//finish(); closes MainActivity, so when the user presses the back button, they won't return to it.
            }
        }, 5000);
    }
}