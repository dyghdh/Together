package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Set_user extends AppCompatActivity {
   FirebaseAuth firebaseAuth;
   FirebaseUser user;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;

TextView nametv,emailtv,phonetv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_user);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=firebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");

        nametv=findViewById(R.id.nametv);
        emailtv=findViewById(R.id.emailtv);
        phonetv=findViewById(R.id.phonetv);
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    String name=":"+ds.child("username").getValue();
                    String email=":"+ds.child("email").getValue();
                    String phone=":"+ds.child("phone").getValue();

                    nametv.setText(name);
                    emailtv.setText(email);
                    phonetv.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.t1);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });

    }
    }


