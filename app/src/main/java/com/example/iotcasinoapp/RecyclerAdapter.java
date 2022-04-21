package com.example.iotcasinoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<String> historyIDs;
    private ArrayList<String> historyVals;
    public RecyclerAdapter(ArrayList<String> historyIDs, ArrayList<String> historyVals){
        this.historyIDs = historyIDs;
        this.historyVals = historyVals;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView idText, chipValue;

        public MyViewHolder(final View view){
            super(view);
            idText = view.findViewById(R.id.textView);
            chipValue = view.findViewById(R.id.textView2);
        }

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String tagID = historyIDs.get(position);
        String val = historyVals.get(position);
        holder.idText.setText("Chip ID: " + tagID);
        holder.chipValue.setText("Value: " + val);
    }

    @Override
    public int getItemCount() {
        return historyIDs.size();
    }
}
