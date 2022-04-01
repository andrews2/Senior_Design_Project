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

public class UpdateProfilePicOnServer implements Runnable {
    private File imageFile;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public UpdateProfilePicOnServer(File imageFile){
        this.imageFile = imageFile;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        new Thread(this).start();
    }

    @Override
    public void run() {
        byte[] imageBytes = null;
        try {
            imageBytes = Files.toByteArray(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, byte[]> map = new HashMap<>();
        map.put("uName", AccountDataHandler.getInstance().getUsername().getBytes(StandardCharsets.UTF_8));
        map.put("fileName", AccountDataHandler.getInstance().getProfilePicture().getBytes(StandardCharsets.UTF_8));
        map.put("image", imageBytes);

        Call<Void> call = retrofitInterface.executeUpdateProfilePic(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200){
                    new UpdateProfilePicOnServer(imageFile);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new UpdateProfilePicOnServer(imageFile);
                return;
            }
        });
    }
}
