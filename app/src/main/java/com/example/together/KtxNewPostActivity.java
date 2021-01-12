package com.example.together;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.together.models.Post;
import com.example.together.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class KtxNewPostActivity extends BaseActivity {
	private DatabaseReference mDatabase;
	private String year_value, month_value, day_value, time_value, city1_value, city2_value;
	private int city1_postion=0,city2_postion=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ktx_room);

		Button create_btn = (Button) findViewById(R.id.btn_chat_room_create);
		ImageButton city_change = (ImageButton) findViewById(R.id.btn_chang_city);
		Spinner year_spinner = (Spinner) findViewById(R.id.year);
		Spinner month_spinner = (Spinner) findViewById(R.id.month);
		Spinner day_spinner = (Spinner) findViewById(R.id.day);
		Spinner time_spinner = (Spinner) findViewById(R.id.time);
		Spinner city1_spinner = (Spinner) findViewById(R.id.city1_spinner);
		Spinner city2_spinner = (Spinner) findViewById(R.id.city2_spinner);

		ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.년, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.월, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this, R.array.일, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter timeAdapter = ArrayAdapter.createFromResource(this, R.array.시간, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter cityAdapter1 = ArrayAdapter.createFromResource(this, R.array.도시선택1, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter cityAdapter2 = ArrayAdapter.createFromResource(this, R.array.도시선택2, android.R.layout.simple_spinner_dropdown_item);


		year_spinner.setAdapter(yearAdapter);
		month_spinner.setAdapter(monthAdapter);
		day_spinner.setAdapter(dayAdapter);
		time_spinner.setAdapter(timeAdapter);
		city1_spinner.setAdapter(cityAdapter1);
		city2_spinner.setAdapter(cityAdapter2);

		year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				year_value = year_spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				month_value = month_spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				day_value = day_spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				time_value = time_spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		city1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				city1_value = city1_spinner.getSelectedItem().toString();
				 if(position==0){
				 	city1_postion = 0;
				}
				 if(position==1){
					 city1_postion = 1;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		city2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				city2_value = city2_spinner.getSelectedItem().toString();
				if(position==0){
					city2_postion = 0;
				}
				if(position==1){
					city2_postion = 1;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


		mDatabase = FirebaseDatabase.getInstance().getReference();

		create_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(city1_value == city2_value){
					Toast.makeText(KtxNewPostActivity.this, "출발지 와 도착지가 같습니다.", Toast.LENGTH_LONG).show();
				}
				else
					submitPost();
			}
		});

		city_change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(city1_postion != city2_postion){
					Toast.makeText(KtxNewPostActivity.this, "출발지 와 도착지가 같습니다.", Toast.LENGTH_LONG).show();
				}
				else if(city1_postion == 0 && city2_postion == 0){
					city1_spinner.setSelection(1);
					city2_spinner.setSelection(1);
				}
				else if (city1_postion == 1 && city2_postion == 1) {
					city1_spinner.setSelection(0);
					city2_spinner.setSelection(0);
				}

			}
		});
	}



	private void submitPost() {
		final String title = year_value + "년" + month_value + "월" +day_value +"일 " +time_value;
		final String body = city1_value + "->" + city2_value;
		final String userId = getUid();


			// Disable button so there are no multi-posts
			mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					User user = dataSnapshot.getValue(User.class);
					if (user == null) {
						Toast.makeText(KtxNewPostActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
					} else {
						writeNewPost(userId, user.username, title, body);
					}

					finish();
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

					Toast.makeText(KtxNewPostActivity.this, "onCancelled: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		}



	private void writeNewPost(String userId, String username, String title, String body) {
		// Create new post at /user-posts/$userid/$postid
		// and at /posts/$postid simultaneously
		String key = mDatabase.child("ktx-posts").push().getKey();
		Post post = new Post(userId, username, title, body);
		Map<String, Object> postValues = post.toMap();

		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put("/ktx-posts/" + key, postValues);
		childUpdates.put("/ktx-user-posts/" + userId + "/" + key, postValues);

		mDatabase.updateChildren(childUpdates);
	}
}