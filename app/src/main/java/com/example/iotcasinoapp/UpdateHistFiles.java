package com.example.iotcasinoapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class UpdateHistFiles implements Runnable{
    File histIDs, histVals, histVersion;

    public UpdateHistFiles(File histIDs, File histVals, File histVersion){
        this.histIDs = histIDs;
        this.histVals = histVals;
        this.histVersion = histVersion;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            //write history games
            FileOutputStream fos = new FileOutputStream(histIDs);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryIDs());

            //write history values
            fos = new FileOutputStream(histVals);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryVals());

            //write history version
            fos = new FileOutputStream(histVersion);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(AccountDataHandler.getInstance().getHistoryVersion());
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UpdateHistFilesOnServer(histIDs, histVals);
    }
}
