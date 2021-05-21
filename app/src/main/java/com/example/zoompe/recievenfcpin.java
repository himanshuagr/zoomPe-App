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

public class recievenfcpin extends AppCompatActivity {

    EditText pin;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recievenfcpin);
        pin = findViewById(R.id.pin2);
        button = findViewById(R.id.registerpin2);
        getSupportActionBar().hide();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pin.getText().toString().length()<4)
                {
                    Toast.makeText(recievenfcpin.this, "Enter Valid Pin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i =getIntent();
                String nfc = i.getStringExtra("nfc");
                int amount = i.getIntExtra("amount",0);

                makepayment(nfc,amount,pin.getText().toString());



            }
        });
    }

    public  void makepayment(String nfc,int amount,String pin)
    {
        String token = getToken();
        String url = "http://18.221.112.221:3000/nfc/getPaymentByNFC/";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("x-auth-token",token);
        System.out.println(nfc);
        params.put("nfcID",nfc);
        params.put("pin",pin);
        params.put("amount",amount);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Intent i =new Intent(getApplicationContext(),status.class);
                i.putExtra("status",true);
                i.putExtra("msg","Payment Successful");
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
                    i.putExtra("msg","Unable to make payment");
                }
                startActivity(i);
            }
        };
        client.post(url,params,responseHandler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(getApplicationContext(),recievePayment.class);
        startActivity(i);
    }
    public String getToken()
    {
        SharedPreferences preferences;
        preferences = getApplicationContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        return preferences.getString("x-auth-token",null);
    }
}