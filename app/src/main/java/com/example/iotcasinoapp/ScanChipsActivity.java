package com.example.iotcasinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class ScanChipsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //nav bar variables
    public DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_chips);

        //set up nav bar
        drawerLayout = findViewById(R.id.scan_chips_activity);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set the on click listener
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent nextIntent;
        switch(item.getItemId()){
            case R.id.home_page:
                nextIntent = new Intent(this, MainActivity.class);
                startActivity(nextIntent);
                break;
            case R.id.scan_page:
                break;
            case R.id.settings_page:
                nextIntent = new Intent(this, SettingsActivity.class);
                startActivity(nextIntent);
                break;
        }

        //close the nav drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}