package com.example.together.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TaxiMyPostsFragment extends TaxiMyPostListFragment {
    public TaxiMyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("taxi-user-posts").child(getUid());
    }
}