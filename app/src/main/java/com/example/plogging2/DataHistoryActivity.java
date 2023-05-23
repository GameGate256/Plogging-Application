package com.example.plogging2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHistoryActivity extends AppCompatActivity {
    ArrayList<String> data_Date = new ArrayList<>();
    ArrayList<String> data_stepCount = new ArrayList<>();
    ArrayList<String> data_wasteCount = new ArrayList<>();

    List<dataBoxItems> list = new ArrayList<dataBoxItems>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        dataLoad();

        for(int i = 0; i < data_Date.size(); i++)
        {
            list.add(new dataBoxItems(data_Date.get(i), data_stepCount.get(i), data_wasteCount.get(i)));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new dataBoxAdapter(getApplicationContext(),list));
    }

    public void backToMain(View v)
    {
        this.finish();
    }

    public void dataLoad()
    {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PloggingData", MODE_PRIVATE);

        Gson gson = new Gson();
        String json1 = sharedPreferences.getString("data_Date", null);
        String json2 = sharedPreferences.getString("data_stepCount", null);
        String json3 = sharedPreferences.getString("data_wasteCount", null);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        data_Date = gson.fromJson(json1,type);
        data_stepCount = gson.fromJson(json2,type);
        data_wasteCount = gson.fromJson(json3,type);

        if(data_Date == null) data_Date = new ArrayList<String>();
        if(data_stepCount == null) data_stepCount = new ArrayList<String>();
        if(data_wasteCount == null) data_wasteCount = new ArrayList<String>();
    }

    public void initializeData(View v)
    {
        Gson gson = new Gson();
        data_Date = new ArrayList<String>();
        data_stepCount = new ArrayList<String>();
        data_wasteCount = new ArrayList<String>();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PloggingData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String json1 = gson.toJson(data_Date);
        String json2 = gson.toJson(data_stepCount);
        String json3 = gson.toJson(data_wasteCount);
        editor.putString("data_Date", json1);
        editor.putString("data_stepCount", json2);
        editor.putString("data_wasteCount", json3);
        editor.apply();

        dataLoad();

        for(int i = 0; i < data_Date.size(); i++)
        {
            list.add(new dataBoxItems(data_Date.get(i), data_stepCount.get(i), data_wasteCount.get(i)));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new dataBoxAdapter(getApplicationContext(),list));
    }
}