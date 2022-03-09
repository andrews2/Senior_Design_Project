package com.example.iotcasinoapp;

import java.util.ArrayList;

public class AccountDataHandler {
    private int accountValue;
    private String username;
    private volatile ArrayList<String> historyGames = new ArrayList<String>();
    private volatile ArrayList<String> historyVals = new ArrayList<String>();
    private String historyVersion;
    private volatile boolean historyUpToDate = false;


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

    public boolean isHistoryUpToDate() {
        return historyUpToDate;
    }

    public void setHistoryUpToDate(boolean historyUpToDate) {
        this.historyUpToDate = historyUpToDate;
    }
    // create instance of account data handler
    private static final AccountDataHandler accountDataHandler = new AccountDataHandler();
    //return the instance
    public static AccountDataHandler getInstance() {
        return accountDataHandler;
    }


}
