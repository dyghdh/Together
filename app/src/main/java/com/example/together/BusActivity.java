package com.example.together;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BusActivity extends BaseActivity {
    Button btn_bus1,btn_bus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        btn_bus1 = (Button) findViewById(R.id.btn_bus1);
        btn_bus2 = (Button) findViewById(R.id.btn_bus2);

        btn_bus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent( Intent.ACTION_VIEW, Uri.parse("https://bis.gn.go.kr/#"));
                startActivity( intent1 );
            }
        });
        btn_bus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent( Intent.ACTION_VIEW, Uri.parse("https://m.bustago.or.kr:444/mobus/btmho/BTMHORN0001.do#"));
                startActivity( intent2 );
            }
        });
}
}