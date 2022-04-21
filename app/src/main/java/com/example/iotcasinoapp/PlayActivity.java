package com.example.iotcasinoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class PlayActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    Button button;
    String chipID;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_play, pageContainer);

        //GUI objects
        spinner = findViewById(R.id.tag_spinner);
        button = findViewById(R.id.play_button);

        spinner.setOnItemSelectedListener(this);

        //create adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, AccountDataHandler.getInstance().getHistoryIDs());
        spinner.setAdapter(arrayAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int chipValue = Integer.parseInt(AccountDataHandler.getInstance().getHistoryVals().get(position));
                AccountDataHandler.getInstance().subFromAccountValue(chipValue);
                AccountDataHandler.getInstance().incrementHistoryVersion();
                //remove chip from history
                AccountDataHandler.getInstance().getHistoryIDs().remove(position);
                AccountDataHandler.getInstance().getHistoryVals().remove(position);
                File histIDs = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_ids.ser");
                File histVals = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                File histVersion = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
                new UpdateHistFiles(histIDs, histVals, histVersion);

                spinner.setSelection(0);
                new PutInPlayOnServer(chipID);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        chipID = AccountDataHandler.getInstance().getHistoryIDs().get(i);
        position = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}