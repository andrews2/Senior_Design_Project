package com.example.iotcasinoapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class ScanChipsActivity extends BaseActivity {
    // variables
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter writingTagFilters[];
    private boolean writeMode;
    private Tag tag;
    TextView readValue, status;
    Button setButton;
    EditText valueToSet;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_scan_chips, pageContainer);

        //set GUI objects
        readValue = findViewById(R.id.tag_value);
        status = findViewById(R.id.status_text);
        setButton = findViewById(R.id.set_button);
        valueToSet = findViewById(R.id.value_to_set);

        context = this;


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tag == null) {
                    status.setText("No tag found.");
                } else {
                    try {
                        writeChip("PlainText|" + valueToSet.getText().toString(), tag);
                        status.setText("Written Successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(context, "No NFC hardware found", Toast.LENGTH_LONG);
            Intent nextIntent = new Intent(context, MainActivity.class);
            startActivity(nextIntent);
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter chipDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        chipDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { chipDetected };
    }

    private void readFromIntent(Intent intent){
        String action = intent.getAction();
        if(actionType(action)){
            Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msg = null;
            if(rawMsg != null){
                msg = new NdefMessage[rawMsg.length];
                for(int i =0; i < rawMsg.length; i++){
                    msg[i] = (NdefMessage) rawMsg[i];
                }
            }
            buildTagView(msg);
        }
    }

    private boolean actionType(String action){
        // check if tag discovered is correct type
        return (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action));
    }

    private void buildTagView(NdefMessage[] msg){
        if((msg == null) || msg.length == 0) return;
        String text = "";
        byte[] payload = msg[0].getRecords()[0].getPayload();
        String encoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int lanCodeLen = payload[0] & 0063;

        try{
            text = new String(payload, lanCodeLen + 11, 3, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        readValue.setText("$" + text);
    }

    private void writeChip(String text, Tag tag) throws IOException, FormatException{
        NdefRecord[] records = { createRecord(text)};
        NdefMessage msg = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(msg);
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "eng";
        String tagType = "PokerChip";
        byte[] typeBytes = tagType.getBytes();
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1+langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, typeBytes, new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        writeModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        writeModeOn();
    }

    private void writeModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }

    private void writeModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }




}