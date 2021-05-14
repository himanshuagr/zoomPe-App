package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class login_mobile extends AppCompatActivity {


    ImageView backbutton;
    Button getOTP;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        getSupportActionBar().hide();
        backbutton = findViewById(R.id.backbutton);
        number = findViewById(R.id.editTextPhone);
        getOTP = findViewById(R.id.getOTP);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent i = new Intent(getApplicationContext(),MainActivity.class);
                 startActivity(i);
            }
        });
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString()==null)
                    Toast.makeText(login_mobile.this, "Enter the number", Toast.LENGTH_SHORT).show();
                else
                {
                    String url="http://18.221.112.221:3000/user/loginByOtp/";
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("mobile",number.getText().toString());
                    JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Intent intent = new Intent(getApplicationContext(),login_otp.class);
                            intent.putExtra("mobile",number.getText().toString());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                Toast.makeText(login_mobile.this, errorResponse.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(login_mobile.this, "unable to send otp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    client.post(url,params,responseHandler);
                }
            }
        });



    }
    public  void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}