package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class nfcpin extends AppCompatActivity {

    EditText pin;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcpin);
        getSupportActionBar().hide();

        pin = findViewById(R.id.pin);
        register = findViewById(R.id.registerpin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pin.getText().toString().length()<4)
                {
                    Toast.makeText(nfcpin.this, "Enter Valid pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = getIntent();
                String nfc = i.getStringExtra("NFC");
                registerNFC(nfc,pin.getText().toString());

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),home.class);
        startActivity(i);
    }

    public void registerNFC(String nfc,String pin)
    {

        String token = getToken();
        String url = "http://18.221.112.221:3000/nfc/registerNFC/";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("x-auth-token",token);
        System.out.println(nfc);
        params.put("nfcID",nfc);
        params.put("pin",pin);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Intent i =new Intent(getApplicationContext(),status.class);
                i.putExtra("status",true);
                i.putExtra("msg","Card Registered Successfully");
                startActivity(i);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Intent i =new Intent(getApplicationContext(),status.class);
                i.putExtra("status",false);
                try {
                    i.putExtra("msg",errorResponse.getString("msg"));
                    System.out.println(errorResponse.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    i.putExtra("msg","Unable to Register Card");
                }
                startActivity(i);
            }
        };
        client.post(url,params,responseHandler);

    }
    public String getToken()
    {
        SharedPreferences preferences;
        preferences = getApplicationContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        return preferences.getString("x-auth-token",null);
    }

}