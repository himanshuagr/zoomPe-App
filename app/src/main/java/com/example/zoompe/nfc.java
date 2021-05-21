package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class nfc extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    TextView textView;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        getSupportActionBar().hide();
        Intent i =getIntent();
        amount = i.getIntExtra("amount",0);
        textView = findViewById(R.id.text5);
        textView.setText("Pay ₹ "+amount);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter==null)
        {
            Toast.makeText(this, "No nfc adaptor", Toast.LENGTH_SHORT).show();
            finish();
        }
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(getApplicationContext(),recievePayment.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter!=null;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            String s = detectTagData(tag);
            if(s!=null)
            {
                Intent i =getIntent();
                Intent intent1 = new Intent(getApplicationContext(),recievenfcpin.class);
                intent1.putExtra("nfc",s);
                intent1.putExtra("amount",amount);
                startActivity(intent1);

            }
        }
    }
    private String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toDec(id));
        Log.v("test",sb.toString());
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }



}