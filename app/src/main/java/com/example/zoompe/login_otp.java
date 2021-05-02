package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class login_otp extends AppCompatActivity {

    Button login;
    EditText otp;
    ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        getSupportActionBar().hide();
        otp = findViewById(R.id.editTextotp2);
        login = findViewById(R.id.login);
        backbutton = findViewById(R.id.backbutton5);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),login_mobile.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().length()<6)
                {
                    Toast.makeText(login_otp.this, "Enter valid otp", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = getIntent();
                String mobile = i.getStringExtra("mobile");
                String url = "http://3.17.63.250:3000/user/loginVerifyOtp/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("mobile",mobile);
                params.put("otp",otp.getText().toString());
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler()
                {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Intent i  = new Intent(getApplicationContext(),home.class);
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putBoolean("LoggedIn",true);
                        edit.putString("x-auth-token",headers[1].toString());
                        edit.commit();
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            Toast.makeText(login_otp.this, errorResponse.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(login_otp.this, "Unable to verify", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                client.post(url,params,responseHandler);
            }
        });
    }
    public  void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),login_mobile.class);
        startActivity(i);
    }
}