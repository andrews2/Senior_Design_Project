package com.example.iotcasinoapp;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateHistFilesOnServer implements Runnable{
    File histGames, histVals;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public UpdateHistFilesOnServer(File histGames, File histVals){
        this.histGames = histGames;
        this.histVals = histVals;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        new Thread(this).start();
    }

    @Override
    public void run() {
        byte[] histGamesBytes = new byte[0];
        byte[] histValsBytes = new byte[0];
        try {
            histGamesBytes = Files.toByteArray(histGames);
            histValsBytes = Files.toByteArray(histVals);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, byte[]> map = new HashMap<>();
        map.put("uName", AccountDataHandler.getInstance().getUsername().getBytes(StandardCharsets.UTF_8));
        map.put("value", String.valueOf(AccountDataHandler.getInstance().getAccountValue()).getBytes(StandardCharsets.UTF_8));
        map.put("version", AccountDataHandler.getInstance().getHistoryVersion().getBytes(StandardCharsets.UTF_8));
        map.put("histGames", histGamesBytes);
        map.put("histVals", histValsBytes);

        Call<Void> call = retrofitInterface.exexcuteUpdateHist(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200){
                    new UpdateHistFilesOnServer(histGames, histVals);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new UpdateHistFilesOnServer(histGames, histVals);
                return;
            }
        });

    }
}
