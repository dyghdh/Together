package com.example.together;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_PERMISSIONS=1000;
    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationClient;
    private Geocoder geocoder;
    Button btn1;
    Button saved1;
    EditText et1;
    EditText et2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmaps);
        et2= (EditText) findViewById(R.id.editText2);
        et1 = (EditText) findViewById(R.id.editText1);
        btn1 = (Button) findViewById(R.id.button);
        saved1 = findViewById(R.id.saved1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        saved1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TMapsActivity.this, TaxiNewPostActivity.class);
                i.putExtra("taxi_start", et1.getText().toString());
                i.putExtra("taxi_arrive", et2.getText().toString());
                startActivity(i);
                finish();
            }
        });
        Places.initialize(getApplicationContext(),"AIzaSyCSECc4Q1yqVZtwxjBC4Y-FFF6YLe3xYOc");

        et1.setFocusable(false);
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS ,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(TMapsActivity.this);
                startActivityForResult(intent,100);
            }
        });
        et2.setFocusable(false);
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS ,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(TMapsActivity.this);
                startActivityForResult(intent,1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            et1.setText(place.getAddress());
            et1.setText(String.format("%s", place.getName()));
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
        if(requestCode==1000&&resultCode==RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            et2.setText(place.getAddress());
            et2.setText(String.format("%s", place.getName()));
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
        }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        btn1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=et1.getText().toString();
                String str1=et2.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str1, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(address);
                mOptions2.position(point);
                //마커 이미지 변경
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker2);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);
                mOptions2.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                // 마커 추가
                mMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng Seoul = new LatLng(37.335887, 126.584063);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Seoul));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }

    public void onLastLocationButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSIONS);
            return;
        }
        mfusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    LatLng myLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("현재위치"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show();
                }
        }

    }
}

