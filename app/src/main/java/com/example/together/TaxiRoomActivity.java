package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class TaxiRoomActivity extends AppCompatActivity  {


                private FirebaseAuth firebaseAuth;
                private int room_num = 0;
                private String year_value,month_value,day_value,time_value,city1_value,city2_value;
                private String str_room_num = "";
                private String room_name="";
                public ArrayList<String> room_name_Array = new ArrayList<String>();
                private TextView textView1;
                private TextView textView2;
                 String sfName = "myFile";

                @Override
                protected void onCreate(Bundle savedInstanceState) {

                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_taxi);

                        Intent i=getIntent();
                        String name= getIntent().getStringExtra("taxi_start");
                        textView1 = (TextView)findViewById(R.id.textView1);
                        textView1 .setText(name);

                        String nasme= getIntent().getStringExtra("taxi_arrive");
                        textView2=(TextView) findViewById(R.id.textView2);
                        textView2.setText(nasme);




                        Button create_btn = (Button) findViewById(R.id.btn_chat_room_create);
                    Spinner year_spinner = (Spinner) findViewById(R.id.year);
                    Spinner month_spinner = (Spinner) findViewById(R.id.month);
                    Spinner day_spinner = (Spinner) findViewById(R.id.day);
                    Spinner time_spinner = (Spinner) findViewById(R.id.time);
                    Button btn_start = (Button) findViewById(R.id.btn_start);



                    ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.년, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.월, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this, R.array.일, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter timeAdapter = ArrayAdapter.createFromResource(this, R.array.시간, android.R.layout.simple_spinner_dropdown_item);


                    year_spinner.setAdapter(yearAdapter);
                    month_spinner.setAdapter(monthAdapter);
                    day_spinner.setAdapter(dayAdapter);
                    time_spinner.setAdapter(timeAdapter);




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

                    create_btn.setOnClickListener(v -> {

                        if(city1_value == city2_value)
                            Toast.makeText(TaxiRoomActivity.this, "출발지 와 도착지 가 같습니다", Toast.LENGTH_SHORT).show();

                    else {
                                String key_ktx ="";
                                firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser chat_id = firebaseAuth.getCurrentUser();
                                String cid = chat_id.getUid();
                                key_ktx = cid + year_value + month_value + day_value + time_value + city2_value;
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid", cid);
                            hashMap.put("strat_city", city1_value);
                            hashMap.put("arrive_city", city2_value);
                            hashMap.put("year", year_value);
                            hashMap.put("month", month_value);
                            hashMap.put("day", day_value);
                            hashMap.put("time", time_value);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Ktx_chat_list");
                                reference.child(key_ktx).setValue(hashMap);

                                Intent intent = new Intent(TaxiRoomActivity.this, KtxMainActivity.class);
                                startActivity(intent);
                        }
                    });
                    View.OnClickListener clickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_start:
                                    Intent intent11 = new Intent(TaxiRoomActivity.this, TMapsActivity.class);
                                    startActivity(intent11);
                                    break;
                            }
                        }
                    };
                    btn_start.setOnClickListener(clickListener);
                }


    }
