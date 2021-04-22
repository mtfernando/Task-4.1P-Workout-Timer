package com.example.task41pworkouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running=false;
    private long chronotime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer2);

        if(savedInstanceState!=null){

            //Setting chronometer time to previously presented time
            //Time the app has been running - the time the chronometer has been running.
            chronometer.setBase(SystemClock.elapsedRealtime() - savedInstanceState.getLong("ChronoTime"));

            //Updating auxiliary variables
            pauseOffset = savedInstanceState.getLong("PauseOffset");
            running = savedInstanceState.getBoolean("running");
            chronotime = SystemClock.elapsedRealtime() - chronometer.getBase();;

            //If the chronometer wasn't previously paused, it will resume
            //Else it will remain stopped.
            if(running){
                chronometer.start();
            }
        }

        //chronotime is updated at every tick of the chronometer
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
        {
            @Override
            public void onChronometerTick(Chronometer chronometer)
            {
                chronotime = SystemClock.elapsedRealtime() - chronometer.getBase();;
            }
        });
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        //Chronometer is stopped when the activity is destroyed
        chronometer.stop();

        //Saving auxiliary variables in instance state
        savedInstanceState.putLong("ChronoTime", chronotime);
        savedInstanceState.putLong("PauseOffset", pauseOffset);
        savedInstanceState.putBoolean("running", running);
    }

    //This function runs when the start button is pressed
    public void startTimer(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    //This function runs when the pause button is pressed
    public void pauseTimer(View view){
        if(running){
            chronometer.stop();

            //time between start and pause button press
            //Total system run time - time that the chronometer started
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

            //Updating auxiliary variable
            running = false;
        }
    }

    //This function runs when the stop button is pressed
    public void stopTimer(View view){
        chronometer.stop();

        //Reset chronometer base to current time - 00:00
        chronometer.setBase(SystemClock.elapsedRealtime());

        //Resetting other auxiliary variables.
        pauseOffset = 0;
        chronotime = 0;
        running = false;

        //TODO - UPDATE SHARED PREFERENCES WITH WORKOUT DETAILS
    }
}