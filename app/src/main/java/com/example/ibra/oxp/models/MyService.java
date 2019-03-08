package com.example.ibra.oxp.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.zip.DataFormatException;


public class MyService {


    private int Id;
    private String Name;
    private String Description;
    private String user;
    private ArrayList<String> images;
    private boolean isLoading = false;
    private boolean isNew = false;
    private String email;

    public MyService(MyService s) {
        this.Name = s.Name;
        this.Description = s.Description;
        this.user = s.user;

    }

    public MyService(String name, String description, String user, ArrayList<String> images) {
        this.Name = name;
        this.Description = description;
        this.user = user;
        this.images = images;

    }

    public MyService() {

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

    public String getUser() {
        return user;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public String getUserEmail() {
        return email;
    }

    public boolean isNew() {
        return isNew;
    }

}
