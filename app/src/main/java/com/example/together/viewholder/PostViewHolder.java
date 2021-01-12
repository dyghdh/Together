package com.example.together.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.example.together.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {


	private TextView titleView;
	private TextView bodyView;

	public PostViewHolder(View itemView) {
		super(itemView);

		titleView = itemView.findViewById(R.id.chat_room_tv1);
		bodyView = itemView.findViewById(R.id.chat_room_tv2);
	}

	public void bindToPost(Post post, View.OnClickListener starClickListener) {
		titleView.setText(post.title);
		bodyView.setText(post.body);
	}
}