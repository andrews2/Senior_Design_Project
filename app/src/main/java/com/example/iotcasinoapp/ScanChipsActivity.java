package com.example.iotcasinoapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.FrameLayout;


public class ScanChipsActivity extends BaseActivity {
    // variables
    private NfcAdapter nfcAdapter;
    private IntentFilter[] writeTagFilters;
    private PendingIntent pendingIntent;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_scan_chips, pageContainer);

        //set up NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);

    }
}