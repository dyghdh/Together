package com.example.together;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    TextView view_city;
    TextView view_temp;
    TextView view_desc;

    ImageView view_weather;
    EditText search;
    FloatingActionButton search_floating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        view_city=findViewById(R.id.town);
        view_city.setText("");
        view_temp=findViewById(R.id.temp);
        view_temp.setText("");
        view_desc=findViewById(R.id.desc);
        view_desc.setText("");

        view_weather=findViewById(R.id.weather_image);
        search=findViewById(R.id.search_edit);
        search_floating=findViewById(R.id.floating_search);

        search_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide Keyboard
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
                api_key(String.valueOf(search.getText()));
            }
        });

    }

    private void api_key(String City) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=f5ebed128a78da305b9cdc06e137d650&units=metric")
                .get()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray array = json.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description = object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject temp1 = json.getJSONObject("main");
                        Double Temperature = temp1.getDouble("temp");

                        setText(view_city,City);

                        String temps = Math.round(Temperature)+" °C";
                        setText(view_temp,temps);
                        setText(view_desc,description);
                        setImage(view_weather,icons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setImage(final ImageView imageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //paste switch
                switch (value){
                    case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "01n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));    
                        break;
                    case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "05d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d05d));
                        break;
                    case "05n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d05d));
                        break;
                    case "06d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d06d));
                        break;
                    case "06n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d06d));
                        break;
                    case "07d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d07d));
                        break;
                    case "07n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d07d));
                        break;
                    default:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.weather));
                }
            }
        });
    }
}