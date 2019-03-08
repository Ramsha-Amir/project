package com.example.ibra.oxp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.product.ViewProducts;
import com.example.ibra.oxp.activities.services.ViewServices;
import com.example.ibra.oxp.database.MyDatabase;
import com.example.ibra.oxp.database.MyDatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomePage extends Base {


    GridLayout mainGrid;
    MyDatabase mydb;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        //setActionBar((Toolbar)findViewById(R.id.toolbar));


       // Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottom();

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);


        final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        final Context ctx = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper.syncCategories(ctx);
            }
        }).start();
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
       for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (finalI){

                        case 0:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(HomePage.this,ViewProducts.class);
//                            intent = new Intent(HomePage.this,AssistProduct.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(HomePage.this, ViewServices.class);
                            startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(HomePage.this,DiscussionForum.class);
                            startActivity(intent);
                            break;
                        case 5:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 6:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 7:
                            intent = new Intent(HomePage.this,Category.class);
                            startActivity(intent);
                            break;
                        case 8:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 9:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 10:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 11:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;
                        case 12:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            break;


                    }
                   // Intent intent = new Intent(HomePage.this,ViewProducts.class);
                    //intent.putExtra("info","This is activity from card item index  "+finalI);
                    //startActivity(intent);

                }
            });
 }




    }

}
