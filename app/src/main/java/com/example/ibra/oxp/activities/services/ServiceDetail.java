package com.example.ibra.oxp.activities.services;

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
import com.example.ibra.oxp.activities.product.ImagePagerAdapter;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.ImageDownloaderTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetail extends Base {

    private int id;
    private String userEmail;

    @BindView(R.id.offer_view_name_text)
    TextView nameView;
    @BindView(R.id.offer_view_description_text)
    TextView descriptionView;
    @BindView(R.id.offer_view_phone_number_button)
    TextView phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottom();

        Bundle data = getIntent().getExtras();
        this.id = data.getInt("Service-ID", -1);
        String name = data.getString("Service-name", "Name");
        String desc = data.getString("Service-description", "Description");
        String phone = data.getString("Service-user", "Phone");
        this.userEmail = data.getString("Service-user-email", "Email");
        ArrayList<String> images = data.getStringArrayList("Service-images");

        nameView = findViewById(R.id.offer_view_name_text);
        descriptionView = findViewById(R.id.offer_view_description_text);
        phoneView = findViewById(R.id.offer_view_phone_number_button);

        nameView.setText("Name: "+name);
        descriptionView.setText(desc);
        phoneView.setText(phone);

        if(images.size() > 0) {
            ImageView imageView = findViewById(R.id.offer_view_main_image);
            imageView.setVisibility(View.GONE);
            ImagePagerAdapter adapter = new ImagePagerAdapter(this);
            adapter.setImages(images);

            ViewPager pager = findViewById(R.id.service_pager);
            pager.setVisibility(View.VISIBLE);
            pager.setAdapter(adapter);
            pager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean r = super.onCreateOptionsMenu(menu);
        if(userEmail.equals(getSharedPreferences("prefs",MODE_PRIVATE).getString("email", ""))) {
            menu.findItem(R.id.edit_service).setVisible(true);
            menu.findItem(R.id.delete_service).setVisible(true);
        }
        return r;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_service:
                editservice();
                return true;
            case R.id.delete_service:
                deleteService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editservice() {
        Intent i = new Intent(this, EditService.class);
        i.putExtra("ServiceID", id);
        startActivity(i);
        finish();
    }

    private void deleteService() {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to delete this service?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String completeURL =(Config.OXP_URL+"delService/");
                        RequestQueue requestQueue = Volley.newRequestQueue(ServiceDetail.this);
                        Map<String, Integer> params = new HashMap<String, Integer>();
                        params.put("id", id);
                        JSONObject parameters =new JSONObject( params );
                        Log.e( "abc", "onResponse:" + parameters.toString());

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, completeURL, parameters, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ServiceDetail.this, "Product has been deleted successfully.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ServiceDetail.this, ViewServices.class);
                                startActivity(i);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(ServiceDetail.this, "An error occurred during deletion.", Toast.LENGTH_SHORT).show();
                                Log.e("Test", error.toString());
                            }
                        });
                        requestQueue.add(jsonRequest);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
