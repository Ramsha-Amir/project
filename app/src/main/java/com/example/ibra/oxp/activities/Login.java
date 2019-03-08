package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.SharedPref;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity {
    public static final String API_URL = Config.OXP_URL+"login/";
    RequestQueue rq;
    JSONArray data;

    String name;
    EditText password;
    @BindView(R.id.login_email) EditText login_email;
    @BindView(R.id.login_password) EditText login_password;
    MyDatabaseHelper mydbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //login_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        mydbhelper=new MyDatabaseHelper(this);

        rq= Volley.newRequestQueue(this);
        rq.start();


    }




    @OnClick(R.id.link_signup)
    public void link_signup_listener() {
        Intent i=new Intent(getApplicationContext(),SignUp.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_login)
    public void btn_login_listener () {
        final String _email=login_email.getText().toString().trim();
        final String _password=login_password.getText().toString().trim();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",_email);
            jsonObject.put("password",_password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*JsonObjectRequest jb=new JsonObjectRequest(Request.Method.POST, API_URL, jsonObject,
            new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                SharedPreferences.Editor prefs=getSharedPreferences("prefs",MODE_PRIVATE).edit();
                prefs.putString("email",_email).apply();
                Toast.makeText(Login.this, response.toString(), Toast.LENGTH_SHORT).show();
                Log.d("LOGIN SUCCESS! ",response.toString());
                Intent intent = new Intent(Login.this,HomePage.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Login.this, "Error logging in", Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN SUCCESS! ",error.toString());
            }
        });*/
//        Intent intent = new Intent(Login.this, HomePage.class);
//        startActivity(intent);
//        finish();
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

        if (!_password.isEmpty()&& !_email.isEmpty() ) {
            Matcher matcher = Pattern.compile(validemail).matcher(_email);
            if (matcher.matches()) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _email);
                params.put("password", _password);
                JSONObject parameters =new JSONObject( params );
                JsonObjectRequest json_rquest = new JsonObjectRequest(Request.Method.POST, API_URL, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e( "abc", "onResponse:" + response.toString());
                        try {
                            if (response.toString().contains("Successfully Logged In")) {
                                //JSONObject temp = new JSONObject(response);
                                JSONArray arr = new JSONArray(response.getString("data"));
                                JSONObject json = ((JSONObject) arr.get(0)).getJSONObject("fields");

                                Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                Log.d("LOGIN SUCCESS! ", response.toString());
                                SharedPreferences.Editor prefs=getSharedPreferences("prefs",MODE_PRIVATE).edit();
                                prefs.putString("name", json.getString("f_name")).apply();
                                prefs.putString("email", _email).apply();
                                prefs.commit();
                                Intent intent = new Intent(Login.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (response.toString().contains("User doesn't exist")) {
                                    Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                                    Log.d("User doesn't exist! ", response.toString());
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Login.this, "LOGIN REQUEST METHOD UNIDENTIFIED", Toast.LENGTH_SHORT).show();
                        Log.d("ERR LOGIN REQ METHOD! ", error.toString());
                    }
                }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("email", _email);
//                        params.put("password", _password);
//                        return params;
//                    }
                };
                // rq.add(jb);
                rq.add(json_rquest);

            }
            else {Toast.makeText(Login.this, "Invalid email", Toast.LENGTH_SHORT).show();}
        }
        else { Toast.makeText(Login.this, "Empty field detected", Toast.LENGTH_SHORT).show(); }

    }




}