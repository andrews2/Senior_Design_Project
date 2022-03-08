package com.example.iotcasinoapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    //GUI variables
    TextView tv;
    int accountValue;
    private RecyclerView recyclerView;
    Context context;
    RecyclerAdapter recyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    public static Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_main, pageContainer);
        Intent intent = getIntent();
        context = this;
        // set text view to account value
        tv = findViewById(R.id.home_account_value);
        accountValue = AccountDataHandler.getInstance().getAccountValue();
        String valueText = "$" + String.valueOf(accountValue);
        tv.setText(valueText);
        recyclerView = findViewById(R.id.history_view);
        setAdapter();

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                File historyGames = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_games.ser");
                File historyVals = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                try {
                    FileInputStream fis = new FileInputStream(historyGames);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    AccountDataHandler.getInstance().setHistoryGames((ArrayList<String>) ois.readObject());
                    fis = new FileInputStream(historyVals);
                    ois = new ObjectInputStream(fis);
                    AccountDataHandler.getInstance().setHistoryVals((ArrayList<String>) ois.readObject());
                    ois.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                setAdapter();
            }
        });

        if (AccountDataHandler.getInstance().historyFilesUpToDate == true){
            t.start();
        }

        //set up swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void setAdapter(){
        ArrayList<String> historyGames = AccountDataHandler.getInstance().getHistoryGames();
        ArrayList<String> historyVals = AccountDataHandler.getInstance().getHistoryVals();
        recyclerAdapter = new RecyclerAdapter(historyGames, historyVals);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }
}