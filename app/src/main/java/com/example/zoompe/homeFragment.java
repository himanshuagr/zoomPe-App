package com.example.zoompe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class homeFragment extends Fragment {

    androidx.appcompat.widget.Toolbar toolbar;
    BottomNavigationView navigationView;
    ImageView registerNFC;
    ImageView toself;
    ImageView toUser;
    ImageView toQr;
    ImageView toBank;
    ImageView wallet;
    ImageView history;
    TextView balance;
    SharedPreferences preferences;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
         View root=  inflater.inflate(R.layout.fragment_home,container,false);
        toolbar = getActivity().findViewById(R.id.mytoolbar);
        navigationView = getActivity().findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.home).setChecked(true);
        toolbar.setTitle("Dashboard");
        toolbar.setLogo(R.drawable.ic_baseline_home_24_w);
         registerNFC = root.findViewById(R.id.registerCard);
         toself = root.findViewById(R.id.toself);
         toUser = root.findViewById(R.id.toUser);
         toQr = root.findViewById(R.id.toQr);
         wallet = root.findViewById(R.id.wallet2);
         history = root.findViewById(R.id.history);
         balance = root.findViewById(R.id.walletbalance2);
        preferences = getActivity().getApplicationContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);

        if(preferences.getInt("Balance",-1)!=-1)
            balance.setText("Your Wallet Balance :  ₹"+preferences.getInt("Balance",-1));
         registerCard();
         setWallet();
         setToQr();
         setHistory();
         setToself();

         return  root;

    }

    @Override
    public void onResume() {
        super.onResume();
        getWalletBalance();
    }

    @Override
    public void onPause() {
        super.onPause();
        getWalletBalance();
    }
    public void setToself()
    {

        toself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),recievePayment.class);
                startActivity(i);
            }
        });
    }
    public void registerCard()
    {
        registerNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),registernfc.class);
                startActivity(i);
            }
        });
    }

    public  void setHistory()
    {
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new transactionFragment()).commit();
            }
        });
    }

    public void setWallet()
    {
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new walletFragment()).commit();

            }
        });
    }
    public void setToQr()
    {
        toQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new qrcodeFragment()).commit();
            }
        });
    }

    public void  getWalletBalance()
    {
        String token= getToken();
        String url = "http://18.221.112.221:3000/user/getWalletBalance/";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("x-auth-token",token);
        httpClient.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                SharedPreferences.Editor edit = preferences.edit();
                int b;
                try {
                    b  = Integer.parseInt(response.getString("Balance"));
                    balance.setText("Your Wallet Balance :  ₹"+b);
                } catch (JSONException e) {
                    e.printStackTrace();
                    b=-1;
                }
                edit.putInt("Balance",b);
                edit.commit();


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
