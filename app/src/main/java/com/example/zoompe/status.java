package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class status extends AppCompatActivity {

    ImageView statuslogo;
    ImageView back;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        getSupportActionBar().hide();
        statuslogo = findViewById(R.id.statuslogo);
        textView = findViewById(R.id.statusmsg);
        back = findViewById(R.id.backbutton8);
        Intent i =getIntent();
        if(i.getBooleanExtra("status",false)==false)
        {
            statuslogo.setImageResource(R.drawable.failed);


        }
        textView.setText(i.getStringExtra("msg"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),home.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),home.class);
        startActivity(i);
    }
}