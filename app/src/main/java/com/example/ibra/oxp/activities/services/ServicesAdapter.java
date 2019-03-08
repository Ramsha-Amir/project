package com.example.ibra.oxp.activities.services;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.ImageDownloaderTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter {
    List<MyService> mServices;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int SERVICE_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;

    public ServicesAdapter(Context mContext) {
        mServices = new ArrayList<>();
        this.mContext = mContext;
    }
    //method to add products as soon as they fetched
    public void addServices(List<MyService> ser) {
        int lastPos = mServices.size();
        this.mServices.addAll(ser);
        notifyItemRangeInserted(lastPos, mServices.size());
    }

    public void addServices(List<MyService> ser, boolean clear) {
        if(clear) {
            this.mServices.clear();
            notifyDataSetChanged();
        }
        int lastPos = mServices.size();
        this.mServices.addAll(ser);
        notifyItemRangeInserted(lastPos, mServices.size());
    }


    @Override
    public int getItemViewType(int position) {
        MyService currentser = mServices.get(position);
        if (currentser.isLoading()) {
            return LOADING_ITEM;
        } else {
            return SERVICE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == SERVICE_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_product, parent, false);
            ((TextView)row.findViewById(R.id.textViewNew2)).setText("Service");
            return new ProductHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final MyService currentSer = mServices.get(position);
        if (holder instanceof ProductHolder) {
            ProductHolder productHolder = (ProductHolder) holder;
            //bind products information with view
            //Picasso.with(mContext).load(currentProduct.getId()).into(productHolder.imageViewProductThumb);
            productHolder.textViewProductName.setText(currentSer.getName());
            //productHolder.textViewProductPrice.setText(currentProduct.getProductPrice());
           // productHolder.textViewProductPrice.setText(currentSer.getPrice()+ " Rs");
            if (currentSer.isNew())
                productHolder.textViewNew.setVisibility(View.VISIBLE);
            else
                productHolder.textViewNew.setVisibility(View.GONE);

            if(currentSer.getImages() != null && currentSer.getImages().size() > 0) {
                String imageUrl = Config.IMAGE_URL+currentSer.getImages().get(0);
                new ImageDownloaderTask(productHolder.imageViewProductThumb, productHolder.img2).execute(imageUrl);
            }

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user selected product now you can show details of that product
//                Toast.makeText(mContext, "Selected "+currentProduct.getName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mContext, ServiceDetail.class);
                    i.putExtra("Service-ID", currentSer.getId());
                    i.putExtra("Service-name", currentSer.getName());
                    i.putExtra("Service-description", currentSer.getDescription());
                    i.putExtra("Service-user", currentSer.getUser());
                    i.putExtra("Service-user-email", currentSer.getUserEmail());
                    i.putStringArrayListExtra("Service-images", currentSer.getImages());
                    mContext.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }

    //Holds view of product with information
    private class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProductThumb, img2;
        TextView textViewProductName, textViewProductPrice, textViewNew;


        public ProductHolder(View itemView) {
            super(itemView);
            imageViewProductThumb = itemView.findViewById(R.id.imageViewProductThumb);
            img2 = itemView.findViewById(R.id.img2);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            //textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewNew = itemView.findViewById(R.id.textViewNew);

        }
    }
    //holds view of loading item
    private class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    //method to show loading
    public void showLoading() {
        MyService serv = new MyService();
        serv.setLoading(true);
        mServices.add(serv);
        LoadingItemPos = mServices.size();
        notifyItemInserted(mServices.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mServices.size()) {
            mServices.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }

    }
}
