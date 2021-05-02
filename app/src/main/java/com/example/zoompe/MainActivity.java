 package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {
    Button button;
    TextView register;
     private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        if(preferences.getBoolean("LoggedIn",false))
        {

          Intent i =new Intent(getApplicationContext(),home.class);
           startActivity(i);

        }
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),login_mobile.class);
                startActivity(i);
            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),register_mobile.class);
                startActivity(i);
            }
        });

    }

   
     public void onBackPressed(){
         Intent a = new Intent(Intent.ACTION_MAIN);
         a.addCategory(Intent.CATEGORY_HOME);
         a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(a);
     }

 }