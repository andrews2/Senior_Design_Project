package com.example.iotcasinoapp;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ScanChipsActivity extends BaseActivity {
    // variables
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter writingTagFilters[];
    private boolean writeMode;
    private Tag tag;
    TextView readValue, status;
    Button setButton;
    EditText valueToSet;
    private Context context;
    private Timer t;
    ScheduledExecutorService exec;
    String tagID;

    @SuppressLint("SetTextI18n")
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

        // keep screen on
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            status.setText("You don't have an NFC capable device.");
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            status.setText("NFC Adapter is not turned on");
        } else {
            //mTextView.setText(R.string.explanation);
            t = new Timer();
            exec = Executors.newSingleThreadScheduledExecutor();
        }
        //FLAG_ACTIVITY_SINGLE_TOP: If set, the activity will not be launched if it is already running at the top of the history stack.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // readFromIntent(getIntent());
        IntentFilter chipDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        chipDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { chipDetected };
    }

    /* private void readFromIntent(Intent intent){
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
    } */

    @Override
    protected void onResume() {
        super.onResume();
        // disable sound if possible.
        // this should enable the reader mode for NFCA tags without the sound, but it only works once and then crashes the NFC reader so a reboot is required
        //nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) { // this method is called when an NFC tag is scanned
        super.onNewIntent(intent);
        String action = intent.getAction();
        switch (action) {
            case NfcAdapter.ACTION_NDEF_DISCOVERED:
                //mTextView.setText("NDEF");
                break;
            case NfcAdapter.ACTION_TECH_DISCOVERED:
                //mTextView.setText("TECH");
                break;
            case NfcAdapter.ACTION_TAG_DISCOVERED:
                //mTextView.setText("TAG");
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                System.out.println(tag.toString());
                startGlove(tag);
                break;
        }
    }

    private void startGlove(Tag tag) {
        String tech = tag.getTechList()[0];
        System.out.println(tech);
        switch (tech) {
            case "android.nfc.tech.NfcA":
                System.out.println("NfcA");
                break;
            case "android.nfc.tech.NfcB":
                System.out.println("NfcB");
                // NfcB nfcB = NfcB.get(tag);
                // byte[] applicationData = nfcB.getApplicationData();
                // System.out.println(Arrays.toString(applicationData));
                // startVibrating(NfcB.get(tag));
                break;
            case "android.nfc.tech.NfcF":
                System.out.println("NfcF");
                break;
            case "android.nfc.tech.NfcV":
                System.out.println("NfcV");
                break;

            // NFC Wisp will enter this case statement always
            case "android.nfc.tech.IsoDep":
                System.out.println("IsoDep Detected");
                NfcB nfcB = NfcB.get(tag);
                byte[] applicationData = nfcB.getApplicationData();
                byte[] tagIDbytes = tag.getId();  // Gets the ID of NFC Tag
                tagID = bytesToHexString(tagIDbytes);   // Converts tagID into readable format
                System.out.println(tagID);
                String gameType = "";
                int chipValue;
                String dataString = bytesToHexString(applicationData);

                switch(dataString.charAt(2)){
                    case '0':
                        gameType = "Poker";
                        break;
                    case '1':
                        gameType = "Black Jack";
                        break;
                    case '2':
                        gameType = "Craps";
                        break;
                    case '3':
                        gameType = "Other";
                        break;
                }

                chipValue = Integer.parseInt(dataString.substring(3));
                System.out.println(chipValue);
                System.out.println(gameType);
                AccountDataHandler.getInstance().addToHistory(gameType, "+" + String.valueOf(chipValue));
                AccountDataHandler.getInstance().addToAccountValue(chipValue);
                AccountDataHandler.getInstance().incrementHistoryVersion();
                File histGames = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_games.ser");
                File histVals = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                File histVersion = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
                new UpdateHistFiles(histGames, histVals, histVersion);
                break;

            case "android.nfc.tech.Ndef":
                System.out.println("Ndef Format");
                break;
            case "android.nfc.tech.MifareClassic":
                System.out.println("Mifare");
                break;
            case "android.nfc.tech.MifareUltralight":
                System.out.println("Mifare Ultra Light");
                break;
            case "android.nfc.tech.NdefFormatable":
                System.out.println("Ndef Formatable");
                break;
        }
    }

    public void onTagDiscovered(Tag tag) {
        System.out.println("Tag discovered in reader mode");
        startGlove(tag);
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            //System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
}