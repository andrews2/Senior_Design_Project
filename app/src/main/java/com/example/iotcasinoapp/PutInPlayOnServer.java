package com.example.iotcasinoapp;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PutInPlayOnServer implements Runnable{
    private String chipID;
    File histIDs, histVals, histVersion;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public PutInPlayOnServer(String chipID, File histIDs, File histVals, File histVersion){
        this.chipID = chipID;
        this.histIDs = histIDs;
        this.histVals = histVals;
        this.histVersion = histVersion;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        new Thread(this).start();
    }

    @Override
    public void run() {
        HashMap<String, String> map = new HashMap<>();
        map.put("chipID", chipID);
        map.put("name", AccountDataHandler.getInstance().getUsername());

        Call<Void> call = retrofitInterface.executePutChipInPlay(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200){
                    new PutInPlayOnServer(chipID, histIDs, histVals, histVersion);
                    return;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new UpdateHistFiles(histIDs, histVals, histVersion);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new PutInPlayOnServer(chipID, histIDs, histVals, histVersion);
                return;
            }
        });
    }
}
