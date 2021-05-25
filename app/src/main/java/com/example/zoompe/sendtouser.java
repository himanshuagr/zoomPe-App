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

public class sendtouser extends AppCompatActivity {

    EditText amount,message,mobile;
    Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendtouser);
        getSupportActionBar().hide();
        amount = findViewById(R.id.edittextamount2);
        message = findViewById(R.id.edittextcomment2);
        mobile = findViewById(R.id.mobilenumber);
        send = findViewById(R.id.sendMoney);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(amount.getText().toString());
                if(a==0)
                {
                    Toast.makeText(sendtouser.this, "Enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobile.getText().toString().length()<10)
                {
                    Toast.makeText(sendtouser.this, "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                initiatePayment(mobile.getText().toString(),a);

            }
        });
    }

    public void initiatePayment(String mobile,int amount)
    {

        String token = getToken();
        String url = "http://18.221.112.221:3000/payment/walletTransfer/";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("x-auth-token",token);
        params.put("amount",amount);
        params.put("RecieverMobile",mobile);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Intent i =new Intent(getApplicationContext(),status.class);
                i.putExtra("status",true);
                i.putExtra("msg","Money Transferred");
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
                    i.putExtra("msg","Unable to Transfer Money");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),home.class);
        startActivity(i);
    }
}