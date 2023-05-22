package com.example.plogging2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView textViewStepCounter, textViewIsRun;
    SensorManager sensorManager;
    boolean run = false, needToReset = false;
    int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        textViewStepCounter = (TextView) findViewById(R.id.textViewStepCounter);
        textViewIsRun = (TextView) findViewById(R.id.textViewIsRun);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        textViewStepCounter.setText("0");

        needToReset = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(needToReset)
        {
            stepCount = (int) event.values[0];
            needToReset = false;
        }
        if(run)
        {
            textViewStepCounter.setText(String.valueOf((int) event.values[0] - stepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        run = false;
        textViewIsRun.setText("Not Run");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        run = true;
        textViewIsRun.setText("Running");
        Sensor count = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(count != null)
        {
            sensorManager.registerListener(this, count, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            textViewStepCounter.setText("No Sensor.");
        }
    }
}