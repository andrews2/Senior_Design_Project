package com.example.iotcasinoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetHistFiles implements Runnable {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";
    File gamesFile;
    File valsFile;
    File versionFile;
    String version;
    Thread thread;

    public GetHistFiles(File gamesFile, File valsFile, File versionFile, String version){
        this.gamesFile = gamesFile;
        this.valsFile = valsFile;
        this.versionFile = versionFile;
        this.version = version;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", AccountDataHandler.getInstance().getUsername());

        //get the games file
        Call<ResponseBody> call = retrofitInterface.executeGetHistGames(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSize = response.body().contentLength();
                    InputStream is = response.body().byteStream();
                    OutputStream os = new FileOutputStream(gamesFile);

                    while(true){
                        int read = is.read(fileReader);

                        if(read == -1) break;

                        os.write(fileReader, 0, read);

                    }
                    os.flush();
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        //get vals file
        Call<ResponseBody> call1 = retrofitInterface.executeGetHistVals(map);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] fileReader = new byte[4096];
                    long fileSize = response.body().contentLength();
                    InputStream is = response.body().byteStream();
                    OutputStream os = new FileOutputStream(valsFile);

                    while(true){
                        int read = is.read(fileReader);

                        if(read == -1) break;

                        os.write(fileReader, 0, read);

                    }
                    os.flush();
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        try {
            FileOutputStream fos = new FileOutputStream(versionFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(version);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            MainActivity.t.start();
        } catch (Exception e){}

    }
}

