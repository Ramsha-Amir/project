package com.example.ibra.oxp.activities.services;

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
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.activities.product.EditProduct;
import com.example.ibra.oxp.activities.services.AddService;
import com.example.ibra.oxp.activities.services.ViewServices;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddService extends Base{

        MyDatabaseHelper dbHelper;

        @BindView(R.id.toolbar)Toolbar toolbar;
        @BindView(R.id.etName)EditText name;
        @BindView(R.id.etDescription)EditText description;
        @BindView(R.id.btnAddService)Button addService;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            setContentView( R.layout.add_service );
            ButterKnife.bind( this );
            setSupportActionBar( toolbar );
            bottom();

            dbHelper = new MyDatabaseHelper( this );
            //dbHelper.getServiceData();
        }
        @OnClick(R.id.btnAddService)
        public void btn_add_service()
        {
            final String _name=name.getText().toString().trim();

            final String _description=description.getText().toString().trim();

            String email=getSharedPreferences("prefs",MODE_PRIVATE).getString("email",null);
            final MyService s = new MyService(_name, _description, email, null);

            StringBuilder sb = new StringBuilder(Config.OXP_URL);
            sb.append("simpleService/?");
            sb.append("name=%1$s");
            sb.append("&description=%2$s");
            sb.append("&email=%3$s");
            String completeURL = String.format(sb.toString(), _name, _description, email);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int id = response.getInt("data");
                        Toast.makeText(AddService.this, "Service has been added successfully.", Toast.LENGTH_LONG).show();
                        s.setId(id);
                        dbHelper.InsertService(s);
                        Intent i = new Intent(AddService.this, EditService.class);
                        i.putExtra("ServiceID", id);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( com.example.ibra.oxp.activities.services.AddService.this, "An error occurred while adding service.", Toast.LENGTH_SHORT).show();
                    Log.e("Test", error.toString());
                }
            });
            requestQueue.add(jsonRequest);
        }

        public void imageClicked(View view) {
            Toast.makeText(this, "Please add the service before uploading image.", Toast.LENGTH_SHORT).show();
        }



}
