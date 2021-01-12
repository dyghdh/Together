package com.example.together.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class KtxMyPostsFragment extends KtxMyPostListFragment {
    public KtxMyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("ktx-user-posts").child(getUid());
    }
}