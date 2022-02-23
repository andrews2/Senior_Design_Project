package com.example.iotcasinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //GUI variables
    TextView tv;
    int accountValue;
    public DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        // set text view to account value
        tv = findViewById(R.id.home_account_value);
        accountValue = AccountDataHandler.getInstance().getAccountValue();
        String valueText = "$" + String.valueOf(accountValue);
        tv.setText(valueText);
        //set up nav drawer
        drawerLayout = findViewById(R.id.main_activity);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set the on click listener
        navigationView.setNavigationItemSelectedListener(this);

        //set up name in header
        View headerView = navigationView.getHeaderView(0);
        TextView headerText = headerView.findViewById(R.id.header_name);
        headerText.setText(AccountDataHandler.getInstance().getUsername());



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
                break;
            case R.id.scan_page:
                nextIntent = new Intent(this,ScanChipsActivity.class);
                startActivity(nextIntent);
                break;
            case R.id.settings_page:
                nextIntent = new Intent(this, SettingsActivity.class);
                startActivity(nextIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}