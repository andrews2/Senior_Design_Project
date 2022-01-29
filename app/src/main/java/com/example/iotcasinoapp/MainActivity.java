package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    TextView tv;
    int accountValue;
    String accKey = "accountValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set initial variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        accountValue = intent.getIntExtra(accKey, 0);

        //load the socket
        s = SocketHandler.getSocket();


    }
}