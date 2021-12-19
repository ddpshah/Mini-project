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
import java.util.TimerTask;

public class Admin_login extends AppCompatActivity {

    private Button login;
    ImageView back_btn;
    TextInputEditText username_val, password_val, code_val;
    String code=new String("431122");
    Timer timer;


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

        final loading_user_admin loadingdialog=new loading_user_admin(Admin_login.this);

        back_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_login.this);
                loadingdialog.startloading_user_admin();
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

                                                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Admin_dashboard.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                loadingdialog.dismissDialog();
                                                code_val.setError("Incorrect Security code entered.");
                                                code_val.requestFocus();
                                            }
                                        } else {
                                            loadingdialog.dismissDialog();
                                            password_val.setError("Incorrect password");
                                            password_val.requestFocus();
                                        }
                                    } else {
                                        loadingdialog.dismissDialog();
                                        username_val.setError("Username doesn't exist.");
                                        username_val.requestFocus();
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }

                            });
                            Query check_password = databaseReference.orderByChild("password").equalTo(password_data);
                        } else {
                            loadingdialog.dismissDialog();
                            code_val.setError("Security code not Entered!");
                            code_val.requestFocus();
                        }
                    } else {
                        loadingdialog.dismissDialog();
                        password_val.setError("Password not Entered!");
                        password_val.requestFocus();
                    }
                } else {
                    loadingdialog.dismissDialog();
                    username_val.setError("Username not Entered!");
                    username_val.requestFocus();

                }
            }
        });
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}