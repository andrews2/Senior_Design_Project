package com.example.iotcasinoapp;

public class AccountDataHandler {
    private int accountValue;
    private String username;


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

    // create instance of account data handler
    private static final AccountDataHandler accountDataHandler = new AccountDataHandler();
    //return the instance
    public static AccountDataHandler getInstance() {
        return accountDataHandler;
    }


}
