package com.example.ibra.oxp.activities;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.product.ViewProducts;


public class Base extends AppCompatActivity
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.clear();
        // Inflate the menu items for use in the action bar

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.app_bar_menu, menu);

        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        MenuItem search_view=menu.findItem(R.id.search);
        TextView v = findViewById(R.id.title_user_name);
        v.setText(getSharedPreferences("prefs",MODE_PRIVATE).getString("name", "User"));
        ShareActionProvider shareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(search_view);
        // MenuItem item=menu.findItem(R.id.)


        return super.onCreateOptionsMenu(menu);
    }

     public void bottom()
     {
         BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
         navigation.setItemBackgroundResource(R.drawable.menu_background);
         navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
     }

     private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {

                case R.id.navigation_account:
                    i=new Intent(getApplicationContext(), ViewProducts.class);
                    String user = getSharedPreferences("prefs",MODE_PRIVATE).getString("email", null);
                    i.putExtra("user", user);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_df:
                    i=new Intent(getApplicationContext(),DiscussionForum.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_fav:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_home:
                    i=new Intent(getApplicationContext(),HomePage.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_product:
                return true;
            case R.id.delete_product:
                return true;
//            case R.id.edit_service:
//               return true;
//            case R.id.delete_service:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit_service:
//                return true;
//            case R.id.delete_service:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
