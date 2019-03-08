package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.product.ViewProducts;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Category extends Base{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.signup);
        //setContentView(R.layout.com.example.ibra.app1.Activity.awein);
        setContentView(R.layout.categories);
        ButterKnife.bind(this);
        //setActionBar((Toolbar)findViewById(R.id.toolbar));

        setSupportActionBar(toolbar);
        bottom();


        //Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(tb);

    }

    public void categoryClicked(View view) {
        Intent i = new Intent(getApplicationContext(), ViewProducts.class);
        String category = (String)view.getTag();
        i.putExtra("category", category);
        startActivity(i);
    }

}
