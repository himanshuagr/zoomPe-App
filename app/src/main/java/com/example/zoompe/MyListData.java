package com.example.zoompe;

public class MyListData {

    private int logo;
    private String type;
    private String amount;
    private String date;
    private String ref;
    private String tid;

    MyListData(int logo,String type,String amount,String date,String ref, String tid)
    {
        this.logo=logo;
        this.type=type;
        this.amount=amount;
        this.date=date;
        this.ref=ref;
        this.tid=tid;

    }

    public int getLogo() {
        return logo;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getRef() {
        return ref;
    }

    public String getTid() {
        return tid;
    }
}
