package com.example.chulkatidotcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserDataShow extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_show);

        auth = FirebaseAuth.getInstance();

        TextView showUser = findViewById(R.id.showUsers);
        MaterialButton logOut = (MaterialButton) findViewById(R.id.logOut);

        logOut.setOnClickListener(v -> {
            auth.signOut();
            Intent myIntent = new Intent(v.getContext(), Login.class);
            startActivity(myIntent);
            finish();
        });

    }
}