package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        LinearLayout btn_taxi = (LinearLayout) findViewById(R.id.btn_taxi);
        LinearLayout btn_ktx = (LinearLayout) findViewById(R.id.btn_ktx);
        LinearLayout btn_bus = (LinearLayout) findViewById(R.id.btn_bus);
        LinearLayout btn_map = (LinearLayout) findViewById(R.id.btn_map);
        LinearLayout btn_weather = (LinearLayout) findViewById(R.id.btn_weather);
        LinearLayout btn_setting = (LinearLayout) findViewById(R.id.btn_setting);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_taxi:
                        Intent intent1 = new Intent( MenuActivity.this, TaxiMainActivity.class );
                        startActivity( intent1 );
                        break;
                    case R.id.btn_ktx :
                        Intent intent2 = new Intent( MenuActivity.this, KtxMainActivity.class );
                        startActivity( intent2 );
                        break;
                    case R.id.btn_bus :
                        Intent intent3 = new Intent( MenuActivity.this, BusActivity.class );
                        startActivity( intent3 );
                        break;
                    case R.id.btn_map :
                        Intent intent4 = new Intent( MenuActivity.this, MapsActivity.class );
                        startActivity( intent4 );
                        break;
                    case R.id.btn_weather :
                        Intent intent5 = new Intent( MenuActivity.this, WeatherActivity.class );
                        startActivity( intent5 );
                        break;
                    case R.id.btn_setting :
                        Intent intent6 = new Intent( MenuActivity.this, Set_user.class );
                        startActivity( intent6 );
                        break;

                }
            }
        };

        btn_taxi.setOnClickListener(clickListener);
        btn_ktx.setOnClickListener(clickListener);
        btn_bus.setOnClickListener(clickListener);
        btn_map.setOnClickListener(clickListener);
        btn_weather.setOnClickListener(clickListener);
        btn_setting.setOnClickListener(clickListener);

    }

}