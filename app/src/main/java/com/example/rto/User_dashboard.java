package com.example.rto;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class User_dashboard extends AppCompatActivity {
    TextView logout;
    Button Search;
    DatabaseReference reference;
    private EditText number_plate_original;
    String pattern = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
    Pattern patternObj = Pattern.compile(pattern);
    private String numberplate_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_user_dashboard);
        final loading_op loadingdialog = new loading_op(User_dashboard.this);
        number_plate_original = findViewById(R.id.et_numberplate_original_user);

        Search = findViewById(R.id.btn_search_user);
        Search.setOnClickListener(v -> {
            hideSoftKeyboard(User_dashboard.this);
            loadingdialog.startloading_loading();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    int flag = 0;
                    numberplate_ = number_plate_original.getText().toString().toUpperCase();
                    Matcher m = patternObj.matcher(numberplate_);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("database");

                    if (m.find()) {
                        reference.child(numberplate_).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), Search.class);
                                intent.putExtra("number_plate_search_user", numberplate_);
                                intent.putExtra("flag", flag);
                                loadingdialog.dismissDialog();
                                startActivity(intent);
                            } else {
                                Thread thread = new Thread() {
                                    public void run() {
                                        runOnUiThread(() -> {
                                            Toast.makeText(getApplicationContext(), "Number Plate doesn't exist!", Toast.LENGTH_SHORT).show();
                                            loadingdialog.dismissDialog();
                                        });
                                    }
                                };
                                thread.start();
                            }
                        });
                    } else {
                        Thread thread = new Thread() {
                            public void run() {
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                    loadingdialog.dismissDialog();
                                });
                            }
                        };
                        thread.start();
                    }
                }
            }, 3000);

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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}