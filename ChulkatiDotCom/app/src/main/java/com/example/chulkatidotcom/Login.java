package com.example.chulkatidotcom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);
        TextView forgetPass =(TextView) findViewById(R.id.forgotpass);
        TextView noAccount =(TextView) findViewById(R.id.donthaveaccount);
        ImageView google =(ImageView) findViewById(R.id.google);
        ImageView fb =(ImageView) findViewById(R.id.fb);
        ImageView twitter =(ImageView) findViewById(R.id.twitter);

        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.loginbtn);


        loginBtn.setOnClickListener(v -> {
            String email = username.getText().toString();
            String ps = password.getText().toString();

            if(email.equals("") || ps.equals(""))
            {
                Toast.makeText(Login.this,"Please Fill All The Fields",Toast.LENGTH_SHORT).show();
            }
            else
            {
                auth.signInWithEmailAndPassword(email,ps).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        dialog.show();
                        Intent myIntent = new Intent(v.getContext(), UserDataShow.class);
                        startActivity(myIntent);
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    toastMessage(e.getMessage());
                    dialog.dismiss();
                });
            }
        });

        //if user didn't registered
        noAccount.setOnClickListener(v -> {
            Intent myIntent = new Intent(v.getContext(), Registration.class);
            startActivity(myIntent);
        });

        //if user forget password
        forgetPass.setOnClickListener(v ->
        {

        });
        //if user click on google
        google.setOnClickListener(v ->
        {

        });
        //if user click on facebook
        fb.setOnClickListener(v ->
        {

        });
        //if user click on twitter
        twitter.setOnClickListener(v ->
        {

        });
    }

    //toastMessage function
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}