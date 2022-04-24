package com.example.iotcasinoapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    public Thread t;
    File historyIDs;
    File historyVals;
    File historyVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_main, pageContainer);
        Intent intent = getIntent();
        context = this;
        // get files
        historyIDs = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_ids.ser");
        historyVals = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
        historyVersion = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
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
                while (!AccountDataHandler.getInstance().isHistoryUpToDate()){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileInputStream fis = new FileInputStream(historyIDs);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    AccountDataHandler.getInstance().setHistoryIDs((ArrayList<String>) ois.readObject());
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
                updateHistoryRecycler();
            }
        });

        if(!AccountDataHandler.getInstance().isHistoryUpToDate()){
            new GetHistFiles(historyIDs, historyVals, historyVersion, AccountDataHandler.getInstance().getHistoryVersion());
        }
        t.start();

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
        ArrayList<String> historyIDs = AccountDataHandler.getInstance().getHistoryIDs();
        ArrayList<String> historyVals = AccountDataHandler.getInstance().getHistoryVals();
        recyclerAdapter = new RecyclerAdapter(historyIDs, historyVals);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void updateHistoryRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapter();
                recyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}