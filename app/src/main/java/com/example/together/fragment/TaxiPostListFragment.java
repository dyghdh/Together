package com.example.together.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.example.together.TaxiPostDetailActivity;
import com.example.together.models.Post;
import com.example.together.viewholder.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class TaxiPostListFragment extends Fragment {
	private Activity mActivity;
	private DatabaseReference mDatabase;
	private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
	private RecyclerView mRecycler;

	public TaxiPostListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);
		mRecycler = rootView.findViewById(R.id.messages_list);
		mRecycler.setHasFixedSize(true);

		mDatabase = FirebaseDatabase.getInstance().getReference();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();

		final Dialog mDialog = new Dialog(mActivity, R.style.NewDialog);
		mDialog.addContentView(
				new ProgressBar(mActivity),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);
		mDialog.setCancelable(true);
		mDialog.show();

		// Set up Layout Manager, reverse layout
		LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
		mManager.setReverseLayout(true);
		mManager.setStackFromEnd(true);
		mRecycler.setLayoutManager(mManager);

		// Set up FirebaseRecyclerAdapter with the Query
		Query postsQuery = getQuery(mDatabase);

		FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
				.setQuery(postsQuery, Post.class)
				.build();

		mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
			@Override
			protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Post model) {
				final DatabaseReference postRef = getRef(position);


				// Bind Post to ViewHolder, setting OnClickListener for the star button
				viewHolder.bindToPost(model, new View.OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});

				viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mActivity, TaxiPostDetailActivity.class);
						intent.putExtra(TaxiPostDetailActivity.EXTRA_POST_KEY, postRef.getKey());
					startActivity(intent);
				}
				});

			}

			@Override
			public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				return new PostViewHolder(inflater.inflate(R.layout.item_chat_room, viewGroup, false));
			}

			@Override
			public void onDataChanged() {
				super.onDataChanged();
				mDialog.dismiss();
			}
		};
		mRecycler.setAdapter(mAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mAdapter != null) {
			mAdapter.startListening();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAdapter != null) {
			mAdapter.stopListening();
		}
	}


	public String getUid() {
		return FirebaseAuth.getInstance().getCurrentUser().getUid();
	}

	public abstract Query getQuery(DatabaseReference databaseReference);
}