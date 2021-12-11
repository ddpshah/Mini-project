package com.example.rto;

import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    TextView register;
    TextView admin_login;
    Button login;
    TextInputEditText username_val, password_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //(Hides notification panel)

        if (!isConnected()) {
            //Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            Dialog dialog = new Dialog(MainActivity.this, R.style.NoInternetDialog);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.show();
            Button close;
            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v -> finishAffinity());
        }


        register = findViewById(R.id.Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_activity.class);
                startActivity(intent);
            }
        });

        admin_login = findViewById(R.id.btn_Admin);
        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Admin_login.class);
                startActivity(intent);
            }
        });


        login = findViewById(R.id.btn_login_admin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_val = findViewById(R.id.et_username_user_input);
                password_val = findViewById(R.id.et_password_user_input);
                String username_user = username_val.getText().toString();
                String password_user = password_val.getText().toString();

                if (!(username_user.isEmpty())) {
                    username_val.setError(null);
                    //email_val.setErrorEnabled(false);
                    if (!(password_user.isEmpty())) {
                        password_val.setError(null);
                        //password_val.setErrorEnabled(false);

                        final String username_data = username_val.getText().toString();
                        final String password_data = password_val.getText().toString();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("datauser");

                        Query check_email = databaseReference.orderByChild("username").equalTo(username_data);
                        check_email.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    username_val.setError(null);
                                    //email_val.setErrorEnabled(false);
                                    String password_check = snapshot.child(username_data).child("password").getValue(String.class);
                                    if (password_check.equals(password_data)) {
                                        password_val.setError(null);
                                        //password_val.setErrorEnabled(false);
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), User_dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        password_val.setError("Incorrect password");
                                    }
                                } else {
                                    username_val.setError("Username doesn't exist, please register first!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Query check_password = databaseReference.orderByChild("password").equalTo(password_data);

                    } else {
                        password_val.setError("Password not Entered!");
                    }

                } else {
                    username_val.setError("Username not Entered!");
                }

            }

        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}