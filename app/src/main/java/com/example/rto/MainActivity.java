package com.example.rto;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

import java.io.Serializable;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    TextView register;
    TextView admin_login;
    Button login;
    TextInputEditText username_val, password_val;
    Timer timer;
    DatabaseReference reference;
    Trie trie = new Trie();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference("database");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String temp = snapshot.child("numberplate").getValue(String.class).toUpperCase();
                    trie.insert(temp);
                    Toast.makeText(getApplicationContext(), trie.search(temp), Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(MainActivity.this,Search.class);
                intent.putExtra("trie", trie);
                //startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        final loading_user_admin loadingdialog=new loading_user_admin(MainActivity.this);

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
                hideSoftKeyboard(MainActivity.this);
                loadingdialog.startloading_user_admin();
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


                                                Intent intent = new Intent(getApplicationContext(), User_dashboard.class);
                                                loadingdialog.dismissDialog();
                                                startActivity(intent);
                                                finishAffinity();

                                    } else {
                                        loadingdialog.dismissDialog();
                                        password_val.setError("Incorrect password");
                                        password_val.requestFocus();
                                    }
                                } else {
                                    loadingdialog.dismissDialog();
                                    username_val.setError("Username doesn't exist, please register first!");
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

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
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