package com.example.iotcasinoapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetProfilePicture implements Runnable {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";
    private File profilePicture;
    private BaseActivity baseActivity;

    public GetProfilePicture(File profilePicture, BaseActivity baseActivity){
        this.profilePicture = profilePicture;
        this.baseActivity = baseActivity;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        new Thread(this).start();
    }

    @Override
    public void run() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", AccountDataHandler.getInstance().getUsername());
        Call<ResponseBody> call = retrofitInterface.executeGetProfilePicture(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200){
                    new GetProfilePicture(profilePicture, baseActivity);
                    return;
                }
                try{
                    byte[] fileReader = new byte[4096];
                    long fileSize = response.body().contentLength();
                    InputStream is = response.body().byteStream();
                    OutputStream os = new FileOutputStream(profilePicture);

                    while(true){
                        int read = is.read(fileReader);

                        if(read == -1) break;

                        os.write(fileReader, 0, read);

                    }
                    os.flush();
                    os.close();
                    baseActivity.updateHeaderProfilePic(profilePicture);
                    return;
                } catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new GetProfilePicture(profilePicture, baseActivity);
                return;
            }
        });
    }
}
