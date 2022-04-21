package com.example.iotcasinoapp;

import java.util.ArrayList;

public class AccountDataHandler {
    private int accountValue;
    private String username;
    private volatile ArrayList<String> historyIDs = new ArrayList<String>();
    private volatile ArrayList<String> historyVals = new ArrayList<String>();
    private String historyVersion;
    private volatile boolean historyUpToDate = false;
    private String profilePicture;


    public int getAccountValue() {
        // account value getter
        return accountValue;
    }

    public void setAccountValue(int accountValue) {
        //account value setter
        this.accountValue = accountValue;
    }

    public void addToAccountValue(int val){
        this.accountValue += val;
    }
    public void subFromAccountValue(int val){this.accountValue -= val;}

    public String getUsername() {
        //username getter
        return username;
    }

    public void setUsername(String username) {
        //username setter
        this.username = username;
    }

    public void addToHistory(String tagID, String val){
        historyIDs.add(0, tagID);
        historyVals.add(0,  val);
    }

    public void setHistoryIDs(ArrayList<String> historyIDs){
        this.historyIDs = historyIDs;
    }

    public void setHistoryVals(ArrayList<String> historyVals){
        this.historyVals = historyVals;
    }

    public ArrayList<String> getHistoryIDs(){
        return historyIDs;
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
    public void incrementHistoryVersion(){
        int currentVersion = Integer.parseInt(this.historyVersion);
        currentVersion++;
        this.historyVersion = String.valueOf(currentVersion);
    }

    public boolean isHistoryUpToDate() {
        return historyUpToDate;
    }

    public void setHistoryUpToDate(boolean historyUpToDate) {
        this.historyUpToDate = historyUpToDate;
    }

    public String getProfilePicture(){
        return profilePicture;
    }

    public void setProfilePicture(String newPic){
        this.profilePicture = newPic;
    }

    // create instance of account data handler
    private static final AccountDataHandler accountDataHandler = new AccountDataHandler();
    //return the instance
    public static AccountDataHandler getInstance() {
        return accountDataHandler;
    }


}
