package com.example.FitnessTracker;

import static com.example.FitnessTracker.ExerciseActivity.animationResId;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.lang.reflect.Field;

public class Exercise_sectionActivity extends AppCompatActivity {
    private LottieAnimationView lottieview;// created a varriable of LottieAnimationView
    private NumberPicker hourpicker; // created a varriable of NumberPicker
    private NumberPicker minutepicker;// created a varriable of NumberPicker
    private NumberPicker secondpicker;// created a varriable of NumberPicker
    private Button startbutton;// created a varriable of Button
    private CountDownTimer countDownTimer;//created a varriable of CountDownTimer
    private long timeinMillis , remainingTimeMillis;// created a long varriable
    private boolean isTimerRunning=false, isPaused=false;//created a boolean flag


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_section);
        lottieview=findViewById(R.id.lottieview);
        hourpicker=findViewById(R.id.numhour);
        minutepicker=findViewById(R.id.numminute);
        secondpicker=findViewById(R.id.numsecond);
        startbutton=findViewById(R.id.button);

        hourpicker.setMinValue(0);// the hourspicker's minimum value is set to 0
        hourpicker.setMaxValue(23);// the hourspicker's maximum value is set to 23 as there is is 24 hours in a day
        minutepicker.setMinValue(0);// the minutepicker's minimum value is set to 0
        minutepicker.setMaxValue(59);//the minutepicker's maximum value is set to 59 as 1 hour =60 minute
        secondpicker.setMinValue(0);// the secondpicker's minimum value is set to 0
        secondpicker.setMaxValue(59);// the secondpicker's maximum value is et to 59 as 1 minute = 60 second

        Intent intent =new Intent();
        int animationId=getIntent().getIntExtra(animationResId, -1);//getting the message passed through the intent in this case it is a animation file
        //-1 argument has also taken for safety suppose for some reason the animation file is not passed then the app can be crashed for that we will use -1
        //by default if an animation file is correctly passed it will by default take a positive value but if the file is not correctly passed then the id will be -1


        if (animationId!=-1) // here we are checking if the id is not -1 because if it is -1 then  it means the file is not correctly passed and the app can crash that's why we used if else here
        {
           lottieview.setAnimation(animationId);//set the animation in the lottieAnimationView
           lottieview.playAnimation();//plays the animation
        }

        startbutton.setOnClickListener(v ->{ // when the button for timer is clicked
            if (isTimerRunning) // checking if a timer is already running using the boolean varriable isTimerRunning which is already set to false
                // if the condition is false means already a timer is running if true means no timer is running we checked using if statement because if
                // a timer is already running and then the user click the start button to start a timer in the middle of a timer the app may be  crashed
            {
               // startTimer();// called the starttimer method which we have created below
                pauseTimer();
            } else if (isPaused)
            {
                resumeTimer();
            }
            else
            {
                startTimer();
            }
        });

    }

    private void startTimer()
    {
        int hour=hourpicker.getValue();// getting the value of hourspicker set by the user
        int minute=minutepicker.getValue();// similarly getting the value of minutepicker set by the user
        int second=secondpicker.getValue();// similarly getting the value of secondpicker set by the user

        timeinMillis=((hour*3600) + (minute*60) +second)*1000; // converted the whole time set by  user in miliseconds  we multiplied the hour with the 3600 since 1 hour=3600 second
        // then multiplied minute with 60 since 1 minute=60 second then we multiplied then we added the all the seconds and then divided it by 1000 since 1 hour=1000milisecond
        //now the whole time is converted into milliseconds
        if (timeinMillis==0) // checking if the whole time is 0 because if it is zero then there is no sense to start thr timer
        {
            return;// that's why the method is returning null here
        }
        else
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // this line keeps the screen on until the timer is finished because if the screen off at during the countdown it will give a bad user experience

            hourpicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why we have setEnabled false of the hourpicker
            minutepicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why similarly we have setEnabled false of the minutepicker
            secondpicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why  similarly we have setEnabled false of the secondpicker

           countDownTimer=new CountDownTimer(timeinMillis, 1000) { // created an object of countDownTimer which takes 2 argument first one is the whole duration or the timer set by user in milliseconds
               // 2nd argument is the interval here it is 1000 millisecond means after each milliseconds the countdown will be updated as 1 second=1000millisecond
               @Override
               public void onTick(long millisUntilFinished) {// millisUntilFinished is an attribute which give the remaining time of the countdown for example if timer  is set for 2 minutes then after 40 second the remaining time is 1 minute 20 seconds
                   long hours=(millisUntilFinished/3600000); // divided the millisUntilFinished  by 3600000 to get total hours since 1 hour = 3600000 milliseconds
                   long minutes=(millisUntilFinished/60000) %60;// divided the millisUntilFinished by  60000, then % 60 to get remaining minutes after full hours. since 1 minute= 60000 millisecond
                   long seconds=(millisUntilFinished/1000) %60;// divided the millisUntilFinished by  1000, then % 60 to get remaining seconds after full minutes. since 1 second= 1000 millisecond
                   hourpicker.setValue((int)hours); // updating the hourpicker with the value of hours after each second as 1000 miliseconds interval has already applied by taking 1000 as the second argument
                   minutepicker.setValue((int)minutes);// similarly updating the minutepicker with the value of minutes after each second
                   secondpicker.setValue((int)seconds);// similarly updating the secondpicker with the value of seconds after each second
               }

               @Override
               public void onFinish() { // this method is applied after the timer is finished

                   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//this line clears the flag so that the screen can naturally off after the timer is finished

                   hourpicker.setValue(0);// again set the value of hourpicker to 0 so that the user can again set thr timer according to their wish
                   minutepicker.setValue(0);// similarly again set the value of minutepicker to 0 so that the user can again set thr timer according to their wish
                   secondpicker.setValue(0);// again set the value of secondpicker to 0 so that the user can again set thr timer according to their wish

                   hourpicker.setEnabled(true);// setEnabled is set to true for hours this will help the user to agian set the timer
                   minutepicker.setEnabled(true);// similarly setEnabled is set to true  for minute this will help the user to agian set the timer
                   secondpicker.setEnabled(true);// similarly setEnabled is set to true  for second this will help the user to agian set the timer
                   isTimerRunning=false; // the flag is set to false again so that in the if statement checking before setting timer again then it is false so that the user start the timer again

               }
           }.start(); //at last this start method is called to start this whole starttimer method

           isTimerRunning=true;// again it is set to true as timer has started after the starttimer method has started by using start method at last
        }
    }

    private void pauseTimer()
    {
        if (countDownTimer!=null) {
            countDownTimer.cancel();
        }
        isTimerRunning=false;
        isPaused=true;
        startbutton.setText("Resume");
    }

    private void resumeTimer()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        countDownTimer =new CountDownTimer(remainingTimeMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis=millisUntilFinished;
                long hours=(millisUntilFinished/3600000); // divided the millisUntilFinished  by 3600000 to get total hours since 1 hour = 3600000 milliseconds
                long minutes=(millisUntilFinished/60000) %60;// divided the millisUntilFinished by  60000, then % 60 to get remaining minutes after full hours. since 1 minute= 60000 millisecond
                long seconds=(millisUntilFinished/1000) %60;// divided the millisUntilFinished by  1000, then % 60 to get remaining seconds after full minutes. since 1 second= 1000 millisecond
                hourpicker.setValue((int)hours); // updating the hourpicker with the value of hours after each second as 1000 miliseconds interval has already applied by taking 1000 as the second argument
                minutepicker.setValue((int)minutes);// similarly updating the minutepicker with the value of minutes after each second
                secondpicker.setValue((int)seconds);// similarly updating the secondpicker with the value of seconds after each second

            }

            @Override
            public void onFinish() {

                resetTimer();
//                startbutton.setText("Start");
            }
        }.start();
        startbutton.setText("Pause");
        isTimerRunning=true;
        isPaused=false;
    }

    private void resetTimer()
    {
        if (countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hourpicker.setValue(0);
        minutepicker.setValue(0);
        secondpicker.setValue(0);

        hourpicker.setEnabled(true);
        minutepicker.setEnabled(true);
        secondpicker.setEnabled(true);
        isTimerRunning=false;
       isPaused=false;
       remainingTimeMillis=0;
       startbutton.setText("Start");
    }

}