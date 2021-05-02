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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class register_otp extends AppCompatActivity {


    Button next;
    ImageView backbutton;
    EditText editotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);
        getSupportActionBar().hide();
        next = findViewById(R.id.next);
        backbutton = findViewById(R.id.backbutton3);
        editotp = findViewById(R.id.edittextotp);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),register_mobile.class);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        String mobile = i.getStringExtra("mobile");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editotp.getText().toString();
                if(otp.length()<6)
                {
                    Toast.makeText(register_otp.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url="http://3.17.63.250:3000/user/registerVerifyOtp/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("mobile",mobile);
                params.put("otp",otp);
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if(statusCode==200)
                        {
                            Intent intent = new Intent(getApplicationContext(),register.class);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(register_otp.this, "unable to verify", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.println(errorResponse);
                        if(statusCode==401) {
                            try {
                                Toast.makeText(register_otp.this, errorResponse.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(register_otp.this, "unable to verify", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(register_otp.this, "unable to verify", Toast.LENGTH_SHORT).show();

                    }
                };
                client.post(url,params,responseHandler);
            }
        });

    }

    public  void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),register_mobile.class);
        startActivity(i);
    }
}