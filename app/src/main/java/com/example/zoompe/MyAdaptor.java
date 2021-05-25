package com.example.zoompe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder> {


    private ArrayList<MyListData> transactionList;
    public MyAdaptor(ArrayList<MyListData> transactionList)
    {
        this.transactionList=transactionList;
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_design,parent,false);
       ViewHolder viewHolder = new ViewHolder(listItem);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        int logo = transactionList.get(position).getLogo();
        String type = transactionList.get(position).getType();
        String amount = transactionList.get(position).getAmount();
        String date = transactionList.get(position).getDate();
        String ref = transactionList.get(position).getRef();
        String tid = transactionList.get(position).getTid();
        holder.setData(logo,type,amount,date,ref,tid);



    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView logoView;
        private TextView typeView;
        private TextView amountView;
        private TextView dateView;
        private TextView refView;
        private TextView tidView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            logoView = itemView.findViewById(R.id.logo);
            typeView = itemView.findViewById(R.id.type);
            refView = itemView.findViewById(R.id.ref);
            amountView = itemView.findViewById(R.id.amount);
            dateView = itemView.findViewById(R.id.date);
            tidView = itemView.findViewById(R.id.transactionId);
        }

        public void setData(int logo, String type, String amount, String date, String ref, String tid) {

            logoView.setImageResource(logo);
            typeView.setText(type);
            amountView.setText(amount);
            dateView.setText(date);
            refView.setText(ref);
            tidView.setText(tid);
        }
    }
}
