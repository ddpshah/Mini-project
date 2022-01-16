package com.example.rto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class Register_activity extends AppCompatActivity {
    ImageView backbutton;
    Button login_button;
    Button register;
    private static final String TAG = "";
    TextInputEditText email_val, password_val, repassword_val;
    String email_;
    String password_;
    String repassword_;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //(Hides notification panel)
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        backbutton = findViewById(R.id.backbutton);
        login_button = findViewById(R.id.login_button);
        register = findViewById(R.id.signup);
        email_val = findViewById(R.id.et_email);
        password_val = findViewById(R.id.et_password_user_signup);
        repassword_val = findViewById(R.id.et_repassword_user_signup);
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
                email_ = email_val.getText().toString().toLowerCase();
                password_ = password_val.getText().toString();
                repassword_ = repassword_val.getText().toString();
                hideSoftKeyboard(Register_activity.this);
                loadingdialog.startloading_user_admin();

                if (!(email_).isEmpty()) {
                    email_val.setError(null);
                    //email_val.setErrorEnabled(false);
                    if ((email_.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))) {
                        email_val.setError(null);
                        if (!(password_).isEmpty()) {
                            password_val.setError(null);
                            if (password_.matches("^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}$")) {
                                password_val.setError(null);
                                // password_val.setErrorEnabled(false);
                                if (!(repassword_).isEmpty()) {
                                    repassword_val.setError(null);
                                    //repassword_val.setErrorEnabled(false);
                                    if (password_.equals(repassword_)) {
                                        String email_s = email_val.getText().toString();
                                        String password_s = password_val.getText().toString();
                                        String repassword_s = repassword_val.getText().toString();
                                        mAuth.createUserWithEmailAndPassword(email_s, password_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    mAuth.getCurrentUser().getUid();
                                                    loadingdialog.dismissDialog();
                                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), User_dashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    loadingdialog.dismissDialog();
                                                    Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        loadingdialog.dismissDialog();
                                        repassword_val.setError("Incorrect password");
                                        repassword_val.requestFocus();
                                    }
                                } else {
                                    loadingdialog.dismissDialog();
                                    repassword_val.setError("Please enter password again!");
                                    repassword_val.requestFocus();
                                }

                            } else {
                                loadingdialog.dismissDialog();
                                password_val.setError("Please enter password of length 8 characters,two digits,two uppercase,three lowercase,one special character!");
                                password_val.requestFocus();
                            }
                        } else {
                            loadingdialog.dismissDialog();
                            password_val.setError("Please enter a password!");
                            password_val.requestFocus();
                        }
                    } else {
                        email_val.setError("Invalid Email entered!");
                        email_val.requestFocus();
                        loadingdialog.dismissDialog();
                    }
                } else {
                    loadingdialog.dismissDialog();
                    email_val.setError("Please enter email address!");
                    email_val.requestFocus();
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