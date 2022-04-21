package com.example.iotcasinoapp;

import java.io.File;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/getHistIDs")
    Call<ResponseBody> executeGetHistIDs(@Body HashMap<String, String> map);

    @POST("/getHistVals")
    Call<ResponseBody> executeGetHistVals(@Body HashMap<String, String> map);

    @POST("/wake")
    Call<Void> executeWake();

    @POST("/updateHist")
    Call<Void> exexcuteUpdateHist(@Body HashMap<String, byte[]> map);

    @POST("/getProfilePicture")
    Call<ResponseBody> executeGetProfilePicture(@Body HashMap<String, String> map);

    @POST("/updateProfilePic")
    Call<Void> executeUpdateProfilePic(@Body HashMap<String, byte[]> map);

    @POST("/addChip")
    Call<Void> executeAddChip(@Body HashMap<String, String> map);

    @POST("/putChipInPlay")
    Call<Void> executePutChipInPlay(@Body HashMap<String, String> map);
}
