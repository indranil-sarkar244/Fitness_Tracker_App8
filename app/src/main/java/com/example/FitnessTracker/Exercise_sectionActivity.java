package com.example.FitnessTracker;

import static com.example.FitnessTracker.ExerciseActivity.animationResId;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
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
    private MediaPlayer  mediaPlayer, mediaPlayer1;
    private long timeinMillis , remainingTimeMillis=0;// created 2 long varriable firstone will store the whole time set for timer in millisecond and the second one wil be used while pause and resuming to count how much we have to count for the remaining time
    private boolean isTimerRunning=false, isPaused=false;//created 2  boolean flag one is for detecting is the countdown is continuing or not and the second one is for detecting if the timer is paused or not
   private  boolean NotClicked=true;


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
                pauseTimer(); // if the if statement is not  satisfied then it will call pausetimer because if a time or countdown is already running then the user will obviously may want to pause the timer
                //pausetimer is a method which we have created below
            } else if (isPaused)// checking if the time is paused using boolean varriable or flag isPaused
            {
                resumeTimer(); // if the else if statement is satisfied here it will call resumetimer method because if the else if statement is satisfied then it means that the user has paused the timer
                // and obviousely if the timer is paused for some reason the user will may want to resume it again to start it from where he stopped it.......> resumetimer is a method we have created below
            }
            else // if the if and also the else if statement is not satisfied then it means that the user has not started the timer yet
            {
                startTimer(); // that's why we will call here the startTimer  method for starting the timer again startTimer is a method which we have created below
            }
        });

    }

    private void startTimer() // here we have created or defined the startTimer method
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
            hourpicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why we have setEnabled false of the hourpicker
            minutepicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why similarly we have setEnabled false of the minutepicker
            secondpicker.setEnabled(false);// when the countdown has started the user can't set the timer until the timer is finished that's why  similarly we have setEnabled false of the secondpicker

           countDownTimer=new CountDownTimer(timeinMillis, 1000) { // created an object of countDownTimer which takes 2 argument first one is the whole duration or the timer set by user in milliseconds
               // 2nd argument is the interval here
               // it is 1000 millisecond means after each milliseconds the countdown will be updated as 1 second=1000millisecond......> //countDownTimer is a built in class used for setting timer
               @Override
               public void onTick(long millisUntilFinished) {// millisUntilFinished is an attribute which give the remaining time of the countdown for example if timer  is set for 2 minutes then after 40 second the remaining time is 1 minute 20 seconds
                   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // this line keeps the screen on until the timer is finished because if the screen off at during the countdown it will give a bad user experience
                   long hours=(millisUntilFinished/3600000); // divided the millisUntilFinished  by 3600000 to get total hours since 1 hour = 3600000 milliseconds
                   long minutes=(millisUntilFinished/60000) %60;// divided the millisUntilFinished by  60000, then % 60 to get remaining minutes after full hours. since 1 minute= 60000 millisecond
                   long seconds=(millisUntilFinished/1000) %60;// divided the millisUntilFinished by  1000, then % 60 to get remaining seconds after full minutes. since 1 second= 1000 millisecond
                   hourpicker.setValue((int)hours); // updating the hourpicker with the value of hours after each second as 1000 miliseconds interval has already applied by taking 1000 as the second argument
                   minutepicker.setValue((int)minutes);// similarly updating the minutepicker with the value of minutes after each second
                   secondpicker.setValue((int)seconds);// similarly updating the secondpicker with the value of seconds after each second
                   startbutton.setText("Pause");// set the text of start button to pause as when the user has started the timer he will not again start the timer untill the remaining one is finished but he may want to pause the timer for which we have set the text of button to pause for better user experience
               }

               @Override
               public void onFinish() { // this method is applied after the timer is finished or the program inside the onTick finished

                   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//this line clears the flag so that the screen can naturally off after the timer is finished

                   hourpicker.setValue(0);// again set the value of hourpicker to 0 so that the user can again set thr timer according to their wish
                   minutepicker.setValue(0);// similarly again set the value of minutepicker to 0 so that the user can again set thr timer according to their wish
                   secondpicker.setValue(0);// again set the value of secondpicker to 0 so that the user can again set thr timer according to their wish

                   hourpicker.setEnabled(true);// setEnabled is set to true for hours this will help the user to agian set the timer
                   minutepicker.setEnabled(true);// similarly setEnabled is set to true  for minute this will help the user to agian set the timer
                   secondpicker.setEnabled(true);// similarly setEnabled is set to true  for second this will help the user to agian set the timer
                   isTimerRunning=false;// the flag is set to false again so that in the if statement checking before setting timer again then it is false so that the user start the timer again
                   isPaused=false;
                   sendNotification();// calling this method to play an audio after finishing the timer
               }
           }.start(); //at last this start method is called to start this whole starttimer method

           isTimerRunning=true;// again it is set to true as timer has started after the starttimer method has started by using start method at last
        }
    }

    private void pauseTimer()// this is the pauseTimer method for acting the starbutton as pause button when started the timer
    {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (countDownTimer!=null) {//checking if there is any countdown is running or not if it is null the no countdown is running if its not null then the countdown is running
            countDownTimer.cancel(); // this line basically pause the timer by using built in method cancel()
            countDownTimer=null;// setting the countdown to null as timer is paused so setting the coundowntimer object null will help us to understand countdown is running now or not
        }
        remainingTimeMillis=(hourpicker.getValue()*3600+minutepicker.getValue()*60+secondpicker.getValue())*1000;//its a mathematical calculation we have done to store the remaining time to count after pausing the timer so that when resumed it can start counting from where it was stopped....> we have done similar mathematical calculation above at line 92 where it was fully explained
        startbutton.setText("Resume");// set the text of button as resume for better user experience as when timer is paused by the user he may want to resume the timer again
        isTimerRunning=false;//it is set to false as after pausing the time no timer is running
        isPaused=true;// it is set to true as this method will pause the timer

    }

    private void resumeTimer()
    {

        countDownTimer =new CountDownTimer(remainingTimeMillis,1000) { //again created an object of counttimer to start counting timer again after being resumed
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
                startTimer();
                isTimerRunning=false;
                isPaused=false;
                startbutton.setText("Start");
                hourpicker.setEnabled(true);//enabled each numberpickers true so that it can again take input after finishing one time
                minutepicker.setEnabled(true);
                secondpicker.setEnabled(true);
                countDownTimer=null;
                sendNotification();
            }
        }.start();
        startbutton.setText("Pause");
        isTimerRunning=true;
        isPaused=false;
    }

    private void sendNotification() // created this method to create sound once the timer is finished
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startbutton.setText("Done");
        if (mediaPlayer!=null) // checking if mediaplayer object already  have any audio or the previous audio which was sounded first time so that we can destroy that object
        {
            mediaPlayer.stop();// first we are stopping the audio of the mediaplayer object
            mediaPlayer.release();// this method will destroy the object's underlying resources just like we destroy our garbage
            mediaPlayer=null;// assigning mediaplayer to null so that when again it wil execute here it will automatically go to the else block
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else
        {
            mediaPlayer = MediaPlayer.create(Exercise_sectionActivity.this, R.raw.notify_timer_finish); // creating a new object of mediaplayerit takes 2 argument first is context which is  Exercise_sectionActivity.this and another is the path of the audio.....>notify_timer_finish audio is inside the raw folder
            mediaPlayer.setLooping(true);//this method will play the audio in  a loop
            mediaPlayer.start();// starting to play audio of mediaplayer
        }
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null && mediaPlayer.isPlaying())// checking if mediaplayer is not null and mediaplayer isplaying so that only then we can stop that...for example  if an audio is playing only then we can stop it
                {
                   mediaPlayer.stop();// stop the audio
                    mediaPlayer.release();// destroy the object frees all system resources
                    mediaPlayer=null; // assigning it to null
                    startbutton.setText("Start");
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    startbutton.setOnClickListener(null);// remove listener to prevent multiple clicks
                    ReInitializeStartButton();//calling ReInitializeStartButton method to reinitialize the button's behaviour its a method which we have created below

                }
            }
        });
    }

    private void ReInitializeStartButton()
    {
        startbutton.setOnClickListener(v ->{ // when the button for timer is clicked
            if (isTimerRunning) // checking if a timer is already running using the boolean varriable isTimerRunning which is already set to false
            // if the condition is false means already a timer is running if true means no timer is running we checked using if statement because if
            // a timer is already running and then the user click the start button to start a timer in the middle of a timer the app may be  crashed
            {
                pauseTimer(); // if the if statement is not  satisfied then it will call pausetimer because if a time or countdown is already running then the user will obviously may want to pause the timer
                //pausetimer is a method which we have created below
            } else if (isPaused)// checking if the time is paused using boolean varriable or flag isPaused
            {
                resumeTimer(); // if the else if statement is satisfied here it will call resumetimer method because if the else if statement is satisfied then it means that the user has paused the timer
                // and obviousely if the timer is paused for some reason the user will may want to resume it again to start it from where he stopped it.......> resumetimer is a method we have created below
            }
            else // if the if and also the else if statement is not satisfied then it means that the user has not started the timer yet
            {
                startTimer(); // that's why we will call here the startTimer  method for starting the timer again startTimer is a method which we have created below
            }
        });

    }

    private void StopMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())// checking if mediaplayer is not null and mediaplayer isplaying so that only then we can stop that...for example  if an audio is playing only then we can stop it
        {
            mediaPlayer.stop();// stop the audio
            mediaPlayer.release();// destroy the object frees all system resources
            mediaPlayer = null; // assigning it to null
        }
    }

    private void StopTimer()
    {
        if (countDownTimer!=null) {//checking if there is any countdown is running or not if it is null the no countdown is running if its not null then the countdown is running
            countDownTimer.cancel(); // this line basically pause the timer by using built in method cancel()
            countDownTimer=null;// setting the countdown to null as timer is paused so setting the coundowntimer object null will help us to understand countdown is running now or not
            isTimerRunning=false;
        }
    }

    @Override
    protected void onPause()//this method is automatically called  When the user leaves the activity (e.g., opens another screen, presses the home button, or locks the screen). its a built in method
    {
        super.onPause();// This calls the original onPause() function from the parent class (Activity). It ensures that Android handles things like saving UI states properly.
        StopMediaPlayer();// Stops the media player if it is still playing. its a method we have created above
        StopTimer();// Stops the countdown timer if it is still running.   its a method we have created above
    }

    @Override
    protected void onDestroy() //this method is automatically called when the activity is completely destroyed (e.g., when the user closes the app or the system kills the activity to free memory).
    {
        super.onDestroy();// Calls the original onDestroy() from the Activity class to clean up system resources.
        StopMediaPlayer();// Stops the media player if it is still playing. its a method we have created above
        StopTimer();// Stops the countdown timer if it is still running.   its a method we have created above
    }
}