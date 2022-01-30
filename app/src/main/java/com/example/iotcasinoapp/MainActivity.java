package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        tv = findViewById(R.id.textView1);
        accountValue = intent.getIntExtra(accKey, 0);

        tv.setText(String.valueOf(accountValue));
        




    }
}