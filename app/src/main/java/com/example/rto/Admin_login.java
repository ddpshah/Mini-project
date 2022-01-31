package com.example.rto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;

public class Admin_login extends AppCompatActivity {

    private Button login;
    ImageView back_btn;
    TextInputEditText email_val, password_val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_login);


        back_btn = findViewById(R.id.back_button_admin);
        login = findViewById(R.id.btn_login_admin);
        email_val = findViewById(R.id.et_email_admin_input);
        password_val = findViewById(R.id.et_password_admin_input);
        final loading_user_admin loadingdialog = new loading_user_admin(Admin_login.this);

        back_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_login.this);
                loadingdialog.startloading_user_admin();
                String email_admin = email_val.getText().toString();
                String password_admin = password_val.getText().toString();
                if (!(email_admin).isEmpty()) {
                    email_val.setError(null);
                    if (!(password_admin).isEmpty()) {
                        password_val.setError(null);
                        final String email_data = email_val.getText().toString();
                        final String password_data = password_val.getText().toString();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("adminData");

                        Query check_email = databaseReference.orderByChild("username").equalTo("admin");
                        check_email.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    email_val.setError(null);
                                    String password_check = snapshot.child("admin").child("password").getValue(String.class);
                                    if (password_check.equals(password_data)) {
                                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Admin_dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        loadingdialog.dismissDialog();
                                        password_val.setError("Incorrect password");
                                        password_val.requestFocus();
                                    }
                                } else {
                                    loadingdialog.dismissDialog();
                                    email_val.setError("Email doesn't exist");
                                    email_val.requestFocus();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });
                        Query check_password = databaseReference.orderByChild("password").equalTo(password_data);
                    } else {
                        loadingdialog.dismissDialog();
                        password_val.setError("Password not Entered!");
                        password_val.requestFocus();
                    }
                } else {
                    loadingdialog.dismissDialog();
                    email_val.setError("Email not entered!");
                    email_val.requestFocus();

                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}