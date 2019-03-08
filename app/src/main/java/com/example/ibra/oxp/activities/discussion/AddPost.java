package com.example.ibra.oxp.activities.discussion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.DiscussionForum;

public class AddPost extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
    }

    public void savePost(View v) {
        Toast.makeText(this, "Your post has been published successfully.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, DiscussionForum.class);
        startActivity(i);
    }
}
