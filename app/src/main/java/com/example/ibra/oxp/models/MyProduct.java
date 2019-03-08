package com.example.ibra.oxp.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.zip.DataFormatException;

public class MyProduct {

    public enum Category {
        CLOTHES, ELECTRONICS, FURNITURE, SPORT, MUSIC, BOOK, ANIMALS, SHOES, COSMETICS, ACCESSORIES, MOBILES, HomeAppliances, FashionBeauty, VEHICLES
    }

    public String DateToStr;
    private int Id;
    private String Name;
    private String Description;
    private String Price;
    private int Quantity;
    private boolean isLoading = false;
    private boolean isNew = false;
    private ArrayList<String> images;
    private String category;
    private String user;
    private String email;


    public MyProduct(MyProduct p) {
        this.Name = p.Name;
        this.Description = p.Description;
        this.Price = p.Price;
        this.Quantity = p.Quantity;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
        this.isNew = true;
        this.images = p.images;
        this.category = p.category;
        this.user = p.user;
    }

    public MyProduct(String name, String description, String price, int quantity, String category, String user, ArrayList<String> images) {
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.category = category;
        this.user = user;
        this.Quantity = quantity;
        this.images = images;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
        this.isNew = true;
    }

    public MyProduct() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getDescription() {
        return Description;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public String getUserEmail() {
        return email;
    }
}
