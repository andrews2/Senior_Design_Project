package com.example.iotcasinoapp;

public class LoginResult {
    private String accountValue;
    private String username;
    private String version;
    private String profilePicture;
    public String getProfilePicture(){return profilePicture;}
    public int getAccountValue() {
        return Integer.parseInt(accountValue);
    }
    public String getUsername(){ return username;}
    public String getVersion() { return version; }
}
