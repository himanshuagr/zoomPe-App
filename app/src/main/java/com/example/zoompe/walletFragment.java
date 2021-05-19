package com.example.zoompe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.message.BasicHttpResponse;

public class walletFragment extends Fragment {

    TextView makepayment;
    TextView balance;
    EditText addmoney;
    Button proceed;
    SharedPreferences preferences;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_wallet,container,false);
        preferences = getActivity().getApplicationContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        makepayment = root.findViewById(R.id.makepayment);
        balance    = root.findViewById(R.id.walletbalancetext);
        addmoney = root.findViewById(R.id.editTextNumberSigned);
        proceed = root.findViewById(R.id.proceed);
        getWalletBalance();
        if(preferences.getInt("Balance",-1)!=-1)
            balance.setText("₹ "+preferences.getInt("Balance",-1));

        makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),nfc.class);
                startActivity(i);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount= Integer.parseInt(addmoney.getText().toString());


                if(amount==0)
                {
                    Activity activity = new Activity();
                    Toast.makeText(activity, "Amount should not be null", Toast.LENGTH_SHORT).show();
                    return;
                }
               String token = getToken();
                if(token==null)
                {
                    Toast.makeText(getActivity(), "Please login Again", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    return;
                }
                String url = "http://18.221.112.221:3000/payment/addmoneytowallet/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("amount",amount);
                client.addHeader("x-auth-token",token);
                ResponseHandlerInterface responseHandler = new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        Toast.makeText(getActivity(), "Unable to add money", Toast.LENGTH_SHORT).show();
                        System.out.println(responseString);

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                       Intent i = new Intent(getActivity(),paymentPage.class);
                       i.putExtra("page",responseString);
                       startActivity(i);

                    }
                };
                client.post(url,params,responseHandler);

            }
        });





        return root;
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
                int balance;
                try {
                   balance  = Integer.parseInt(response.getString("Balance"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    balance=-1;
                }
                edit.putInt("Balance",balance);
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
