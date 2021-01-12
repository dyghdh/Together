package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KtxChangePostActivity extends BaseActivity {
	private DatabaseReference mDatabase;
	private String year_value, month_value, day_value, time_value, city1_value, city2_value;
	private int city1_postion=0,city2_postion=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ktx_room_change);

		mDatabase = FirebaseDatabase.getInstance().getReference();
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
					Toast.makeText(KtxChangePostActivity.this, "출발지 와 도착지가 같습니다.", Toast.LENGTH_LONG).show();
				}
				else {
					Intent intent1 = getIntent();
					String title = year_value + "년" + month_value + "월" + day_value + "일 " + time_value;
					String body = city1_value + "->" + city2_value;
					String Ktx_post = intent1.getStringExtra("ktx-post-change");
					String Ktx_post1 = Ktx_post.replace("https://together-46f52.firebaseio.com/ktx-posts/", "");
					String Ktx_user_post = intent1.getStringExtra("ktx-user-post-change");
					String Ktx_user_post1 = Ktx_user_post.replace("https://together-46f52.firebaseio.com/ktx-user-posts/", "");
					String Ktx_user_post2 = Ktx_user_post1.replace("/" + Ktx_post, "");

					DatabaseReference post_db = mDatabase.child("ktx-posts").child(Ktx_post1);
					DatabaseReference user_post_db = mDatabase.child("ktx-user-posts").child(Ktx_user_post2).child(Ktx_post1);

					Map<String, Object> post_Updates = new HashMap<>();
					post_Updates.put("title", title);
					post_Updates.put("body", body);
					post_db.updateChildren(post_Updates);
					user_post_db.updateChildren(post_Updates);

					Intent intent = new Intent(KtxChangePostActivity.this, KtxMainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		city_change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(city1_postion != city2_postion){
					Toast.makeText(KtxChangePostActivity.this, "출발지 와 도착지가 같습니다.", Toast.LENGTH_LONG).show();
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




}