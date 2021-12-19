package com.example.rto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.nio.file.*;

public class Admin_login extends AppCompatActivity {

    private static final String TAG = "";
    private Button login;
    ImageView back_btn;
    TextInputEditText username_val, password_val, code_val;
    String code=new String("431122");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_login);
        back_btn = findViewById(R.id.back_button_admin);
        login = findViewById(R.id.btn_login_admin);
        username_val = findViewById(R.id.et_username_admin_input);
        password_val = findViewById(R.id.et_password_admin_input);
        code_val = findViewById(R.id.et_security_code_admin_input);

        back_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        Trie trie = new Trie();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("database");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String temp = snapshot.child("numberplate").getValue(String.class).toUpperCase();
                    trie.insert(temp);
                    Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_admin = username_val.getText().toString();
                String password_admin = password_val.getText().toString();
                String security_admin = code_val.getText().toString();

                if (!(username_admin).isEmpty()) {
                    username_val.setError(null);
                    if (!(password_admin).isEmpty()) {
                        password_val.setError(null);
                        if (!(security_admin).isEmpty()) {
                            code_val.setError(null);
                            final String username_data = username_val.getText().toString();
                            final String password_data = password_val.getText().toString();
                            final String security_data=code_val.getText().toString();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("datauser");

                            Query check_username = databaseReference.orderByChild("username").equalTo(username_data);
                            check_username.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        username_val.setError(null);
                                        String password_check = snapshot.child(username_data).child("password").getValue(String.class);
                                        if (password_check.equals(password_data)) {
                                            if (security_data.equals(code)) {
                                                password_val.setError(null);
                                                code_val.setError(null);
                                                //password_val.setErrorEnabled(false);
                                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Admin_dashboard.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                code_val.setError("Incorrect Security code entered.");
                                            }
                                        } else {
                                            password_val.setError("Incorrect password");
                                        }
                                    } else {
                                        username_val.setError("Username doesn't exist.");
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }

                            });
                            Query check_password = databaseReference.orderByChild("password").equalTo(password_data);
                        } else {
                            code_val.setError("Security code not Entered!");
                        }
                    } else {
                        password_val.setError("Password not Entered!");
                    }
                } else {
                    username_val.setError("Username not Entered!");

                }
            }
        });
    }
}