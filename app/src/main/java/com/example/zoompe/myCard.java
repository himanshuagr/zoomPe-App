package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class myCard extends AppCompatActivity {

    SharedPreferences preferences;
    Button deactivate;
    TextView cardnumber,cardname,pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        getSupportActionBar().hide();
        cardnumber = findViewById(R.id.cardnumber);
        cardname =findViewById(R.id.cardname);
        pin =findViewById(R.id.pin3);
        preferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        if(preferences.getString("cardnumber",null)!=null)
        {
            cardnumber.setText(preferences.getString("cardnumber",null));
            cardname.setText(preferences.getString("cardname",null));
            pin.setText("PIN : "+preferences.getString("pin",null));

        }
     getMyCard();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(getApplicationContext(),home.class);
        startActivity(i);
    }
    public void  getMyCard()
    {
        String token= getToken();
        String url = "http://18.221.112.221:3000/user/getMyCard";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("x-auth-token",token);
        httpClient.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String name = response.getString("Name");
                    String p =response.getString("PIN");
                    String number = response.getString("CardNumber");
                    cardnumber.setText(number);
                    cardname.setText(name);
                    pin.setText("PIN : "+p);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cardnumber",number);
                    editor.putString("pin",p);
                    editor.putString("cardname",name);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println(errorResponse);

            }

        });

    }

    public String getToken()
    {

        return preferences.getString("x-auth-token",null);
    }
}