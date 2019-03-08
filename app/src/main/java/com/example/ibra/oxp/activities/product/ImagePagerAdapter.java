package com.example.ibra.oxp.activities.product;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.utils.Config;
import com.example.ibra.oxp.utils.ImageDownloaderTask;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> images = new ArrayList<>();

    public ImagePagerAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setImages(ArrayList<String> images) {
        this.images = new ArrayList<>(images);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        String imageUrl = Config.IMAGE_URL + images.get(position);
        new ImageDownloaderTask(imageView, null).execute(imageUrl);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
