package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView tv;
    Button login, signup;
    String SERVER_IP = "172.20.10.2";
    int SERVER_PORT = 1234;
    String message;
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        signup = findViewById(R.id.Signup);
        password = findViewById(R.id.Password);
        context = this;



        //set up buttons to wait for click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "LI " + username.getText().toString() + " " + password.getText().toString();
                signup.setClickable(false);
                login.setClickable(false);
                connectToServer();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "SU " + username.getText().toString() + " " + password.getText().toString();
                signup.setClickable(false);
                login.setClickable(false);
                connectToServer();
            }
        });


    }
    //sends messages to screen
    public void showToast(final String toast){
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_LONG).show());
    }
    //reset GUI objects to inital states
    public void resetInputs(){
        runOnUiThread(() -> {
            username.setText("");
            password.setText("");
            username.setEnabled(true);
            password.setEnabled(true);
            signup.setClickable(true);
            login.setClickable(true);
        });
    }

    public void lockInputsOnError(String e){
        runOnUiThread(()->{
            tv.setText(e);
            username.setText("");
            password.setText("");
            username.setEnabled(false);
            password.setEnabled(false);
            signup.setClickable(false);
            login.setClickable(false);
        });
    }
    //main thread to handle connection to server
    public void connectToServer(){
        Thread thread1 = new Thread(){
            public void run(){
                try {
                    message.trim(); //trim username and password message to be sent
                    // initalize server connection objects
                    s = new Socket(SERVER_IP, SERVER_PORT);
                    oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(message);
                    ois = new ObjectInputStream(s.getInputStream());
                    // get reply from server
                    String reply = (String) ois.readObject();
                    if (reply.equals("Login/signup Succesful")) { // check if is a succesful login
                        int accval = (int) ois.readObject();
                        SocketHandler.setSocket(s);
                        Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                        nextIntent.putExtra("accountValue", accval);
                        LoginActivity.this.startActivity(nextIntent);
                    }
                    else{
                        showToast(reply);
                        resetInputs();
                    }
                } catch (Exception e) {
                    showToast("Error: " + e.toString());
                }
            }
        };
        thread1.start();
    }

}