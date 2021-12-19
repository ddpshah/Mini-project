package com.example.rto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_activity extends AppCompatActivity {
    ImageView backbutton;
    Button login_button;
    Button register;
    TextInputEditText name_val, phone_val, email_val, password_val;
    String name_;
    String phone_;
    String email_;
    String password_;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //(Hides notification panel)
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        backbutton = findViewById(R.id.backbutton);
        login_button = findViewById(R.id.login_button);
        register = findViewById(R.id.signup);
        name_val = findViewById(R.id.et_username_new_input);
        phone_val = findViewById(R.id.et_phone_no);
        email_val = findViewById(R.id.et_email_user_signup);
        password_val = findViewById(R.id.et_password_user_signup);
        final loading_user_admin loadingdialog = new loading_user_admin(Register_activity.this);

        backbutton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_ = name_val.getText().toString();
                phone_ = phone_val.getText().toString();
                email_ = email_val.getText().toString();
                password_ = password_val.getText().toString();
                hideSoftKeyboard(Register_activity.this);
                loadingdialog.startloading_user_admin();

                if (!(name_).isEmpty()) {
                    name_val.setError(null);
                    if (!(phone_).isEmpty()) {
                        phone_val.setError(null);
                        //phone_val.setErrorEnabled(false);
                        if ((phone_.matches("\\d{10}"))) {
                            if (!(email_).isEmpty()) {
                                email_val.setError(null);
                                // email_val.setErrorEnabled(false);
                                if (!(password_).isEmpty()) {
                                    password_val.setError(null);
                                    //password_val.setErrorEnabled(false);

                                    if (email_.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        reference = firebaseDatabase.getReference("datauser");
                                        String username = name_val.getText().toString();
                                        String phone_s = phone_val.getText().toString();
                                        String email_s = email_val.getText().toString();
                                        String password_s = password_val.getText().toString();

                                        storing_data storing_dataobj = new storing_data(username, phone_s, email_s, password_s);
                                        reference.child(username).setValue(storing_dataobj);
                                        Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), User_dashboard.class);
                                        loadingdialog.dismissDialog();
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        loadingdialog.dismissDialog();
                                        email_val.setError("Invalid Email!");
                                        email_val.requestFocus();
                                    }
                                } else {
                                    loadingdialog.dismissDialog();
                                    password_val.setError("Password not Entered!");
                                    password_val.requestFocus();
                                }
                            } else {
                                loadingdialog.dismissDialog();
                                email_val.setError("Email Address not Entered!");
                                email_val.requestFocus();
                            }
                        } else {
                            phone_val.setError("Invalid Mobile Number!");
                            phone_val.requestFocus();
                            loadingdialog.dismissDialog();
                        }
                    } else {
                        loadingdialog.dismissDialog();
                        phone_val.setError("Phone number not Entered!");
                        phone_val.requestFocus();
                    }
                } else {
                    loadingdialog.dismissDialog();
                    name_val.setError("Username not Entered!");
                    name_val.requestFocus();
                }
            }
        });

        login_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));

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