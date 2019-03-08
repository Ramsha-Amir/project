package com.example.ibra.oxp.activities.product;


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
import com.example.ibra.oxp.models.MyProduct;
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

public class ProductsAdapter extends RecyclerView.Adapter {
    List<MyProduct> mProducts;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;

    public ProductsAdapter(Context mContext) {
        mProducts = new ArrayList<>();
        this.mContext = mContext;
    }
    //method to add products as soon as they fetched
    public void addProducts(List<MyProduct> products) {
        int lastPos = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(lastPos, mProducts.size());
    }

    public void addProducts(List<MyProduct> products, boolean clear) {
        if(clear) {
            this.mProducts.clear();
            notifyDataSetChanged();
        }
        int lastPos = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(lastPos, mProducts.size());
    }


    @Override
    public int getItemViewType(int position) {
        MyProduct currentProduct = mProducts.get(position);
        if (currentProduct.isLoading()) {
            return LOADING_ITEM;
        } else {
            return PRODUCT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == PRODUCT_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_product, parent, false);
            return new ProductHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final MyProduct currentProduct = mProducts.get(position);
        if (holder instanceof ProductHolder) {
            ProductHolder productHolder = (ProductHolder) holder;
            //bind products information with view
            //Picasso.with(mContext).load(currentProduct.getId()).into(productHolder.imageViewProductThumb);
            productHolder.textViewProductName.setText(currentProduct.getName());
            //productHolder.textViewProductPrice.setText(currentProduct.getProductPrice());
            productHolder.textViewProductPrice.setText(currentProduct.getPrice()+ " Rs");
            if (currentProduct.isNew())
                productHolder.textViewNew.setVisibility(View.VISIBLE);
            else
                productHolder.textViewNew.setVisibility(View.GONE);

            if(currentProduct.getImages() != null && currentProduct.getImages().size() > 0) {
                String imageUrl = Config.IMAGE_URL+currentProduct.getImages().get(0);
                new ImageDownloaderTask(productHolder.imageViewProductThumb, productHolder.img2).execute(imageUrl);
            }

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // user selected product now you can show details of that product
//                Toast.makeText(mContext, "Selected "+currentProduct.getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, ProductDetail.class);
                i.putExtra("Product-ID", currentProduct.getId());
                i.putExtra("Product-name", currentProduct.getName());
                i.putExtra("Product-price", currentProduct.getPrice());
                i.putExtra("Product-quantity", currentProduct.getQuantity());
                i.putExtra("Product-description", currentProduct.getDescription());
                i.putExtra("Product-user", currentProduct.getUser());
                i.putExtra("Product-user-email", currentProduct.getUserEmail());
                i.putStringArrayListExtra("Product-images", currentProduct.getImages());
                mContext.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
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
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
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
        MyProduct product = new MyProduct();
        product.setLoading(true);
        mProducts.add(product);
        LoadingItemPos = mProducts.size();
        notifyItemInserted(mProducts.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mProducts.size()) {
            mProducts.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }

    }
}
