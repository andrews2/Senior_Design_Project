package com.example.iotcasinoapp;

import java.io.File;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/getHistGames")
    Call<ResponseBody> executeGetHistGames(@Body HashMap<String, String> map);

    @POST("/getHistVals")
    Call<ResponseBody> executeGetHistVals(@Body HashMap<String, String> map);
}
