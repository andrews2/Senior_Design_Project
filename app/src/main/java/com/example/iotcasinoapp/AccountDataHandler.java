package com.example.iotcasinoapp;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountDataHandler {
    private int accountValue;
    private String username;
    private ArrayList<String> historyGames = new ArrayList<String>();
    private ArrayList<String> historyVals = new ArrayList<String>();
    private String historyVersion;
    public volatile boolean historyFilesUpToDate = false;


    public int getAccountValue() {
        // account value getter
        return accountValue;
    }

    public void setAccountValue(int accountValue) {
        //account value setter
        this.accountValue = accountValue;
    }

    public String getUsername() {
        //username getter
        return username;
    }

    public void setUsername(String username) {
        //username setter
        this.username = username;
    }

    public void addToHistory(String game, String val){
        historyGames.add(0, game);
        historyVals.add(0, val);
    }

    public void setHistoryGames(ArrayList<String> historyGames){
        this.historyGames = historyGames;
    }

    public void setHistoryVals(ArrayList<String> historyVals){
        this.historyVals = historyVals;
    }

    public ArrayList<String> getHistoryGames(){
        return historyGames;
    }

    public ArrayList<String> getHistoryVals(){
        return historyVals;
    }

    public String getHistoryVersion() {
        return historyVersion;
    }
    public void setHistoryVersion(String historyVersion) {
        this.historyVersion = historyVersion;
    }
    // create instance of account data handler
    private static final AccountDataHandler accountDataHandler = new AccountDataHandler();
    //return the instance
    public static AccountDataHandler getInstance() {
        return accountDataHandler;
    }
}
