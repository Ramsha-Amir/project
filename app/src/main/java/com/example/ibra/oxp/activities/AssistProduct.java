package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.activities.product.ViewProducts;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssistProduct extends Base{

        @BindView(R.id.toolbar)Toolbar toolbar;
        //Toolbar toolbar;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.signup);
            //setContentView(R.layout.com.example.ibra.app1.Activity.awein);
            setContentView(R.layout.assist_product);
            ButterKnife.bind(this);
            setSupportActionBar(toolbar);
            bottom();
            GridLayout gd=(GridLayout)findViewById(R.id.assistProduct_mainGrid);
            setSingleEvent(gd);
        }


        private void setSingleEvent(GridLayout assistProduct_mainGrid) {
            //Loop all child item of Main Grid
           // Toast toast = Toast.makeText(getApplicationContext(),"helll"+assistProduct_mainGrid.getChildCount(), Toast.LENGTH_SHORT); toast.show();
            for (int i = 0; i <assistProduct_mainGrid.getChildCount(); i++) {
                //You can see , all child item is CardView , so we just cast object to CardView
                CardView cardView = (CardView)assistProduct_mainGrid.getChildAt(i);
                final int finalI = i;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        switch (finalI) {

                            case 0:
                                intent = new Intent(AssistProduct.this, AddProduct.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(AssistProduct.this, HomePage.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AssistProduct.this, HomePage.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AssistProduct.this, ViewProducts.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }




        }

}
