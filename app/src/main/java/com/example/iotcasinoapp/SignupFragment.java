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

import java.util.HashMap;
import java.util.Locale;

import javax.crypto.SecretKey;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment {
    EditText username, password, confrimPassword;
    Button signup;
    String usernameToSend, passwordToSend;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        signup = root.findViewById(R.id.Signup);
        username = root.findViewById(R.id.Username);
        password = root.findViewById(R.id.Password);
        confrimPassword = root.findViewById(R.id.ConfirmPassword);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(password.getText().toString().equals(confrimPassword.getText().toString()))){
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                if (username.getText().toString().equals("") || password.getText().toString().equals("") || confrimPassword.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }
                //create map object with username and password
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
                //send message to server
                Call<Void> call = retrofitInterface.executeSignup(map);
                //wait for response
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            // successful signup attempt, start main activity
                            AccountDataHandler.getInstance().setAccountValue(0);
                            AccountDataHandler.getInstance().setUsername(username.getText().toString().toUpperCase());
                            Intent nextIntent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(nextIntent);
                        } else if(response.code() == 400){
                            // if username is taken
                            Toast.makeText(getActivity(), "Username is already taken", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error connecting to server. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;
    }

}
