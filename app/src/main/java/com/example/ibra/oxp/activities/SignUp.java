package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.User;
import com.example.ibra.oxp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {
    public static final String SIGNUP_URl = Config.OXP_URL+"signup/";
    @BindView(R.id.signup_fname)
    EditText fname;
    @BindView(R.id.signup_lname)
    EditText lname;
    @BindView(R.id.signup_spinner)
    Spinner city_spinner;
    @BindView(R.id.signup_loginlink)
    TextView link_login;

    //@BindView(R.id.signup_) EditText city;
    @BindView(R.id.signup_contact)
    EditText contact;
    @BindView(R.id.signup_address)
    EditText address;
    @BindView(R.id.signup_email)
    EditText email;
    @BindView(R.id.signup_password)
    EditText password;
    @BindView(R.id.signup_confirmpassword)
    EditText confirm_password;
    MyDatabaseHelper mydbhelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);
        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        mydbhelper = new MyDatabaseHelper(this);

    }

    @OnClick(R.id.signup_loginlink)
    public void link_login_listener() {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


    @OnClick(R.id.btn_signup)
    public void btn_signup_listener() {
        String _fname = fname.getText().toString().trim();
        String _lname = lname.getText().toString().trim();
        String _address = address.getText().toString().trim();
        String _contact = contact.getText().toString().trim();
        String _email = email.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String _cpassword = confirm_password.getText().toString().trim();
        String _city = city_spinner.getSelectedItem().toString().trim();
        User u = new User(_fname, _lname, _city, _address, _contact, _email, _password);

        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

        if (!_fname.isEmpty() && !_lname.isEmpty()  && !_address.isEmpty() && !_contact.isEmpty() && !_email.isEmpty() && !_password.isEmpty() && !_cpassword.isEmpty()) {
            if (_password.equals(_cpassword)) {
                //Toast.makeText(SignUp.this, u.get_fname() + "  " + u.get_lname() + "" + u.get_city() + " " + u.get_address() + " " + u.get_phone() + " " + u.get_password() + " " + u.get_email(), Toast.LENGTH_SHORT).show();
                Matcher matcher = Pattern.compile(validemail).matcher(_email);
                if (matcher.matches()) {
                    processSignUp();
                    Toast.makeText(SignUp.this, "SignedUp Successfully", Toast.LENGTH_SHORT).show();
                    mydbhelper.InsertData(u);
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUp.this, "Password Not matching", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignUp.this, "Empty field detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void processSignUp() {
        String _fname = fname.getText().toString().trim();
        String _lname = lname.getText().toString().trim();
        String _address = address.getText().toString().trim();
        String _contact = contact.getText().toString().trim();
        String _email = email.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String _city = city_spinner.getSelectedItem().toString().trim();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("f_name", _fname);
            jsonObject.put("l_name", _lname);
            jsonObject.put("country", "Pakistan");
            jsonObject.put("postal_code", String.valueOf(54000));
            jsonObject.put("address", _address);
            jsonObject.put("phoneNo", _contact);
            jsonObject.put("email", _email);
            jsonObject.put("password", _password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SIGNUP_URl, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SignUp Success!",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SignUp ERROR!",error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
