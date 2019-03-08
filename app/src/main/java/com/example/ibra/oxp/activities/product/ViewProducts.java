package com.example.ibra.oxp.activities.product;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.services.ViewServices;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.EndlessScrollListener;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewProducts extends Base {

    ProductsAdapter productsAdapter;
    MyDatabaseHelper dbHelper;
    String reference_id;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        dbHelper = new MyDatabaseHelper(this);
        reference_id = getIntent().getStringExtra("user");
        reference_id = (reference_id == null) ? "" : "user/"+reference_id;

        if(reference_id.equals("")) {
            reference_id = getIntent().getStringExtra("category");
            reference_id = (reference_id == null) ? "" : "category/" + reference_id;
        }
        // dbHelper.getProductData();


        //////////////////////////////////////////////////VIEWWWWW//////////////////////////////////////////////////////////

        //Bind RecyclerView from layout to recyclerViewProducts object
        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerview_product);
        productsAdapter = new ProductsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        mySwipeRefreshLayout = findViewById(R.id.refresh_product);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        feedData();
                    }
                }
        );

        //Crete new EndlessScrollListener fo endless recyclerview loading
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!productsAdapter.loading) {
                    feedData();
                }
            }
        };

        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (productsAdapter.getItemViewType(position)) {
                    case ProductsAdapter.PRODUCT_ITEM:
                        return 1;
                    case ProductsAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        //add on on Scroll listener
        //recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(productsAdapter);
        //load first page of recyclerview
        endlessScrollListener.onLoadMore(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean r = super.onCreateOptionsMenu(menu);
        if(!reference_id.isEmpty()) {
            menu.findItem(R.id.view_services).setVisible(true);
        }
        return r;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_services:
                Intent i = new Intent(getApplicationContext(), ViewServices.class);
                String user = getSharedPreferences("prefs",MODE_PRIVATE).getString("email", null);
                i.putExtra("user", user);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void feedData() {
        mySwipeRefreshLayout.setRefreshing(true);
        //show loading in recyclerview
        productsAdapter.showLoading();
        final List<MyProduct> products = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Config.OXP_URL + "get/"+reference_id,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length = jsonArray.length();
//                    Log.e("test", jsonArray.toString());
                    for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String description = jsonObject.getString("description");
                        int quantity = jsonObject.getInt("quantity");
                        String category = jsonObject.getString("category");
                        String user = jsonObject.getString("user_phone");
                        ArrayList<String> images = new ArrayList<>();
//                        Log.e("test", jsonObject.getJSONArray("images").toString());
                        for(int j = 0 ; j < jsonObject.getJSONArray("images").length() ; j++) {
                            images.add(jsonObject.getJSONArray("images").getString(j));
                        }
                        MyProduct product = new MyProduct(name, description, price, quantity, category, user, images);
                        product.setId(jsonObject.getInt("ID"));
                        product.setUserEmail(jsonObject.getString("user_email"));
                        products.add(product);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //hide loading
                            productsAdapter.hideLoading();
                            //add products to recyclerview
                            productsAdapter.addProducts(products, true);
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void addProduct(View v) {
        Intent i = new Intent(this, AddProduct.class);
        startActivity(i);
        finish();
    }
}


