package com.example.ibra.oxp.activities.product;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProduct extends Base {
    MyDatabaseHelper dbHelper;

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.add_prod_name)EditText name;
    @BindView(R.id.add_prod_price)EditText price;
    @BindView(R.id.add_prod_description)EditText description;
    @BindView(R.id.add_prod_quantity)EditText quantity;
    @BindView(R.id.add_offer_category_spinner)Spinner category;
    @BindView(R.id.add_prod_button)Button addProduct;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();

        dbHelper = new MyDatabaseHelper(this);
        //dbHelper.getProductData();
        ArrayAdapter<CharSequence> adapter = getCategoryAdapter(dbHelper, this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.add_offer_category_spinner);
        sItems.setAdapter(adapter);

    }

    @OnClick(R.id.add_prod_button)
    public void btn_add_product()
    {
        final String _name=name.getText().toString().trim();
        final String _price=price.getText().toString().trim();
        final String _description=description.getText().toString().trim();
        final int _quantity=Integer.parseInt(quantity.getText().toString().trim());
        final String _category=category.getSelectedItem().toString().trim();

        String email=getSharedPreferences("prefs",MODE_PRIVATE).getString("email",null);
        final MyProduct p = new MyProduct(_name, _description, _price, _quantity, _category, email, null);

        StringBuilder sb = new StringBuilder(Config.OXP_URL);
        sb.append("simple/?");
        sb.append("name=%1$s");
        sb.append("&price=%2$s");
        sb.append("&description=%3$s");
        sb.append("&quantity=%4$s");
        sb.append("&category=%5$s");
        sb.append("&email=%6$s");
        String completeURL = String.format(sb.toString(), _name, _price, _description, _quantity, _category, email);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("data");
                    Toast.makeText(AddProduct.this, "Product has been added successfully.", Toast.LENGTH_LONG).show();
                    p.setId(id);
                    dbHelper.InsertProduct(p);
                    Intent i = new Intent(AddProduct.this, EditProduct.class);
                    i.putExtra("ProductID", id);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProduct.this, "An error occurred while adding product.", Toast.LENGTH_SHORT).show();
                Log.e("Test", error.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }

    public void imageClicked(View view) {
        Toast.makeText(this, "Please add the product before uploading image.", Toast.LENGTH_SHORT).show();
    }

    public static ArrayAdapter<CharSequence> getCategoryAdapter(MyDatabaseHelper helper, Context context) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        adapter.addAll(helper.getCategories());
        return adapter;
    }

}
