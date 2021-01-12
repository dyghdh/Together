package com.example.together.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TaxiPostsFragment extends TaxiPostListFragment {
    public TaxiPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // My top posts by number of stars
        return databaseReference.child("taxi-posts").limitToFirst(5);
    }
}