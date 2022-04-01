package com.example.iotcasinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView headerImage, profilePicture;
    Thread t;
    View headerView;

    protected void onCreateDrawer() {
        setContentView(R.layout.activity_base);
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        headerImage = (ImageView) headerView.findViewById(R.id.header_image);
        profilePicture = (ImageView) headerView.findViewById(R.id.profile_pic);
        TextView headerText = headerView.findViewById(R.id.header_name);
        headerText.setText(AccountDataHandler.getInstance().getUsername());
        Glide.with(headerView).load(R.drawable.pocker_background).apply(new RequestOptions().override(1152, 1356)).centerCrop().into(headerImage);

        if(AccountDataHandler.getInstance().getProfilePicture().equals("none")){
            Glide.with(headerView).load(R.drawable.wolf_logo).centerInside().into(profilePicture);
        }
        else{
            File profilePicFile = new File(getFilesDir(), AccountDataHandler.getInstance().getProfilePicture());
            if (profilePicFile.exists()){
                updateHeaderProfilePic(profilePicFile);
            }
            else{
                new GetProfilePicture(profilePicFile, this);
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent nextIntent = null;
        switch (item.getItemId()){
            case R.id.home_page:
                nextIntent = new Intent(this, MainActivity.class);
                break;
            case R.id.scan_page:
                nextIntent = new Intent(this,ScanChipsActivity.class);
                break;
            case R.id.settings_page:
                nextIntent = new Intent(this,SettingsActivity.class);
                break;
        }
        if (this.getIntent().filterEquals(nextIntent)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        startActivity(nextIntent);
        return true;
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

    public void updateHeaderProfilePic(File file){
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(headerView).load(file).apply(options).into(profilePicture);
    }
}