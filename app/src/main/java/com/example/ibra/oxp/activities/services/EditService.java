package com.example.ibra.oxp.activities.services;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.ImageDownloaderTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditService extends Base{

        public static final int PICK_IMAGE = 1;
        private int id;
        private int currTag = 0;

        MyDatabaseHelper dbHelper;
        @BindView(R.id.toolbar)Toolbar toolbar;
        @BindView(R.id.etName)EditText name;
        @BindView(R.id.etDescription)EditText description;
        @BindView(R.id.btnAddService)Button addService;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_service);
            ButterKnife.bind(this);
            setSupportActionBar(toolbar);
            bottom();

            addService.setText("UPDATE SERVICE");
            id = getIntent().getIntExtra("ServiceID", 0);

            dbHelper = new MyDatabaseHelper(this);
            //dbHelper.getServiceData();
            feedData();
        }

        private void feedData() {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    Config.OXP_URL + "service/"+id+"/",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        String user = jsonObject.getString("user_phone");
                        ArrayList<String> images = new ArrayList<>();
                        for(int j = 0 ; j < jsonObject.getJSONArray("images").length() ; j++) {
                            images.add(jsonObject.getJSONArray("images").getString(j));
                        }
                        final MyService service = new MyService(name, description, user, images);
                        service.setId(jsonObject.getInt("ID"));
                        new Handler().postDelayed( new Runnable() {
                            @Override
                            public void run() {
                                fillServiceInfo(service);
                            }
                        }, 2000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( com.example.ibra.oxp.activities.services.EditService.this, "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }

        private void fillServiceInfo(MyService service) {
            name.setText(service.getName());
            description.setText(service.getDescription());
            ArrayList<String> images = service.getImages();
            if(images.size() > 0) {
                for(int i = 0 ; i < images.size() ; i++) {
                    for(int j = 1 ; j <= 7 ; j++) {
                        if (!images.get(i).contains("_" + j + ".png")) continue;
                        int id = getResources().getIdentifier("add_offer_picture" + j, "id", getPackageName());
                        try {
                            ImageView imageView = findViewById(id);
                            if (imageView != null) {
                                String imageUrl = Config.IMAGE_URL + images.get(i);
                                new ImageDownloaderTask(imageView, null).execute(imageUrl);
                            }
                        } catch (Exception e) {
                            Log.e("test", e.getMessage());
                        }
                    }
                }
            }
        }

        @OnClick(R.id.btnAddService)
        public void btn_add_service()
        {
            updateService();
        }

        private void updateService() {
            final String _name=name.getText().toString().trim();
            final String _description=description.getText().toString().trim();


            String email=getSharedPreferences("prefs",MODE_PRIVATE).getString("email",null);
            final MyService s = new MyService(_name, _description,email, null);

            StringBuilder sb = new StringBuilder(Config.OXP_URL);
            sb.append("simpleService/?");
            sb.append("name=%1$s");
            sb.append("&description=%2$s");
            sb.append("&email=%3$s");
            sb.append("&id=%4$s");
            String completeURL = String.format(sb.toString(), _name, _description, email, id);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, completeURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText( com.example.ibra.oxp.activities.services.EditService.this, "Service has been updated successfully.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent( com.example.ibra.oxp.activities.services.EditService.this, ViewServices.class);
                    startActivity(i);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( com.example.ibra.oxp.activities.services.EditService.this, "An error occurred while updating service.", Toast.LENGTH_SHORT).show();
                    Log.e("Test", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        }

        public void imageClicked(View view) {
            currTag = Integer.parseInt(view.getTag().toString());
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Service Image"), PICK_IMAGE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "Unable to read image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(data.getData() != null) {
                        uploadImages(data.getData());

                    } else if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            uploadImages(item.getUri());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }
        }

        private void uploadImages(final Uri image) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String completeURL = Config.OXP_URL+"upload-images/";
            Map<String, String> params = new HashMap<>();
            params.put("service", id+"");
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.e("Test", "image_"+currTag);
                params.put("image_"+currTag, encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject parameters =new JSONObject( params );
            Log.e("Test", "image_"+parameters.toString());
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, completeURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.e("Test", response);
                    Toast.makeText( com.example.ibra.oxp.activities.services.EditService.this, "All images has been uploaded successfully.", Toast.LENGTH_LONG).show();
                    updateService();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( com.example.ibra.oxp.activities.services.EditService.this, "An error occurred while uploading images.", Toast.LENGTH_SHORT).show();
                    Log.e("Test", error.toString());
                }
            }) {
                // @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("service", id+"");
//                try {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] imageBytes = baos.toByteArray();
//                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                    Log.e("Test", "image_"+currTag);
//                    params.put("image_"+currTag, encodedImage);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return params;
                // }
            };
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonRequest.setRetryPolicy(policy);
            requestQueue.add(jsonRequest);
        }



}
