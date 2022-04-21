package com.example.iotcasinoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsActivity extends BaseActivity {
    TextView usernameText, accountValue, changePicText;
    Button logout, deleteFiles, changePicture;
    ImageView profilePic;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_settings, pageContainer);
        //init settings values
        context = this;
        usernameText = findViewById(R.id.username_text);
        accountValue = findViewById(R.id.account_value);
        logout = findViewById(R.id.logout);
        deleteFiles = findViewById(R.id.delete_files);
        changePicText = findViewById(R.id.change_pic_text);
        profilePic = findViewById(R.id.settings_prof_pic);
        usernameText.setText(AccountDataHandler.getInstance().getUsername());
        accountValue.setText(String.valueOf(AccountDataHandler.getInstance().getAccountValue()));

        //set text to be underlined for change profile pic text
        SpannableString spannableString = new SpannableString("Change Profile Picture");
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        changePicText.setText(spannableString);
        changePicText.bringToFront();

        //set profile picture
        if(AccountDataHandler.getInstance().getProfilePicture().equals("none")){
            Glide.with(context).load(R.drawable.wolf_logo).centerInside().into(profilePic);
        }
        else{
            File profilePicFile = new File(getFilesDir(), AccountDataHandler.getInstance().getProfilePicture());
            if (profilePicFile.exists()){
                RequestOptions options = new RequestOptions();
                options.circleCrop();
                Glide.with(context).load(profilePicFile).apply(options).into(profilePic);
            }
            else{
                new GetProfilePicture(profilePicFile, this);
            }
        }


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
                File idsFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_ids.ser");
                File valsFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                File versionFile = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
                idsFile.delete();
                valsFile.delete();
                versionFile.delete();
            }
        });

        changePicText.setOnClickListener(new View.OnClickListener() {
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
            String fileName = AccountDataHandler.getInstance().getUsername() + '_' + imageUri.getPath().substring(imageUri.getPath().lastIndexOf('/') + 1) + ".webp";
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                File file = new File(getFilesDir(), fileName);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.WEBP, 75, out);
                out.close();
                if(file.length() > 30000000){
                    Toast.makeText(this, "Image file size is too large.", Toast.LENGTH_LONG).show();
                    file.delete();
                    return;
                }
                AccountDataHandler.getInstance().setProfilePicture(fileName);
                updateHeaderProfilePic(file);
                RequestOptions options = new RequestOptions();
                options.circleCrop();
                Glide.with(context).load(file).apply(options).into(profilePic);
                new UpdateProfilePicOnServer(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}