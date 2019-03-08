package com.example.ibra.oxp.activities.discussion;

public class DiscussionPost {

    private String mPost, mAuthor;

    DiscussionPost()
    {
    }

    public DiscussionPost(String author, String post)
    {
        mAuthor = author;
        mPost = post;
    }

    private boolean isLoading = false;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPost() {
        return mPost;
    }
}
