package com.example.plogging2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class dataBoxHolder extends RecyclerView.ViewHolder {
    TextView textViewDate, textViewData;

    public dataBoxHolder(@NonNull View itemView) {
        super(itemView);
        textViewDate = itemView.findViewById(R.id.textViewDate);
        textViewData = itemView.findViewById(R.id.textViewData);
    }
}
