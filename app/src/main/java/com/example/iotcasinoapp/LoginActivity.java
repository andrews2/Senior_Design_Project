package com.example.iotcasinoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";
    EditText username, password;
    Button login, signup;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        signup = findViewById(R.id.Signup);
        context = this;



        //set up buttons to wait for click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", username.getText().toString());
                map.put("password", password.getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code() == 200){
                            LoginResult result = response.body();
                            Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(nextIntent);
                        } else if(response.code() == 400){
                            Toast.makeText(LoginActivity.this, "Incorrect Password for " + username.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                        else if(response.code() == 404){
                            Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", username.getText().toString());
                map.put("password", password.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(nextIntent);
                        } else if(response.code() == 400){
                            Toast.makeText(LoginActivity.this, "Username is already taken", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(LoginActivity.this, String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}