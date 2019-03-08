package com.example.ibra.oxp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

//import static android.drm.DrmStore.DrmObjectType.CONTENT;
import static android.drm.DrmStore.DrmObjectType.CONTENT;
import static android.provider.MediaStore.Audio.Playlists.Members._ID;

public class MyDatabase extends SQLiteOpenHelper {
    //private static DatabaseHelper instance;
    private Context context;

    public static final String DATABASE_NAME = "OXP_DATABASE.db";    // Database Name
    public static final String TABLE_USER = "User";          //table name
    public static final String TABLE_PRODUCTS = "Products";// Table Name
    //public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_SERVICES = "Services";
    public static final String TABLE_POST = "Post";
    public static final String TABLE_COMMENT = "Comment";
    public static final String TABLE_IMAGES = "images";
    private static final int DATABASE_Version = 1;

    ///////////USER///////////////////////

    public static final String USER_ID = "Id";
    public static final String USER_FNAME = "F_Name";
    public static final String USER_LNAME = "L_Name";
    public static final String USER_EMAIL = "Email";
    public static final String USER_CITY = "City";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_ADDRESS = "Address";
    public static final String USER_PHONENU = "Phone_No";
    public static final String USER_TYPE = "Type";

    /////////////PRODUCTS///////////////////////

    public static final String PRODUCT_ID = "Id";
    public static final String PRODUCT_FK_EMAIL = "User_Email";
    public static final String PRODUCT_NAME = "Name";
    public static final String PRODUCT_PRICE = "Price";
    public static final String PRODUCT_DESCRIPTION = "Description";
    public static final String PRODUCT_CREATEDDATE = "Created_Date";
    public static final String PRODUCT_UPDATEDDATE = "Updated_Date";
    public static final String PRODUCT_QUANTITY = "Quantity";

    /////////////SERVICE////////////////////////

    public static final String SERVICE_ID = "Id";
    public static final String SERVICE_FK_EMAIL = "User_Email";
    public static final String SERVICE_NAME = "Name";
    public static final String SERVICE_DESCRIPTION = "Description";

    ////////////////POST////////////////////
    public static final String POST_ID = "id";
    public static final String POST_FK_EMAIL = "user_email";
    public static final String POST_NAME = "name";
    public static final String POST_LIKES = "likes";
    public static final String POST_DESCRIPTION = "description";

    ////////////////COMMENT////////////////////
    public static final String COMMENT_ID = "id";
    public static final String COMMENT_FK_EMAIL = "user_email";
    public static final String COMMENT_FK_POST = "post_name";
    public static final String COMMENT_NAME = "name";
    public static final String COMMENT_DESCRIPTION = "description";

    // CATEGORIES table columns
    public static final String NAME = "name";
    public static final String CATEGORY_ID = "category_id";

    //Categories
    private static final String CLOTHES = "Clothes";
    private static final String ELECTRONICS = "Electronics";
    private static final String FURNITURE = "Furniture";
    private static final String SPORT = "Sport";
    private static final String MUSIC = "Music";
    private static final String BOOKS = "Books";
    private static final String ANIMALS = "Animals";
    private static final String VEHICLES = "VEHICLES";
    private static final String HomeAppliances = "Home Appliances";
    private static final String MOBILES = "MOBILES";
    private static final String SHOES = "Shoes";
    private static final String FashionBeauty = "FASHION";
    private static final String COSMETICS = "Cosmetics";
    private static final String ACCESSORIES = "Accessories";
    private static final String AUTO = "Auto";

//    // MESSAGES table columns
//    public  static final String MESSAGE_ID = "message_id";
//    public  static final String RECEIVER_ID = "receiver_id";
//    public  static final String SENDER_ID = "sender_id";

    //IMAGES table columns
    public static final String IMAGE_ID = "image_id";
    public static final String IS_MAIN = "is_main";

    ////////CREATE TABLE OF USER ///////////////

    private static final String CREATE_USER_TABLE ="CREATE TABLE "
            + TABLE_USER + "(" + USER_ID
            + " TEXT PRIMARY KEY,"
            + USER_EMAIL +" TEXT UNIQUE,"
            + USER_FNAME + " TEXT,"
            + USER_LNAME +" TEXT,"
            + USER_CITY +" TEXT,"
            + USER_PASSWORD+" TEXT,"
            + USER_TYPE+" TEXT,"
            + USER_ADDRESS +" TEXT,"
            + USER_PHONENU +" TEXT  );";


    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +TABLE_CATEGORIES + " ("
            + CATEGORY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " VARCHAR(255) NOT NULL "
            +") ";

    /////////////CREATE TABLE OF PRODUCTS//////////////////
    private static final String CREATE_PRODUCT_TABLE= "CREATE TABLE "
            + TABLE_PRODUCTS + "(" + PRODUCT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_FK_EMAIL+" TEXT,"
            + CATEGORY_ID + " TEXT, "
            + PRODUCT_NAME+" TEXT,"
            + PRODUCT_PRICE +" TEXT,"
            + PRODUCT_DESCRIPTION+" TEXT,"
            + PRODUCT_CREATEDDATE +" DATE,"
            + PRODUCT_QUANTITY +" INTEGER,"
            + PRODUCT_UPDATEDDATE +" DATE,"
//            + "FOREIGN KEY ("+CATEGORY_ID+") REFERENCES "+TABLE_CATEGORIES+" ("+CATEGORY_ID +"),"
            + "FOREIGN KEY ("+PRODUCT_FK_EMAIL+") REFERENCES "+TABLE_USER+" ("+USER_ID+") );";

