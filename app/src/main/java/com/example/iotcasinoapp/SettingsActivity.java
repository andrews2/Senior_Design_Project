package com.example.iotcasinoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class SettingsActivity extends BaseActivity {
    TextView usernameText, accountValue;
    Button logout, deleteFiles, changePicture;

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
        deleteFiles = findViewById(R.id.delete_files);
        changePicture = findViewById(R.id.change_picture);
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

        deleteFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File gamesFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_games.ser");
                File valsFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                File versionFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
                gamesFile.delete();
                valsFile.delete();
                versionFile.delete();
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, 1000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK){
            //delete old image
            if(AccountDataHandler.getInstance().getProfilePicture() != "none"){
                File oldProfPic = new File(getFilesDir(), AccountDataHandler.getInstance().getProfilePicture());
                oldProfPic.delete();
            }

            Uri imageUri = data.getData();
            String fileName = AccountDataHandler.getInstance().getUsername() + '_' + imageUri.getPath().substring(imageUri.getPath().lastIndexOf('/') + 1) + ".jpg";
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                File file = new File(getFilesDir(), fileName);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
                if(file.length() > 30000000){
                    Toast.makeText(this, "Image file size is too large.", Toast.LENGTH_LONG).show();
                    file.delete();
                    return;
                }
                AccountDataHandler.getInstance().setProfilePicture(fileName);
                updateHeaderProfilePic(file);
                new UpdateProfilePicOnServer(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}