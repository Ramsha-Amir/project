package com.example.ibra.oxp.activities.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.ImageDownloaderTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetail extends Base {

    private int id;
    private String userEmail;

    @BindView(R.id.offer_view_name_text)
    TextView nameView;
    @BindView(R.id.offer_view_price_text)
    TextView priceView;
    @BindView(R.id.offer_view_quantity_text)
    TextView quantityView;
    @BindView(R.id.offer_view_description_text)
    TextView descriptionView;
    @BindView(R.id.offer_view_phone_number_button)
    TextView phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottom();

        Bundle data = getIntent().getExtras();
        this.id = data.getInt("Product-ID", -1);
        String name = data.getString("Product-name", "Name");
        String price = data.getString("Product-price", "Price");
        int qty = data.getInt("Product-quantity", 0);
        String desc = data.getString("Product-description", "Description");
        String phone = data.getString("Product-user", "Phone");
        this.userEmail = data.getString("Product-user-email", "Email");
        ArrayList<String> images = data.getStringArrayList("Product-images");

        nameView = findViewById(R.id.offer_view_name_text);
        priceView = findViewById(R.id.offer_view_price_text);
        quantityView = findViewById(R.id.offer_view_quantity_text);
        descriptionView = findViewById(R.id.offer_view_description_text);
        phoneView = findViewById(R.id.offer_view_phone_number_button);

        nameView.setText("Name: "+name);
        priceView.setText("Rs: "+price );
        quantityView.setText("Quantity: "+qty+"");
        descriptionView.setText(desc);
        phoneView.setText(phone);

        if(images.size() > 0) {
            ImageView imageView = findViewById(R.id.offer_view_main_image);
            imageView.setVisibility(View.GONE);
            ImagePagerAdapter adapter = new ImagePagerAdapter(this);
            adapter.setImages(images);

            ViewPager pager = findViewById(R.id.product_pager);
            pager.setVisibility(View.VISIBLE);
            pager.setAdapter(adapter);
            pager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean r = super.onCreateOptionsMenu(menu);
        if(userEmail.equals(getSharedPreferences("prefs",MODE_PRIVATE).getString("email", ""))) {
            menu.findItem(R.id.edit_product).setVisible(true);
            menu.findItem(R.id.delete_product).setVisible(true);
        }
        return r;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_product:
                editProduct();
                return true;
            case R.id.delete_product:
                deleteProduct();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editProduct() {
        Intent i = new Intent(this, EditProduct.class);
        i.putExtra("ProductID", id);
        startActivity(i);
        finish();
    }

    private void deleteProduct() {
        new AlertDialog.Builder(this)
            .setTitle("Title")
            .setMessage("Do you really want to delete this product?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String completeURL =(Config.OXP_URL+"del/");
                    RequestQueue requestQueue = Volley.newRequestQueue(ProductDetail.this);
                    Map<String, Integer> params = new HashMap<String, Integer>();
                    params.put("id", id);
                    JSONObject parameters =new JSONObject( params );
                    Log.e( "abc", "onResponse:" + parameters.toString());

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, completeURL, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ProductDetail.this, "Product has been deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ProductDetail.this, ViewProducts.class);
                            startActivity(i);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        Toast.makeText(ProductDetail.this, "An error occurred during deletion.", Toast.LENGTH_SHORT).show();
                        Log.e("Test", error.toString());
                        }
                    });
                    requestQueue.add(jsonRequest);
                }})
            .setNegativeButton(android.R.string.no, null).show();
    }
}
