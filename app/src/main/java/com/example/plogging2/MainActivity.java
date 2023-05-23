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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView textViewStepCounter, textViewWasteCounter;
    Button buttonExercise, buttonRecord, buttonWastePlus, buttonWasteMinus;
    SensorManager sensorManager;
    File file;
    boolean run = false, needToReset = false, isExercising = false;
    int stepCount = 0, wasteCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        textViewStepCounter = (TextView) findViewById(R.id.textViewStepCounter);
        textViewWasteCounter = (TextView) findViewById(R.id.textViewWasteCounter);

        buttonExercise = (Button) findViewById(R.id.buttonExercise);
        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonWastePlus = (Button) findViewById(R.id.buttonWastePlus);
        buttonWasteMinus = (Button) findViewById(R.id.buttonWasteMinus);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        textViewStepCounter.setText("Not Exercising.");

        buttonWastePlus.setEnabled(false);
        buttonWasteMinus.setEnabled(false);
        buttonRecord.setEnabled(false);

        needToReset = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(needToReset)
        {
            stepCount = (int) event.values[0];
            needToReset = false;
        }

        if(run && isExercising)
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
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        run = true;
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

    public void resetCount(View v)
    {
        needToReset = true;
        textViewStepCounter.setText("reset complete.");
    }

    public void toggleExercise(View v)
    {
        if(isExercising)
        {
            isExercising = false;
            buttonExercise.setText("Start Plogging!");
            buttonWastePlus.setEnabled(false);
            buttonWasteMinus.setEnabled(false);
            buttonRecord.setEnabled(true);
        }
        else
        {
            isExercising = true;
            needToReset = true;
            textViewStepCounter.setText("0");
            wasteCount = 0;
            textViewWasteCounter.setText(String.valueOf(wasteCount));
            buttonExercise.setText("End Plogging!");
            buttonWastePlus.setEnabled(true);
            buttonWasteMinus.setEnabled(true);
            buttonRecord.setEnabled(false);
        }
    }

    public void wastePlus(View v)
    {
        wasteCount++;
        textViewWasteCounter.setText(String.valueOf(wasteCount));
    }

    public void wasteMinus(View v)
    {
        wasteCount--;
        if(wasteCount < 0) wasteCount = 0;
        textViewWasteCounter.setText(String.valueOf(wasteCount));
    }
}