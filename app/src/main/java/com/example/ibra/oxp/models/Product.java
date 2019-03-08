package com.example.ibra.oxp.models;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amardeep on 1/4/2018.
 */

public class Product {
    public String DateToStr ;
    private int imageResourceId;
    private String productName;
    private String productPrice;
    private boolean isLoading = false;
    private boolean isNew = false;

    public Product(int imageResourceId, String productName, String productPrice, boolean isNew) {
        this.imageResourceId = imageResourceId;
        this.productName = productName;
        this.productPrice = productPrice;
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate=new Date();
        DateToStr=dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
        this.isNew = isNew;
    }

    public Product() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate=new Date();
        DateToStr=dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }
    public boolean isNew() {
        return isNew;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}

