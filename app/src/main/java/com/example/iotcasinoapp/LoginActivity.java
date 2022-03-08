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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


public class LoginActivity extends AppCompatActivity {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView background;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //find GUI objects
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        background = (ImageView) findViewById(R.id.background);
        tv = findViewById(R.id.wp_casino_label);
        context = this;
        Glide.with(context).load(R.drawable.pocker_background).centerCrop().into(background);
        tv.bringToFront();
        // set up tab fragments
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Login");
        tabLayout.getTabAt(1).setText("Signup");
        //set up files
        File historyVersion = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
        if(historyVersion.exists()){
            try{
                FileInputStream fis = new FileInputStream(historyVersion);
                ObjectInputStream ois = new ObjectInputStream(fis);
                AccountDataHandler.getInstance().setHistoryVersion((String) ois.readObject());
                ois.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            AccountDataHandler.getInstance().setHistoryVersion("0");
        }
    }

    @Override
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
    }

}