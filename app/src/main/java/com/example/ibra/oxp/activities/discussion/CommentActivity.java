package com.example.ibra.oxp.activities.discussion;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.utils.EndlessScrollListener;
import com.example.ibra.oxp.utils.Space;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends Base {

    private CommentsAdapter adapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();

        RecyclerView rv = findViewById(R.id.comments);
        adapter = new CommentsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.addItemDecoration(new Space(1, 20, true, 0));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case CommentsAdapter.COMMENT_ITEM:
                        return 1;
                    case CommentsAdapter.LOADING_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        mySwipeRefreshLayout = findViewById(R.id.refresh_comments);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadComments();
                    }
                }
        );

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!adapter.loading) {
                    loadComments();
                }
            }
        };

        rv.setAdapter(adapter);
        endlessScrollListener.onLoadMore(0, 0);
    }

    private void loadComments() {
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
                final ArrayList<String> dummyComments = new ArrayList<>();
                for(int i = 1 ; i <= 5 ; i++) {
                    dummyComments.add("This is dummy comment "+i);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.hideLoading();
                        adapter.setComments(dummyComments);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
