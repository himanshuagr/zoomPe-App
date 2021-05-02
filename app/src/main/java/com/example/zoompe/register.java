package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class register extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText email;
    EditText date;
    ImageView backbutton;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        firstname = findViewById(R.id.editTextfistname);
        lastname = findViewById(R.id.editTextTextLastName);
        email = findViewById(R.id.editTextTextEmailAddress);
        date = findViewById(R.id.editTextDate);
        backbutton = findViewById(R.id.backbutton4);
        register = findViewById(R.id.register);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),register_otp.class);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        String mobile = i.getStringExtra("mobile");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(firstname.getText().toString().length()==0||lastname.getText().toString().length()==0||email.getText().toString().length()==0||date.getText().toString().length()==0)
                 {
                     Toast.makeText(register.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                     return;
                 }
                String url="http://3.17.63.250:3000/user/register/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("mobile",mobile);
                params.put("FirstName",firstname.getText().toString());
                params.put("LastName",lastname.getText().toString());
                params.put("Email",email.getText().toString());
                params.put("DOB",date.getText().toString());
                JsonHttpResponseHandler responseHandler =new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Intent i = new Intent(getApplicationContext(),home.class);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();;
                        edit.putBoolean("LoggedIn",true);
                        edit.putString("x-auth-token",headers[1].toString());
                        edit.commit();
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.println("failure");
                        try {
                            Toast.makeText(register.this, errorResponse.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(register.this, "Unable to register", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                client.post(url,params,responseHandler);
            }
        });


    }
    public  void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),register_otp.class);
        startActivity(i);
    }
}