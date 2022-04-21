package com.example.iotcasinoapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PutInPlayOnServer implements Runnable{
    private String chipID;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public PutInPlayOnServer(String chipID){
        this.chipID = chipID;
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
                    new PutInPlayOnServer(chipID);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new PutInPlayOnServer(chipID);
                return;
            }
        });
    }
}
