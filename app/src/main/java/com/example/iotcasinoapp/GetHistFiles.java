package com.example.iotcasinoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

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
    File idsFile;
    File valsFile;
    volatile boolean idsDoneWriting;
    volatile boolean valsDoneWriting;
    File versionFile;
    String version;
    Thread thread;

    public GetHistFiles(File idsFile, File valsFile, File versionFile, String version){
        this.idsFile = idsFile;
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
        idsDoneWriting = false;
        valsDoneWriting = false;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", AccountDataHandler.getInstance().getUsername());

        //get the games file
        Call<ResponseBody> call = retrofitInterface.executeGetHistIDs(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() != 200){ //make sure it gets response
                    new GetHistFiles(idsFile, valsFile, versionFile, version);
                    return;
                }
                try {

                    byte[] fileReader = new byte[4096];
                    long fileSize = response.body().contentLength();
                    InputStream is = response.body().byteStream();
                    OutputStream os = new FileOutputStream(idsFile);

                    while(true){
                        int read = is.read(fileReader);

                        if(read == -1) break;

                        os.write(fileReader, 0, read);

                    }
                    os.flush();
                    os.close();
                    idsDoneWriting = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new GetHistFiles(idsFile, valsFile, versionFile, version);
                return;
            }
        });


        //get vals file
        Call<ResponseBody> call1 = retrofitInterface.executeGetHistVals(map);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() != 200){ //make sure it gets response
                    new GetHistFiles(idsFile, valsFile, versionFile, version);
                    return;
                }
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
                    valsDoneWriting = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new GetHistFiles(idsFile, valsFile, versionFile, version);
                return;
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

        while(!(idsFile.exists() && valsFile.exists() && valsDoneWriting && idsDoneWriting)){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        AccountDataHandler.getInstance().setHistoryUpToDate(true);
        return;
    }
}

