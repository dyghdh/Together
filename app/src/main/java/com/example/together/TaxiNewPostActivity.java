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

import com.example.together.models.Post;
import com.example.together.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TaxiNewPostActivity extends BaseActivity {
	private DatabaseReference mDatabase;
	private String AMPM_value,hour_value,min_value, city1_value, city2_value,value,city1,city2;
	private TextView textView1;
	private TextView textView2;
	private ImageButton btn_change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taxi);

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


		Intent i=getIntent();
		String name= getIntent().getStringExtra("taxi_start");
		textView1 = (TextView)findViewById(R.id.textView1);
		textView1 .setText(name);

		String nasme= getIntent().getStringExtra("taxi_arrive");
		textView2=(TextView) findViewById(R.id.textView2);
		textView2.setText(nasme);
		city1= name;
		city2 = nasme;


		Button create_btn = (Button) findViewById(R.id.btn_chat_room_create);
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
					submitPost();
					Intent intent1 = new Intent(TaxiNewPostActivity.this, TaxiMainActivity.class);
					startActivity(intent1);
				}
				else{
					Toast.makeText(TaxiNewPostActivity.this, "출발지 와 도착지를 설정하세요", Toast.LENGTH_SHORT).show();
				}
			}
		});
		map_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent11 = new Intent(TaxiNewPostActivity.this, TMapsActivity.class);
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

	private void submitPost() {
		city1_value = textView1.getText().toString().trim();
		city2_value = textView2.getText().toString().trim();

		final String title = city1_value +"->"+city2_value;
		final String body = AMPM_value +" "+ hour_value+ " 시 " + min_value+ " 분 ";
		final String userId = getUid();


			// Disable button so there are no multi-posts
			mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					User user = dataSnapshot.getValue(User.class);
					if (user == null) {
						Toast.makeText(TaxiNewPostActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
					} else {
						writeNewPost(userId, user.username, title, body);
					}

					finish();
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

					Toast.makeText(TaxiNewPostActivity.this, "onCancelled: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		}



	private void writeNewPost(String userId, String username, String title, String body) {
		// Create new post at /user-posts/$userid/$postid
		// and at /posts/$postid simultaneously
		String key = mDatabase.child("taxi-posts").push().getKey();
		Post post = new Post(userId, username, title, body);
		Map<String, Object> postValues = post.toMap();

		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put("/taxi-posts/" + key, postValues);
		childUpdates.put("/taxi-user-posts/" + userId + "/" + key, postValues);

		mDatabase.updateChildren(childUpdates);
	}
}