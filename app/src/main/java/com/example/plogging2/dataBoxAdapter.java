package com.example.plogging2;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dataBoxAdapter extends RecyclerView.Adapter<dataBoxHolder> {

    Context context;
    List<dataBoxItems> items;

    public dataBoxAdapter(Context context, List<dataBoxItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public dataBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dataBoxHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull dataBoxHolder holder, int position) {
        holder.textViewData.setText("step: " + items.get(position).getStepCount() + ", waste: " + items.get(position).getWasteCount());
        holder.textViewDate.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
