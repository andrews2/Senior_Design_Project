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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends BaseActivity {

    // toolbar variables
    public DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView usernameText, accountValue;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_settings, pageContainer);
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
}