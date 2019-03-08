package com.example.ibra.oxp.models;

import java.io.Serializable;

public class MyPost implements Serializable {
    private int Id;
    private String Name;
    private String Description;
    private int Likes;
    private boolean isLoading = false;


    public MyPost(MyPost p) {
        this.Id=p.Id;
        this.Name = p.Name;
        this.Description = p.Description;
        this.Likes = p.Likes;
    }
    public MyPost(String name, String description) {
        this.Name = name;
        this.Description = description;
    }
    public MyPost(int id, String name, String description, int likes) {
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Likes = likes;
    }

    public MyPost() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}

