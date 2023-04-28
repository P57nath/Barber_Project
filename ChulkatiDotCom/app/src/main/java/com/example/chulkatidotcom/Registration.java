package com.example.chulkatidotcom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    //create object for realtime_database
    DatabaseReference DatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chulkatidotcom-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;

    private TextView date, UserName, PhoneNumber, email, password, date_of_birth;
    private CheckBox policy;
    private RadioGroup gender_group;
    private RadioButton gender_button;

    private Calendar c;
    private DatePickerDialog dpd;


    @SuppressLint({"CutPasteId", "SetTextI18n", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //identify all object_id

        String deviceId = Settings.Secure.getString(Registration.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        auth = FirebaseAuth.getInstance();

        ProgressDialog dialog = new ProgressDialog(Registration.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");
        gender_group = findViewById(R.id.genderGroup);

        date = findViewById(R.id.date_of_birth);
        Button select_date = findViewById(R.id.select_dob);

        UserName = findViewById(R.id.first_name);
        PhoneNumber = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        date_of_birth = findViewById(R.id.date_of_birth);
        password = findViewById(R.id.password);
        policy = findViewById(R.id.policy);

        //To Select date

        select_date.setOnClickListener(v -> {

            c = Calendar.getInstance();
            final int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            dpd = new DatePickerDialog(Registration.this, (view, year1, month1, dayOfMonth) -> {

                if(dayOfMonth < 10 || month1 < 10)
                {
                    if(month1 < 10)
                    {
                        date.setText("0"+(month1 + 1) + "/" + dayOfMonth + "/" + year1);
                    }
                    else
                    {
                        date.setText((month1 + 1) + "/0" + dayOfMonth + "/" + year1);
                    }
                }
                else
                {
                    date.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1);
                }

            }, day, month, year);
            dpd.show();
        });
        //Tap on register

        Button b = findViewById(R.id.signUpButtonId);

        b.setOnClickListener(v -> {

            if (PhoneNumber.getText().toString().length()>11)
            {
                PhoneNumber.setError("Phone Number Is Invalid");
            }
            else
            {
                //gender checked
                int selected_gender = gender_group.getCheckedRadioButtonId();
                gender_button = findViewById(selected_gender);

                //pattern_writing_style
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String datePattern = "^(1[0-2]|0[1-9])/(3[01]" + "|[12][0-9]|0[1-9])/[0-9]{4}$";

                //edit text error checked

                if (UserName.getText().toString().equals(""))
                {
                    UserName.setError("Empty First Name");
                }
                else if (PhoneNumber.getText().toString().equals(""))
                {
                    PhoneNumber.setError("Empty Phone Number");
                }
                else if (PhoneNumber.getText().toString().length()>11)
                {
                    PhoneNumber.setError("Phone Number Is Invalid");
                }
                else if (email.getText().toString().equals(""))
                {
                    email.setError("Empty Email Address");
                }
                else if (!email.getText().toString().trim().matches(emailPattern))
                {
                    email.setError("Invalid Email Address");
                }
                else if (date_of_birth.getText().toString().equals(""))
                {
                    date_of_birth.setError("Select Date Of Birth");
                }
                else if (!date_of_birth.getText().toString().trim().matches(datePattern))
                {
                    date_of_birth.setError("Date Format Should Be 'mm/dd/yyyy'");
                }
                else if (password.getText().toString().equals(""))
                {
                    password.setError("Empty Password");
                }
                else if(!policy.isChecked())
                {
                    policy.setError("Policy Must Be Accepted");
                }
                else
                {
                    if (UserName.getError() == null && PhoneNumber.getError() == null && email.getError() == null &&
                            date_of_birth.getError() == null && password.getError() == null && policy.isChecked()) {

                        //Get data from view
                        String userName = UserName.getText().toString();
                        String phoneNumber =  PhoneNumber.getText().toString();
                        String Email = email.getText().toString();
                        String Gender = gender_button.getText().toString();
                        String DOB = date_of_birth.getText().toString();
                        String Password = password.getText().toString();

                        Query query = DatabaseRef.orderByChild("User's").equalTo(deviceId);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //check if phone number is registered or not
                                if(snapshot.exists())
                                {
                                    toastMessage("Phone Number Is Already Registered");
                                }
                                else
                                {
                                    //sending data to firebase_database
                                    auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                                        if(task.isSuccessful())
                                        {
                                            dialog.show();
                                            String usId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                                            HashMap<String,Object> map = new HashMap<>();

                                            map.put("UserName",userName);
                                            map.put("PhoneNumber",phoneNumber);
                                            map.put("Email",Email);
                                            map.put("Password",Password);
                                            map.put("Gender",Gender);
                                            map.put("Date_Of_Birth",DOB);
                                            map.put("DeviceId",usId);

                                            DatabaseRef.child("User's").child(usId).setValue(map).addOnCompleteListener(task1 ->
                                                    toastMessage("Registration Successful"));
                                            //Going back to Login
                                            Intent myIntent = new Intent(v.getContext(), Login.class);
                                            startActivity(myIntent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            toastMessage(e.getMessage());
                                            dialog.dismiss();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    UserName.setText("");
                    PhoneNumber.setText("");
                    email.setText("");
                    date_of_birth.setText("");
                    password.setText("");
                    policy.setChecked(false);
                    policy.setError(null);
                }
            }

        });
    }
    //toastMessage function
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}