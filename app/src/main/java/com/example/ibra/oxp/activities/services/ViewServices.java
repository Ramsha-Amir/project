package com.example.ibra.oxp.activities.services;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.ibra.oxp.activities.product.ViewProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyService;
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

public class ViewServices extends Base {

    SwipeRefreshLayout mySwipeRefreshLayout;
    ServicesAdapter servicesAdapter;
    MyDatabaseHelper dbHelper;
    String reference_id;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_services);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        dbHelper = new MyDatabaseHelper(this);
        reference_id = getIntent().getStringExtra("user");
        reference_id = (reference_id == null) ? "" : "user/"+reference_id;

//        if(reference_id.equals("")) {
//            reference_id = getIntent().getStringExtra("category");
//            reference_id = (reference_id == null) ? "" : "category/" + reference_id;
//        }
        // dbHelper.getServiceData();


        //////////////////////////////////////////////////VIEWWWWW//////////////////////////////////////////////////////////

        //Bind RecyclerView from layout to recyclerViewServices object
        RecyclerView recyclerViewServices = findViewById(R.id.recyclerview_service);
        servicesAdapter = new ServicesAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewServices.setLayoutManager(gridLayoutManager);

        mySwipeRefreshLayout = findViewById(R.id.refresh_service);
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
                if (!servicesAdapter.loading)
                    feedData();
            }
        };

        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (servicesAdapter.getItemViewType(position)) {
                    case ServicesAdapter.SERVICE_ITEM:
                        return 1;
                    case ServicesAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        //add on on Scroll listener
        //recyclerViewServices.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewServices.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewServices.setAdapter(servicesAdapter);
        //load first page of recyclerview
        endlessScrollListener.onLoadMore(0, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean r = super.onCreateOptionsMenu(menu);
        if(!reference_id.isEmpty()) {
            menu.findItem(R.id.view_products).setVisible(true);
        }
        return r;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_products:
                Intent i = new Intent(getApplicationContext(), ViewProducts.class);
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
        servicesAdapter.showLoading();
        final List<MyService> services = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Config.OXP_URL + "getService/"+reference_id,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length = jsonArray.length();
//                    Log.e("test", jsonArray.toString());
                    for (int i = length - 1; i >= 0; i--) //////newly added services will be shown first
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");

                        String description = jsonObject.getString("description");
                        String user = jsonObject.getString("user_phone");
                        ArrayList<String> images = new ArrayList<>();
//                        Log.e("test", jsonObject.getJSONArray("images").toString());
                        for(int j = 0 ; j < jsonObject.getJSONArray("images").length() ; j++) {
                            images.add(jsonObject.getJSONArray("images").getString(j));
                        }
                        MyService service = new MyService(name, description, user, images);
                        service.setUserEmail(jsonObject.getString("user_email"));
                        service.setId(jsonObject.getInt("ID"));
                        services.add(service);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //hide loading
                            servicesAdapter.hideLoading();
                            //add services to recyclerview
                            servicesAdapter.addServices(services, true);
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

    public void addService(View v) {
        Intent i = new Intent(this, AddService.class);
        startActivity(i);
        finish();
    }
}


