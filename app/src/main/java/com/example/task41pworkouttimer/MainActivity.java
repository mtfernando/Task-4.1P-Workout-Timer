package com.example.task41pworkouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private TextView workoutTextView;
    private TextView workoutTypeEditText;
    private long pauseOffset;
    private boolean running=false;
    private long chronotime;
    private SharedPreferences sp;
    public String workoutTime;
    public String workoutType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("SharedPrefs", MODE_PRIVATE);

        chronometer = findViewById(R.id.chronometer2);
        workoutTextView = findViewById(R.id.workoutTextView);
        workoutTypeEditText = findViewById(R.id.editTextWorkoutType);

        updateWorkoutDetails();

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
                chronotime = SystemClock.elapsedRealtime() - chronometer.getBase();
            }
        });
    }

    //This function updates the text view with your previous workout details.
    public void updateWorkoutDetails(){
        System.out.println("Update function is running");

        workoutTime = sp.getString("workoutTime", "");
        workoutType = sp.getString("workoutType", "");

        System.out.println(workoutTime);
        System.out.println(workoutType);
        if(!workoutTime.equals("")) {
            System.out.println("Loop 1");
            if(!workoutType.equals("")){
                System.out.println("Loop 2");
                workoutTextView.setText("You spent " + workoutTime + " on " + workoutType + " last time.");
            }
            else{
                workoutTextView.setText("You spent " + workoutTime + " on your workout last time.");
            }
        }
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

        //Chronometer time to a string
        int mins = (int)(chronotime/1000/60);
        int secs = (int)((chronotime/1000)-(mins*60));
        String minsStr = String.valueOf(mins);
        String secsStr = String.valueOf(secs);

        //Padding Zeros
        if(mins<10){
            minsStr = ("0"+minsStr);
        }
        if(secs<10){
            secsStr = ("0"+secsStr);
        }

        String timeStr = (minsStr+":"+secsStr);
        System.out.println(timeStr);

        //Only updating shared preferences if an actual workout was done.
        //Avoids accidentail erasing of previous workout details by clicking the stop button.
        System.out.println(chronotime);
        if(chronotime>0){
            SharedPreferences.Editor workoutEdit = sp.edit();
            workoutEdit.putString("workoutTime", timeStr);
            workoutEdit.putString("workoutType", workoutTypeEditText.getText().toString());

            workoutEdit.commit();
            updateWorkoutDetails();
        }

        //Reset chronometer base to current time - 00:00
        chronometer.setBase(SystemClock.elapsedRealtime());

        //Resetting other auxiliary variables.
        pauseOffset = 0;
        chronotime = 0;
        running = false;

        Toast.makeText(getApplicationContext(), "Stopwatch has been reset!", Toast.LENGTH_SHORT).show();
    }
}