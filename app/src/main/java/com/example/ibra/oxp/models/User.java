package com.example.ibra.oxp.models;

public class User {
   /* private String FirstName;
    private String LastName;
    private String City;
    private String Address;
    private String PhoneNumber;
    private String Email;
    private String Password;*/
    public String FirstName;
    public String LastName;
    public String City;
    public String Address;
    public String PhoneNumber;
    public String Email;
    public String Password;



    User()
    {

    }

    public User(  String FirstName, String LastName, String City, String Address, String PhoneNumber,String Email,String Password)
    {
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.City=City;
        this.Address=Address;
        this.PhoneNumber=PhoneNumber;
        this.Email=Email;
        this.Password=Password;

    }

    public String get_fname() { return FirstName;}
    public String get_lname() { return LastName;}
    public String get_city() { return City;}
    public String get_address() { return Address;}
    public String get_email() { return Email;}
    public String get_phone() { return PhoneNumber;}
    public String get_password() { return Password;}
}
