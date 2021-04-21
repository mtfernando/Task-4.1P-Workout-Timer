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
    private long pauseOffset=0;
    private boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer2);

        if(savedInstanceState!=null){
            chronometer.setBase(savedInstanceState.getLong("ChronoTime") - savedInstanceState.getLong("PauseOffset"));
            running = savedInstanceState.getBoolean("running");
            if(running){
                chronometer.start();
            }
            System.out.println("onCreate is running!");
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong("ChronoTime", chronometer.getBase());
        savedInstanceState.putFloat("PauseOffset", pauseOffset);
        savedInstanceState.putBoolean("running", running);
    }

    public void startTimer(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseTimer(View view){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void stopTimer(View view){
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        running = false;
    }
}