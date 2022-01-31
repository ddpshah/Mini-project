package com.example.rto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    TextView register;
    TextView admin_login, forget_password_user;
    Button login;
    TextInputEditText email_val, password_val;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();


        final loading_user_admin loadingdialog = new loading_user_admin(MainActivity.this);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //(Hides notification panel)

        if (!isConnected()) {
            //Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            Dialog dialog = new Dialog(MainActivity.this, R.style.NoInternetDialog);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.show();
            Button close;
            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                    dialog.cancel();
                }
            });
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finishAffinity();
                        dialog.cancel();
                    }
                    return true;
                }
            });
        } else {
            setContentView(R.layout.activity_main);

            forget_password_user = findViewById(R.id.forgot_password_user);
            forget_password_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,forget_password.class);
                    startActivity(intent);
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.email_sent_forgot_password, (ConstraintLayout) findViewById(R.id.layoutDialogContainer_success));
                    builder.setView(view);
                    AlertDialog alertDialog1 = builder.create();
                    view.findViewById(R.id.button_proceed).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog1.dismiss();
                        }
                    });*/
                }
            });


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

            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            login = findViewById(R.id.btn_login_admin);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email_val = findViewById(R.id.et_email_user_input);
                    password_val = findViewById(R.id.et_password_user_input);
                    String email_user = email_val.getText().toString().toLowerCase();
                    String password_user = password_val.getText().toString();
                    hideSoftKeyboard(MainActivity.this);
                    loadingdialog.startloading_user_admin();
                    if (!(email_user.isEmpty())) {
                        email_val.setError(null);
                        //password_val.setErrorEnabled(false);
                        if ((email_user.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))) {
                            email_val.setError(null);
                            if (!(password_user.isEmpty())) {
                                password_val.setError(null);
                                final String email_data = email_val.getText().toString().toLowerCase();
                                final String password_data = password_val.getText().toString();

                                mAuth.signInWithEmailAndPassword(email_data, password_data).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            loadingdialog.dismissDialog();
                                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), User_dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            loadingdialog.dismissDialog();
                                            Toast.makeText(getApplicationContext(), "User doesn't exist!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                loadingdialog.dismissDialog();
                                password_val.setError("Password not Entered!");
                                password_val.requestFocus();
                            }
                        } else {
                            loadingdialog.dismissDialog();
                            password_val.setError("Please enter a valid email address!");
                            password_val.requestFocus();
                        }

                    } else {
                        loadingdialog.dismissDialog();
                        email_val.setError("Email not Entered!");
                        email_val.requestFocus();
                    }

                }

            });

        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}