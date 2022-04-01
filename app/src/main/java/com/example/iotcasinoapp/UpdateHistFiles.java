package com.example.iotcasinoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class UpdateHistFiles implements Runnable{
    File histGames, histVals, histVersion;

    public UpdateHistFiles(File histGames, File histVals, File histVersion){
        this.histGames = histGames;
        this.histVals = histVals;
        this.histVersion = histVersion;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            //write history games
            FileOutputStream fos = new FileOutputStream(histGames);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryGames());

            //write history values
            fos = new FileOutputStream(histVals);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryVals());

            //write history version
            fos = new FileOutputStream(histVersion);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UpdateHistFilesOnServer(histGames, histVals);
    }
}
