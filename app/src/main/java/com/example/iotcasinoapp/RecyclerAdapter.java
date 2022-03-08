package com.example.iotcasinoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<String> historyGames;
    private ArrayList<String> historyVals;
    public RecyclerAdapter(ArrayList<String> historyGames, ArrayList<String> historyVals){
        this.historyGames = historyGames;
        this.historyVals = historyVals;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView gameText, winningsOrLosses;

        public MyViewHolder(final View view){
            super(view);
            gameText = view.findViewById(R.id.textView);
            winningsOrLosses = view.findViewById(R.id.textView2);
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
        String game = historyGames.get(position);
        String val = historyVals.get(position);
        holder.gameText.setText(game);
        holder.winningsOrLosses.setText(val);

    }

    @Override
    public int getItemCount() {
        return historyGames.size();
    }
}
