package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class recievePayment extends AppCompatActivity {


    EditText amount,message;
    Button recieve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_payment);
        getSupportActionBar().hide();
        amount = findViewById(R.id.edittextamount);
        message = findViewById(R.id.edittextcomment);
        recieve = findViewById(R.id.makePayment2);

        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = Integer.parseInt(amount.getText().toString());
                if(a==0)
                {
                    Toast.makeText(recievePayment.this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getApplicationContext(),nfc.class);
                i.putExtra("amount",a);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(getApplicationContext(),home.class);
        startActivity(i);
    }
}