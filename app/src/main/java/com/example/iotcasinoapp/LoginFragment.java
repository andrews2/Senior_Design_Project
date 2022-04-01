package com.example.iotcasinoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.crypto.SecretKey;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    EditText username, password;
    Button login;
    String usernameToSend, passwordToSend;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);

        username = root.findViewById(R.id.Username);
        password = root.findViewById(R.id.Password);
        login = root.findViewById(R.id.Login);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                //ecrypt username and password
                try {
                    SecretKey secret = DataEncryption.generateKey();
                    usernameToSend = DataEncryption.encrypt(username.getText().toString().toUpperCase(), secret);
                    passwordToSend = DataEncryption.encrypt(password.getText().toString(), secret);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("name", usernameToSend);
                map.put("password", passwordToSend);

                File historyVersion = new File(getContext().getFilesDir(), username.getText().toString().toUpperCase() + "_version.ser");
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


                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code() == 200){
                            LoginResult result = response.body();
                            //check if we need to download history
                            if(!(AccountDataHandler.getInstance().getHistoryVersion().equals(result.getVersion()))){
                                AccountDataHandler.getInstance().setHistoryUpToDate(false);
                            }
                            else{
                                AccountDataHandler.getInstance().setHistoryUpToDate(true);
                            }

                            // save account value in handler
                            AccountDataHandler.getInstance().setAccountValue(result.getAccountValue());
                            AccountDataHandler.getInstance().setUsername(result.getUsername());
                            AccountDataHandler.getInstance().setHistoryVersion(result.getVersion());
                            AccountDataHandler.getInstance().setProfilePicture(result.getProfilePicture());
                            //start main activity
                            Intent nextIntent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(nextIntent);
                        } else if(response.code() == 400){
                            Toast.makeText(getActivity(), "Incorrect Password for " + username.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                        else if(response.code() == 404){
                            Toast.makeText(getActivity(), "Account does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error connecting to server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;

        //set listeners for enter to go to next text box
    }
}