    /////////////CREATE TABLE OF SERVICES//////////////////
    private static final String CREATE_SERVICES_TABLE= "CREATE TABLE "
            + TABLE_SERVICES + "(" + SERVICE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SERVICE_FK_EMAIL+" TEXT,"
            + SERVICE_NAME+" TEXT,"
            + SERVICE_DESCRIPTION+" TEXT,"
            + "FOREIGN KEY ("+SERVICE_FK_EMAIL+") REFERENCES "+TABLE_USER+" ("+USER_ID+") );";

    /////////////CREATE TABLE OF POST//////////////////
    private static final String CREATE_POST_TABLE= "CREATE TABLE "
            + TABLE_POST + "(" + POST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + POST_FK_EMAIL+" TEXT,"
            + POST_NAME+" TEXT,"
            + POST_DESCRIPTION+" TEXT,"
            + POST_LIKES+" INTEGER,"
            + "FOREIGN KEY ("+POST_FK_EMAIL+") REFERENCES "+TABLE_USER+" ("+USER_ID+") );";

    /////////////CREATE TABLE OF COMMENT//////////////////
    private static final String CREATE_COMMENT_TABLE= "CREATE TABLE "
            + TABLE_COMMENT + "(" + COMMENT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMMENT_FK_EMAIL+" TEXT,"
            + COMMENT_NAME+" TEXT,"
            + COMMENT_DESCRIPTION+" TEXT,"
            + COMMENT_FK_POST+" TEXT,"
            + "FOREIGN KEY ("+COMMENT_FK_POST+") REFERENCES "+TABLE_POST+" ("+POST_ID+") );";
           // + "FOREIGN KEY ("+COMMENT_FK_EMAIL+") REFERENCES "+TABLE_USER+" ("+USER_ID+") );";

    ////////////////////CREATE TABLE OF IMAGES/////////////////////
    private static final String CREATE_IMAGES_TABLE =  "CREATE TABLE IF NOT EXISTS " +TABLE_IMAGES + " ("
            + IMAGE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRODUCT_ID + " INTEGER, "
            + NAME + " STRING, "
            //+ CONTENT + " BLOB NOT NULL, "
            + IS_MAIN + " BOOL NOT NULL, "
            + "FOREIGN KEY ("+ PRODUCT_ID +") REFERENCES "+ TABLE_PRODUCTS +"("+ PRODUCT_ID+")"
            +") ";

    ///////// DROP TABLE IF EXIST //////////

    private static final String DROP_USER_TABLE ="DROP TABLE IF EXISTS "+TABLE_USER;
    private static final String DROP_PRODUCT_TABLE ="DROP TABLE IF EXISTS "+TABLE_PRODUCTS;
    private static final String DROP_SERVICES_TABLE ="DROP TABLE IF EXISTS "+TABLE_SERVICES;
    private static final String DROP_CATEGORIES_TABLE ="DROP TABLE IF EXISTS "+TABLE_CATEGORIES;
    private static final String DROP_IMAGES_TABLE ="DROP TABLE IF EXISTS "+TABLE_IMAGES;
    private static final String DROP_COMMENT_TABLE ="DROP TABLE IF EXISTS "+TABLE_COMMENT;
    private static final String DROP_POST_TABLE ="DROP TABLE IF EXISTS "+TABLE_POST;
   // private static final String DROP_IMAGE_TABLE ="DROP TABLE IF EXISTS "+TABLE_IMAGE;
    //private Context context;

    ///////CONSTRUCTOR////////

    public MyDatabase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_SERVICES_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_POST_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PRODUCT_TABLE );
        db.execSQL(DROP_SERVICES_TABLE );
        db.execSQL(DROP_POST_TABLE);
        db.execSQL(DROP_COMMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        onCreate(db);

    }
    public void addCategories(SQLiteDatabase db){
        ArrayList<String> cat = new ArrayList<String>();
        cat.add(ELECTRONICS);
        cat.add(FURNITURE);
        cat.add(CLOTHES);
        cat.add(SPORT);
        cat.add(MUSIC);
        cat.add(BOOKS);
        cat.add(ANIMALS);
        cat.add(VEHICLES);
        cat.add(MOBILES);
        cat.add(HomeAppliances);
        cat.add(FashionBeauty);
        cat.add(COSMETICS);
        cat.add(ACCESSORIES);
        cat.add(SHOES);
        cat.add(AUTO);

        for(String c : cat){
            ContentValues vals = new ContentValues();
            vals.put(NAME, c);
            db.insert(TABLE_CATEGORIES, null, vals);
        }

    }
//    //IMAGES
//    //create image
//    public long createImage(byte[] array, int ProductId, String name){
//        SQLiteDatabase db = this.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(NAME, name);
//        values.put(PRODUCT_ID, ProductId);
//        values.put(CONTENT, array);
//
//        long id = db.insert(TABLE_IMAGES, null, values);
//        return id;
//    }
//
//    public boolean checkCategory(String name){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_CATEGORIES
//                + " WHERE " + NAME + " = " + name;
//
//        Cursor c = db.rawQuery(query, null);
//        if(c != null)
//            return true;
//        else
//            return false;
//
//    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}

