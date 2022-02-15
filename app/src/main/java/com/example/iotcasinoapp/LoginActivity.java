package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;


public class LoginActivity extends AppCompatActivity {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //find GUI objects
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        context = this;
        // set up tab fragments
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Login");
        tabLayout.getTabAt(1).setText("Signup");

    }

}