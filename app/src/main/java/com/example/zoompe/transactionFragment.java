package com.example.zoompe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cz.msebera.android.httpclient.Header;

public class transactionFragment extends Fragment {

    androidx.appcompat.widget.Toolbar toolbar;
    BottomNavigationView navigationView;
    TextView message;
    RecyclerView recyclerView;
    SharedPreferences preferences;
    ArrayList<MyListData> list;
    MyAdaptor adaptor;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transaction,container,false);
        toolbar = getActivity().findViewById(R.id.mytoolbar);
        toolbar.setTitle("Transactions");
        toolbar.setLogo(R.drawable.ic_baseline_history_24_w);
        navigationView = getActivity().findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.history).setChecked(true);
        message = root.findViewById(R.id.history2);
        message.setVisibility(View.INVISIBLE);
        recyclerView = root.findViewById(R.id.recyclerview);
        preferences = getActivity().getApplicationContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        list = new ArrayList<>();
        loadData();
        adaptor = new MyAdaptor(list);
        LinearLayoutManager layoutManager =new LinearLayoutManager(root.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        getTransactions(list.size());
        adaptor.notifyDataSetChanged();



        return root;
    }

    public void loadData()
    {
        Gson gson = new Gson();
        String json = preferences.getString("transactions",null);
        Type type = new TypeToken<ArrayList<MyListData>>(){}.getType();
        if(json!=null)
        {
            list = gson.fromJson(json,type);
        }
    }
    public void getTransactions(int index)
    {

        String token = getToken();
        String url = "http://18.221.112.221:3000/user/getTransactionDetails/";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("x-auth-token",token);
        params.put("index",index);

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if(response.length()!=0)
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    for(int i=0;i<response.length();i++)
                    {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            int credit = object.getInt("Credit");
                            int debit = object.getInt("Debit");
                            String ref = object.getString("Ref");
                            String Time = object.getString("Time");
                            String tid = object.getString("TransactionId");
                            String type = object.getString("Type");
                            int logo = 0;
                            String typeof="";
                            String amount="";
                            String date="";
                            String reference = "";
                            String id=tid;
                            System.out.println(type);

                            if(type.compareTo("W2W")==0)
                            {
                                if(credit==0) {
                                    logo = R.drawable.ic_baseline_north_send;
                                    typeof = "Paid to";
                                    reference=ref;
                                    amount="-₹"+debit;
                                }
                                else {
                                    logo = R.drawable.ic_baseline_south_recieve;
                                    typeof = "Recieved From";
                                    reference=ref;
                                    amount="+₹"+credit;
                                }
                            }
                            if(type.compareTo("W2A")==0) {
                                logo = R.drawable.ic_baseline_account_bank;
                                typeof = "Money Withdrawn";
                                reference=ref;
                                amount="-₹"+debit;

                            }
                            if(type.compareTo("ADD2W")==0) {
                                logo = R.drawable.ic_baseline_account_wallet;
                                typeof = "Money Added";
                                amount="+₹"+credit;
                                reference="To Wallet";
                            }
                            if(type.compareTo("N2W")==0) {
                                if(credit==0) {
                                    logo = R.drawable.ic_baseline_north_send;
                                    typeof = "Paid using card";
                                    reference=ref;
                                    amount="-₹"+debit;
                                }
                                else {
                                    logo = R.drawable.ic_baseline_south_recieve;
                                    typeof = "Recieved by card";
                                    reference=ref;
                                    amount="+₹"+credit;
                                }
                            }
                            String[] s=Time.split("T");
                            date = s[0];
                            list.add(0,new MyListData(logo,typeof,amount,date,reference,id));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    editor.putString("transactions",json);
                    editor.commit();
                    adaptor.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        };
        client.post(url,params,responseHandler);

    }
    public String getToken()
    {

        return preferences.getString("x-auth-token",null);
    }



}
