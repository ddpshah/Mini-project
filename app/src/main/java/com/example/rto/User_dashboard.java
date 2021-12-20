package com.example.rto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_dashboard extends AppCompatActivity {
    TextView logout;
    private static final String TAG = "";
    Button Search;
    String number_plate_original;
    DatabaseReference reference;
    Trie trie = new Trie();
    EditText state_code, city_code, after_city_code, four_digit_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_dashboard);
        final loading_op loadingdialog = new loading_op(User_dashboard.this);

        reference = FirebaseDatabase.getInstance().getReference("database");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String temp = snapshot.child("numberplate").getValue(String.class).toUpperCase();
                    trie.insert(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
        state_code = findViewById(R.id.et_statecode_user);
        city_code = findViewById(R.id.et_citycode_user);
        after_city_code = findViewById(R.id.et_aftercitycode_user);
        four_digit_code = findViewById(R.id.et_four_digit_no_user);

        logout = findViewById(R.id.btn_log_out_user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Search = findViewById(R.id.btn_search_user);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(User_dashboard.this);
                loadingdialog.startloading_loading();
                int flag = 0;
                String state_ = state_code.getText().toString().toUpperCase();
                String city_ = city_code.getText().toString().toUpperCase();
                String af_city_ = after_city_code.getText().toString().toUpperCase();
                String four_digi_ = four_digit_code.getText().toString().toUpperCase();
                number_plate_original = state_ + city_ + af_city_ + four_digi_;

                if (trie.search(number_plate_original.toUpperCase()).equals("")) {
                    Thread thread = new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Number Plate doesn't exist!", Toast.LENGTH_SHORT).show();
                                    loadingdialog.dismissDialog();
                                }
                            });
                        }
                    };
                    thread.start();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Search.class);
                    intent.putExtra("number_plate_search_user", number_plate_original.toUpperCase());
                    intent.putExtra("flag", flag);
                    loadingdialog.dismissDialog();
                    startActivity(intent);
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