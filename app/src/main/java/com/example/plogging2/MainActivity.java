package com.example.plogging2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView textViewStepCounter, textViewWasteCounter;
    Button buttonExercise, buttonRecord, buttonWastePlus, buttonWasteMinus;
    SensorManager sensorManager;
    File file;
    boolean run = false, needToReset = false, isExercising = false;
    int stepCount = 0, wasteCount = 0;

    ArrayList<String> data_Date = new ArrayList<>();
    ArrayList<String> data_stepCount = new ArrayList<>();
    ArrayList<String> data_wasteCount = new ArrayList<>();

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

    public void dataSave(View v)
    {
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currDate = mFormat.format(mDate);

        data_Date.add(0,currDate);
        data_stepCount.add(0,textViewStepCounter.getText().toString());
        data_wasteCount.add(0,textViewWasteCounter.getText().toString());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PloggingData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json1 = gson.toJson(data_Date);
        String json2 = gson.toJson(data_stepCount);
        String json3 = gson.toJson(data_wasteCount);
        editor.putString("data_Date", json1);
        editor.putString("data_stepCount", json2);
        editor.putString("data_wasteCount", json3);
        editor.apply();

        buttonRecord.setEnabled(false);

        Toast.makeText(this,"Data Saved!", Toast.LENGTH_SHORT).show();
    }

    public void openHistory(View v)
    {
        Intent intent = new Intent(this, DataHistoryActivity.class);
        startActivity(intent);
    }
}