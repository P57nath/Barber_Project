package com.example.chulkatidotcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton EnterBtn = (MaterialButton) findViewById(R.id.Enterbtn);

        EnterBtn.setOnClickListener(v -> {
            Intent myIntent = new Intent(v.getContext(), Login.class);
            startActivity(myIntent);
        });
    }
}