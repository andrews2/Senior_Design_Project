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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
    ImageView spinningChip;
    private Context context;
    private Timer t;
    ScheduledExecutorService exec;
    String tagID;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://vast-springs-82374.herokuapp.com/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer();
        FrameLayout pageContainer = (FrameLayout) findViewById(R.id.page_container);
        getLayoutInflater().inflate(R.layout.activity_scan_chips, pageContainer);
        context = this;

        spinningChip = findViewById(R.id.spinning_chip);
        Glide.with(context).load(R.drawable.spinning_chip).into(spinningChip);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        spinningChip.setAnimation(rotateAnimation);



        // keep screen on
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        try{
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        } catch (Exception e){
            Toast.makeText(this, "This device does not have NFC capabilities", Toast.LENGTH_LONG).show();
            Intent nextIntent = new Intent(this, MainActivity.class);
            startActivity(nextIntent);
        }


        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device does not have NFC capabilities", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
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
                //when NFC wisp is read
                System.out.println("IsoDep Detected");
                NfcB nfcB = NfcB.get(tag);
                byte[] applicationData = nfcB.getApplicationData();
                byte[] tagIDbytes = tag.getId();  // Gets the ID of NFC Tag
                tagID = bytesToHexString(tagIDbytes);   // Converts tagID into readable format
                int chipValue;
                String dataString = bytesToHexString(applicationData);
                chipValue = Integer.parseInt(dataString.substring(3));

                HashMap<String, String> map = new HashMap<>();
                map.put("chipID", tagID);
                map.put("name", AccountDataHandler.getInstance().getUsername());

                //send server message to check chip ID
                Call<Void> call = retrofitInterface.executeAddChip(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // chip is not currently owned
                        if(response.code() == 200){
                            AccountDataHandler.getInstance().addToHistory(tagID, String.valueOf(chipValue));
                            AccountDataHandler.getInstance().addToAccountValue(chipValue);
                            AccountDataHandler.getInstance().incrementHistoryVersion();
                            File histIDs = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_ids.ser");
                            File histVals = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_vals.ser");
                            File histVersion = new File(getFilesDir(), AccountDataHandler.getInstance().getUsername() + "_version.ser");
                            new UpdateHistFiles(histIDs, histVals, histVersion);
                        }
                        // if chip is already owned
                        else if(response.code() == 400){
                            Toast.makeText(context, "Someone already owns this chip", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                    }
                });
                // Send NFCID:Uname to the server, and it'll add to the owned map
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