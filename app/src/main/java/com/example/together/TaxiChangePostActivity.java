package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TaxiChangePostActivity extends BaseActivity {
	private DatabaseReference mDatabase;
	private String AMPM_value,hour_value,min_value, city1_value, city2_value,value,city1,city2;
	private TextView textView1;
	private TextView textView2;
	private ImageButton btn_change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taxi_change);

		Spinner AMPM_spinner = (Spinner) findViewById(R.id.taxi_am_pm);
		Spinner hour_spinner = (Spinner) findViewById(R.id.taxi_hour);
		Spinner min_spinner = (Spinner) findViewById(R.id.taxi_min);
		btn_change = (ImageButton) findViewById(R.id.btn_chang_city);

		ArrayAdapter AMPMAdapter = ArrayAdapter.createFromResource(this, R.array.taxi_AMPM, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter hourAdapter = ArrayAdapter.createFromResource(this, R.array.taxi_hour, android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter minAdapter = ArrayAdapter.createFromResource(this, R.array.taxi_min, android.R.layout.simple_spinner_dropdown_item);

		AMPM_spinner.setAdapter(AMPMAdapter);
		hour_spinner.setAdapter(hourAdapter);
		min_spinner.setAdapter(minAdapter);

		Intent intent1 = getIntent();

		String taxi_post = intent1.getStringExtra("taxi-post-change");
		String taxi_post1 =taxi_post.replace("https://together-46f52.firebaseio.com/taxi-posts/","");
		String taxi_user_post = intent1.getStringExtra("taxi-user-post-change");
		String taxi_user_post1 = taxi_user_post.replace("https://together-46f52.firebaseio.com/taxi-user-posts/","");
		String taxi_user_post2 = taxi_user_post1.replace("/"+taxi_post,"");



		String name= getIntent().getStringExtra("taxi_start");
		textView1 = (TextView)findViewById(R.id.textView1);
		textView1.setText(name);

		String nasme= getIntent().getStringExtra("taxi_arrive");
		textView2=(TextView) findViewById(R.id.textView2);
		textView2.setText(nasme);
		city1= name;
		city2 = nasme;



		Button create_btn = (Button) findViewById(R.id.btn_chat_room_change);
		Button map_btn = (Button) findViewById(R.id.btn_start);



		AMPM_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				AMPM_value = AMPM_spinner.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		hour_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				 hour_value = hour_spinner.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		min_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				min_value = min_spinner.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mDatabase = FirebaseDatabase.getInstance().getReference();

		create_btn.setOnClickListener(new View.OnClickListener() {
			@Override

			public void onClick(View v) {
				boolean nullInput = textView1.getText().toString().equals("") && textView2.getText().toString().equals("");

				if(!nullInput){

					city1_value = textView1.getText().toString().trim();
					city2_value = textView2.getText().toString().trim();

					final String title = city1_value +"->"+city2_value;
					final String body = AMPM_value +" "+ hour_value+ " 시 " + min_value+ " 분 ";


					//Toast.makeText(TaxiChangePostActivity.this, taxi_user_post2, Toast.LENGTH_LONG).show();

					DatabaseReference post_db = mDatabase.child("taxi-posts").child(taxi_post1);
					DatabaseReference user_post_db = mDatabase.child("taxi-user-posts").child(taxi_user_post2).child(taxi_post1);

					Map<String, Object> post_Updates = new HashMap<>();
					post_Updates.put("title", title);
					post_Updates.put("body", body);
					post_db.updateChildren(post_Updates);
					user_post_db.updateChildren(post_Updates);
					Intent intent = new Intent(TaxiChangePostActivity.this, TaxiMainActivity.class);
					startActivity(intent);
					finish();
				}
				else{
					Toast.makeText(TaxiChangePostActivity.this, "출발지 와 도착지를 설정하세요", Toast.LENGTH_SHORT).show();
				}
			}
		});
		map_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent11 = new Intent(TaxiChangePostActivity.this, TMapsActivity.class);
				startActivity(intent11);
			}
		});

		btn_change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				value = city1;
				city1 = city2;
				city2 = value;

				textView1.setText(city1);
				textView2.setText(city2);
			}
		});

	}
}