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

public class transferToBank extends AppCompatActivity {

    EditText accountname,accountnumber,ifsccode,amount;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_bank);
        getSupportActionBar().hide();
        accountname = findViewById(R.id.accountname);
        accountnumber = findViewById(R.id.accountnumber);
        ifsccode = findViewById(R.id.ifsccode);
        amount = findViewById(R.id.amount2);
        send = findViewById(R.id.sendMoney2);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(amount.getText().toString());
                if(a==0)
                {
                    Toast.makeText(transferToBank.this, "Enter Valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(accountname.getText().toString()==null||accountnumber.getText().toString().length()<10||ifsccode.getText().toString()==null)
                {
                    Toast.makeText(transferToBank.this, "Fill all details properly", Toast.LENGTH_SHORT).show();
                    return;
                }
                initiatePayment(accountname.getText().toString(),accountnumber.getText().toString(),ifsccode.getText().toString(),a);
            }
        });
    }
    public void initiatePayment(String accountname,String accountnumber,String ifsc,int amount)
    {

        String token = getToken();
        String url = "http://18.221.112.221:3000/payment/transferToBank/";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("x-auth-token",token);
        params.put("amount",amount);
        params.put("accountName",accountname);
        params.put("accountNumber",accountnumber);
        params.put("ifscCode",ifsc);
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Intent i =new Intent(getApplicationContext(),status.class);
                i.putExtra("status",true);
                i.putExtra("msg","Money Transferred To Bank");
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