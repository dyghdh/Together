package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FindID extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthLisner;
    private EditText et_name,et_phnb;
    private Button btn_find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);

        et_phnb=findViewById(R.id.et_phnb);
        btn_find=findViewById(R.id.btn_find);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phnb= et_phnb.getText().toString();
                Intent intent=new Intent(FindID.this,FindIDcheck.class);
                intent.putExtra("phnb",phnb);
                startActivity(intent);
            }
        });

    }
}
