package com.example.ibra.oxp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.models.User;
import com.example.ibra.oxp.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyDatabaseHelper  extends Base {

    User u;
    MyDatabase myhelper;

    public MyDatabaseHelper(Context context) {
        myhelper = new MyDatabase(context);
    }

    ////////INSERT USER DATA////////////
    public long insertUserData(Integer id, String fname, String lname, String city, String email, String password, String address, Integer phonenu, String type) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();

        contentValues1.put(MyDatabase.USER_ID, id);
        contentValues1.put(MyDatabase.USER_FNAME, fname);
        contentValues1.put(MyDatabase.USER_LNAME, lname);
        contentValues1.put(MyDatabase.USER_CITY, city);
        contentValues1.put(MyDatabase.USER_EMAIL, email);
        contentValues1.put(MyDatabase.USER_PASSWORD, password);
        contentValues1.put(MyDatabase.USER_ADDRESS, address);
        contentValues1.put(MyDatabase.USER_PHONENU, phonenu);

        contentValues1.put(MyDatabase.USER_TYPE, type);

        long id2 = dbb.insert(MyDatabase.TABLE_USER, null, contentValues1);

        return id2;

    }

    public void InsertData(User u)
    {
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.USER_FNAME, u.get_fname());
        content_values.put(MyDatabase.USER_LNAME, u.get_lname());
        content_values.put(MyDatabase.USER_CITY, u.get_city());
        content_values.put(MyDatabase.USER_ADDRESS, u.get_address());
        content_values.put(MyDatabase.USER_PHONENU, u.get_phone());
        content_values.put(MyDatabase.USER_EMAIL, u.get_email());
        content_values.put(MyDatabase.USER_PASSWORD, u.get_password());
        long id = sql_db.insert(MyDatabase.TABLE_USER, null, content_values);


        sql_db.close();
        //return id;
    }

    public boolean validate_user(String email,String password)
    {
        boolean flag=false;
        String query = "SELECT* FROM "+myhelper.TABLE_USER+" WHERE ("+myhelper.USER_EMAIL+"='"+email+"' and "+myhelper.USER_PASSWORD+"='"+password+"')";
        SQLiteDatabase sql_db= myhelper.getReadableDatabase();
        Cursor cursor= sql_db.rawQuery(query,null);
        String[]data=null;
        if(cursor.getCount()!=0 && cursor.getCount()==1)
        {
             flag=true;
        }
        return flag;
    }

    //////////INSERT PRODUCT DATA////////////////

    public long insertProductData(Integer id, String name, Integer price, String description, Integer createdDate, Integer updatedDate, Integer quantity) {
        SQLiteDatabase dbbb = myhelper.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(MyDatabase.PRODUCT_ID, id);

        contentValues2.put(MyDatabase.PRODUCT_NAME, name);
        contentValues2.put(MyDatabase.PRODUCT_PRICE, price);
        contentValues2.put(MyDatabase.PRODUCT_DESCRIPTION, description);
        contentValues2.put(MyDatabase.PRODUCT_CREATEDDATE, createdDate);
        contentValues2.put(MyDatabase.PRODUCT_UPDATEDDATE, updatedDate);
        contentValues2.put(MyDatabase.PRODUCT_QUANTITY, quantity);
        long id1 = dbbb.insert(MyDatabase.TABLE_PRODUCTS, null, contentValues2);
        return id1;

    }
      //////////////////GET USER DATA//////////////



    public void InsertProduct( MyProduct p)
    {
        boolean flag=false;
        //long categoryId = getCategory(category);
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.PRODUCT_FK_EMAIL, p.getUser());
        content_values.put(MyDatabase.CATEGORY_ID, p.getCategory());
        content_values.put(MyDatabase.PRODUCT_NAME, p.getName());
        content_values.put(MyDatabase.PRODUCT_DESCRIPTION, p.getDescription());
        content_values.put(MyDatabase.PRODUCT_PRICE, p.getPrice());
        content_values.put(MyDatabase.PRODUCT_QUANTITY, p.getQuantity());

        long id = sql_db.insert(MyDatabase.TABLE_PRODUCTS, null, content_values);


        sql_db.close();
        //return id;
    }



    public void InsertService( MyService s)
    {
        boolean flag=false;
        //long categoryId = getCategory(category);
        //Toast.makeText(MyDatabaseHelper.this,u.get_fname()+"  "+u.get_lname()+""+u.get_city()+" "+u.get_address()+" "+u.get_phone()+" "+u.get_password()+" "+u.get_email(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase sql_db = myhelper.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        //content_values.put(dbHandler.user_id_col, i);
        content_values.put(MyDatabase.SERVICE_FK_EMAIL, s.getUser());
        content_values.put(MyDatabase.SERVICE_NAME, s.getName());
        content_values.put(MyDatabase.SERVICE_DESCRIPTION, s.getDescription());

        long id = sql_db.insert(MyDatabase.TABLE_SERVICES, null, content_values);


        sql_db.close();
        //return id;
    }

    public String getUserData ()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.USER_ID, MyDatabase.USER_FNAME, MyDatabase.USER_LNAME, MyDatabase.USER_CITY, MyDatabase.USER_EMAIL, MyDatabase.USER_PASSWORD, MyDatabase.USER_ADDRESS, MyDatabase.USER_PHONENU, MyDatabase.USER_TYPE};
        Cursor cursor = db.query(MyDatabase.TABLE_USER, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(MyDatabase.USER_ID));
            String fname = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_LNAME));
            String city = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_CITY));
            String email = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_PASSWORD));
            String address = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_ADDRESS));
            String phonenu = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_PHONENU));
            String type = cursor.getString(cursor.getColumnIndex(MyDatabase.USER_TYPE));

            buffer.append(cid + "   " + fname + "   " + lname + "   " + city + "   " + email + " " + address + "   " + phonenu + "   " + type + "   "+ password + " \n");
        }
        return buffer.toString();
    }

    /////////////// GET PRODUCT DATA ////////////////////////

    /*public String getProductData ()
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.PRODUCT_ID, MyDatabase.PRODUCT_NAME, MyDatabase.PRODUCT_PRICE, MyDatabase.PRODUCT_DESCRIPTION, MyDatabase.PRODUCT_CREATEDDATE, MyDatabase.PRODUCT_UPDATEDDATE, MyDatabase.PRODUCT_QUANTITY};
        Cursor cursor = dbb.query(MyDatabase.TABLE_PRODUCTS, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int ccid = cursor.getInt(cursor.getColumnIndex(MyDatabase.PRODUCT_ID));
            double price = cursor.getDouble(cursor.getColumnIndex(MyDatabase.PRODUCT_PRICE));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_DESCRIPTION));
            String createdDate = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_CREATEDDATE));
            String updatedDate = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_UPDATEDDATE));
            String quantity = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_QUANTITY));

            buffer.append(ccid + "   " + price + "   " + name + "   " + description + "   " + createdDate + " " + updatedDate + "   " + quantity + " \n");
        }
        return buffer.toString();
    }*/

    public void syncCategories(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String completeURL = Config.OXP_URL + "categories/";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SQLiteDatabase sql_db = myhelper.getWritableDatabase();
                sql_db.delete(MyDatabase.TABLE_CATEGORIES, null, null);
                try {
                    JSONArray data = response.getJSONArray("data");
                    for(int i = 0 ; i < data.length() ; i++) {
                        ContentValues content_values = new ContentValues();
                        content_values.put(MyDatabase.NAME, data.getJSONObject(i).getString("name"));
                        sql_db.insert(MyDatabase.TABLE_CATEGORIES, null, content_values);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sql_db.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Test", error.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.TABLE_CATEGORIES, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndex(MyDatabase.NAME)));
        }
        cursor.close();
        db.close();
        return categories;
    }

    public User getUser(String email) {
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + myhelper.TABLE_USER
                + " WHERE " + myhelper.USER_EMAIL + " = " + email;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        String type = c.getString(c.getColumnIndex(myhelper.USER_TYPE));
        String password = c.getString(c.getColumnIndex(myhelper.USER_PASSWORD));
        //String emai = c.getString(c.getColumnIndex(myhelper.USER_EMAIL));
        String fname = c.getString(c.getColumnIndex(myhelper.USER_FNAME));
        String lname = c.getString(c.getColumnIndex(myhelper.USER_LNAME));
        String city = c.getString(c.getColumnIndex(myhelper.USER_CITY));
        String address = c.getString(c.getColumnIndex(myhelper.USER_ADDRESS));
        String phone = c.getString(c.getColumnIndex(myhelper.USER_PHONENU));

        User user = new User(email, password, fname, lname, phone, city, address);
        //user.setUserId(id);
        c.close();
        db.close();
        return user;
    }

    public String getCategory(long id) {
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String selectQuery = "SELECT " + myhelper.NAME + " FROM " + myhelper.TABLE_CATEGORIES
                + " WHERE " + myhelper.CATEGORY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        String name = "";
        if(c.moveToFirst()){
            name = c.getString(c.getColumnIndex(myhelper.NAME));
        }
        c.close();
        db.close();
        return name;
    }

    // get category by name

    public long getCategory(String name) {
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String selectQuery = "SELECT " + myhelper.CATEGORY_ID + " FROM " + myhelper.TABLE_CATEGORIES
                + " WHERE " + myhelper.NAME + " = \"" + name + "\"";
        Cursor c = db.rawQuery( selectQuery, null );
        long id = 0;
        if (c.moveToFirst()) {
            id = c.getLong( c.getColumnIndex( myhelper.CATEGORY_ID ) );
        }
        c.close();
        return id;

    }

    public ArrayList<String> getImages(long offerId) {
        SQLiteDatabase db = myhelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + myhelper.TABLE_IMAGES
                + " WHERE " + myhelper.PRODUCT_ID + " = " + offerId;

        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<String> images = new ArrayList<String>();

        if(c.moveToFirst()){
            do{
                String image = c.getString(c.getColumnIndex(myhelper.NAME));

                images.add(image);
            } while(c.moveToNext());
        }
        c.close();
        return images;
    }


    //public ArrayList<MyProduct> getProductData2 (ArrayList<MyProduct>list)
     public void getProductData2 (ArrayList<MyProduct>list)
    {
        //ArrayList<MyProduct> list=new ArrayList<MyProduct>();
        //list=new ArrayList<MyProduct>();
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        String[] columns = {MyDatabase.PRODUCT_ID,MyDatabase.PRODUCT_FK_EMAIL,MyDatabase.CATEGORY_ID, MyDatabase.PRODUCT_NAME, MyDatabase.PRODUCT_PRICE, MyDatabase.PRODUCT_DESCRIPTION, MyDatabase.PRODUCT_CREATEDDATE, MyDatabase.PRODUCT_UPDATEDDATE, MyDatabase.PRODUCT_QUANTITY};
        Cursor cursor = dbb.query(MyDatabase.TABLE_PRODUCTS, columns, null, null, null, null, null);
        //StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int ccid = cursor.getInt(cursor.getColumnIndex(MyDatabase.PRODUCT_ID));
            int category = cursor.getInt(cursor.getColumnIndex(MyDatabase.CATEGORY_ID));
            String  user = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_FK_EMAIL));
            String  price = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_PRICE));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_NAME));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.PRODUCT_DESCRIPTION));
            int quantity = cursor.getInt(cursor.getColumnIndex(MyDatabase.PRODUCT_QUANTITY));
            u = getUser(user);
            String c = getCategory(category);
            ArrayList<String> images = getImages(ccid);
            MyProduct p = new MyProduct(user, description, price, quantity, c, u.get_phone(), images);
            list.add(p);
           // buffer.append(ccid + "   " + price + "   " + name + "   " + description + "   " + createdDate + " " + updatedDate + "   " + quantity + " \n");
        }
        //return list;
    }
}
