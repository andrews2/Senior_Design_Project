package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView background, wolfLogo, wolfBackground;
    TextView tv;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //find GUI objects
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        background = (ImageView) findViewById(R.id.background);
        wolfLogo = (ImageView) findViewById(R.id.login_wolf);
        wolfBackground = (ImageView) findViewById(R.id.wolf_background);
        tv = findViewById(R.id.wp_casino_label);
        context = this;
        Glide.with(context).load(R.drawable.pocker_background).centerCrop().into(background);
        tv.bringToFront();
        wolfBackground.bringToFront();
        wolfLogo.bringToFront();
        // set up tab fragments
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Login");
        tabLayout.getTabAt(1).setText("Signup");
        //wake up server
        wakeServer();
    }

    @Override
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
    }

    public void wakeServer(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<Void> call = retrofitInterface.executeWake();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    return;
                }
                wakeServer();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                wakeServer();
            }
        });
    }
}