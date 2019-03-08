package com.example.ibra.oxp.activities.discussion;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibra.oxp.R;

import java.util.ArrayList;
import java.util.List;

public class DiscussionPostAdapter extends RecyclerView.Adapter {
    private List<DiscussionPost> mPosts;
    private Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int POST_ITEM = 1;
    private int LoadingItemPos;
    public boolean loading = false;

    public DiscussionPostAdapter(Context mContext) {
        mPosts = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setPosts(ArrayList<DiscussionPost> posts) {
        this.mPosts.clear();
        notifyDataSetChanged();
        this.mPosts = new ArrayList<>(posts);
    }

    @Override
    public int getItemViewType(int position) {
        DiscussionPost currentPost = mPosts.get(position);
        if (currentPost.isLoading()) {
            return LOADING_ITEM;
        } else {
            return POST_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == POST_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_post, parent, false);
            return new PostHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //get current post
        final DiscussionPost currentPost = mPosts.get(position);
        if (holder instanceof PostHolder) {
            PostHolder postHolder = (PostHolder) holder;
            postHolder.textViewAuthor.setText(currentPost.getAuthor()+":");
            postHolder.textViewPost.setText(currentPost.getPost());

            postHolder.menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(mContext, view);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.edit_post:
                                    Intent i = new Intent(mContext, AddPost.class);
                                    mContext.startActivity(i);
                                    break;
                                case R.id.delete_post:
                                    Toast.makeText(mContext, "Delete clicked", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    break;
                            }

                            return true;
                        }
                    });
                }
            });

            postHolder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Post liked", Toast.LENGTH_LONG).show();
                }
            });

            postHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CommentActivity.class);
                    mContext.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //Holds view of product with information
    private class PostHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor, textViewPost;
        ImageView menuView;
        Button like, comment;

        public PostHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewPost = itemView.findViewById(R.id.textViewPost);
            menuView = itemView.findViewById(R.id.menuView);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
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
        DiscussionPost post = new DiscussionPost();
        post.setLoading(true);
        mPosts.add(post);
        LoadingItemPos = mPosts.size();
        notifyItemInserted(mPosts.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mPosts.size()) {
            mPosts.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }

    }
}
