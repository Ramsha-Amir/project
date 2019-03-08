package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.discussion.AddPost;
import com.example.ibra.oxp.activities.discussion.DiscussionPost;
import com.example.ibra.oxp.activities.discussion.DiscussionPostAdapter;
import com.example.ibra.oxp.utils.EndlessScrollListener;
import com.example.ibra.oxp.utils.Space;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionForum extends Base
{

    private DiscussionPostAdapter adapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_forum);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();

        RecyclerView rv = findViewById(R.id.discussion_posts);
        adapter = new DiscussionPostAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.addItemDecoration(new Space(1, 20, true, 0));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case DiscussionPostAdapter.POST_ITEM:
                        return 1;
                    case DiscussionPostAdapter.LOADING_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        mySwipeRefreshLayout = findViewById(R.id.refresh_post);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPosts();
                    }
                }
        );

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!adapter.loading) {
                    loadPosts();
                }
            }
        };

        rv.setAdapter(adapter);
        endlessScrollListener.onLoadMore(0, 0);
    }

    private void loadPosts()
    {
        mySwipeRefreshLayout.setRefreshing(true);
        adapter.showLoading();
        // Load dummy posts with wait
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final ArrayList<DiscussionPost> dummyPosts = new ArrayList<>();
                for(int i = 1 ; i <= 5 ; i++) {
                    dummyPosts.add(new DiscussionPost("Ramsha", "This is a dummy post "+i+" and it will be replaced by the author."));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.hideLoading();
                        adapter.setPosts(dummyPosts);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void addPost(View v) {
        Intent i = new Intent(this, AddPost.class);
        startActivity(i);
    }

}
