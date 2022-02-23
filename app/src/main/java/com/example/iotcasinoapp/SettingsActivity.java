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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // toolbar variables
    public DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView usernameText, accountValue;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // set variables
        drawerLayout = findViewById(R.id.settings_activity);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set nav view on click listeners
        navigationView.setNavigationItemSelectedListener(this);

        //intit settings values
        usernameText = findViewById(R.id.username_text);
        accountValue = findViewById(R.id.account_value);
        logout = findViewById(R.id.logout);
        usernameText.setText(AccountDataHandler.getInstance().getUsername());
        accountValue.setText(String.valueOf(AccountDataHandler.getInstance().getAccountValue()));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back to log in screen
                Intent nextIntent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(nextIntent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        // if drawer is open close drawer
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
        switch (item.getItemId()){
            case R.id.home_page:
                nextIntent = new Intent(this, MainActivity.class);
                startActivity(nextIntent);
                break;
            case R.id.scan_page:
                nextIntent = new Intent(this,ScanChipsActivity.class);
                startActivity(nextIntent);
                break;
            case R.id.settings_page:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}