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

public class register_mobile extends AppCompatActivity {
    ImageView backbutton;
    Button getOTP;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);
        getSupportActionBar().hide();
        backbutton = findViewById(R.id.backbutton2);
        getOTP = findViewById(R.id.getOTP2);
        number = findViewById(R.id.editTextPhone2);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = number.getText().toString();
                if(num.length()<10)
                {
                    Toast.makeText(register_mobile.this, "Enter Valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url="http://3.17.63.250:3000/user/registerUserByMobile/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("mobile",num);
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if(statusCode==200)
                        {
                            Intent intent =new Intent(getApplicationContext(),register_otp.class);
                            intent.putExtra("mobile",num);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(register_mobile.this, "Unable to send otp", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.println(errorResponse);
                        try {
                            Toast.makeText(register_mobile.this, errorResponse.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(register_mobile.this, "something wrong happens", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                client.post(url,params,responseHandler);
            }
        });
    }
    public  void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}