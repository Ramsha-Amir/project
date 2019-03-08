package com.example.ibra.oxp.activities.discussion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibra.oxp.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter {
    private List<String> mComments;
    private List<Boolean> mLoading;
    private Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int COMMENT_ITEM = 1;
    private int LoadingItemPos;
    public boolean loading = false;

    public CommentsAdapter(Context mContext) {
        mComments = new ArrayList<>();
        mLoading = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setComments(ArrayList<String> comments) {
        this.mComments.clear();
        notifyDataSetChanged();
        this.mComments = new ArrayList<>(comments);

        this.mLoading = new ArrayList<>();
        for(int i = 0 ; i < comments.size() ; i++)
            this.mLoading.add(false);
    }

    @Override
    public int getItemViewType(int position) {
        Boolean isLoading = mLoading.get(position);
        if (isLoading) {
            return LOADING_ITEM;
        } else {
            return COMMENT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == COMMENT_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_comment, parent, false);
            return new CommentHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String currentComment = mComments.get(position);
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.textViewComment.setText(currentComment);
        }

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    //Holds view of product with information
    private class CommentHolder extends RecyclerView.ViewHolder {
        TextView textViewComment;

        public CommentHolder(View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.commentView);
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
        mComments.add("");
        mLoading.add(true);
        LoadingItemPos = mComments.size();
        notifyItemInserted(mComments.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mComments.size()) {
            mComments.remove(LoadingItemPos - 1);
            mLoading.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }

    }
}