package com.example.rto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Admin_dashboard extends AppCompatActivity {
    private static final String TAG = "";
    TextView logout;
    Button search,add,delete,edit;
    String number_plate_original,state_,city_,af_city_,four_digi_;
    EditText state_code,city_code,after_city_code,four_digit_code;
    Timer timer;

    DatabaseReference reference;
    Trie trie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_dashboard);

        reference = FirebaseDatabase.getInstance().getReference("database");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String temp = snapshot.child("numberplate").getValue(String.class).toUpperCase();
                    trie.insert(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        final loading_op loadingdialog=new loading_op(Admin_dashboard.this);

        reference= FirebaseDatabase.getInstance().getReference("database");

        state_code=findViewById(R.id.et_statecode);
        city_code=findViewById(R.id.et_citycode);
        after_city_code=findViewById(R.id.et_aftercitycode);
        four_digit_code=findViewById(R.id.et_four_digit_no);

        logout=findViewById(R.id.btn_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add=findViewById(R.id.btn_Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                loadingdialog.startloading_loading();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        state_=state_code.getText().toString();
                        city_=city_code.getText().toString();
                        af_city_=after_city_code.getText().toString();
                        four_digi_=four_digit_code.getText().toString();
                        number_plate_original=state_+city_+af_city_+four_digi_;
                        if (trie.insert(number_plate_original.toUpperCase())==-1) {
                            Toast.makeText(getApplicationContext(), "Number plate already exist!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),Insert_page.class);
                            intent.putExtra("number_plate_insert",number_plate_original.toUpperCase());
                            loadingdialog.dismissDialog();
                            startActivity(intent);
                        }
                    }
                },3000);

            }
        });

        search=findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                loadingdialog.startloading_loading();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int flag=1;
                        state_=state_code.getText().toString();
                        city_=city_code.getText().toString();
                        af_city_=after_city_code.getText().toString();
                        four_digi_=four_digit_code.getText().toString();
                        number_plate_original=state_+city_+af_city_+four_digi_;
                        if (trie.search(number_plate_original.toUpperCase()).equals("")) {
                            Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),Search.class);
                            intent.putExtra("number_plate_search",number_plate_original.toUpperCase());
                            intent.putExtra("flag",flag);
                            loadingdialog.dismissDialog();
                            startActivity(intent);
                        }
                    }
                },3000);

            }
        });

        delete=findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);

                if (trie.search(number_plate_original.toUpperCase()).equals("")) {
                    Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.child(number_plate_original).removeValue();
                    Toast.makeText(getApplicationContext(), "Car Details deleted successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit=findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                loadingdialog.startloading_loading();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        state_=state_code.getText().toString();
                        city_=city_code.getText().toString();
                        af_city_=after_city_code.getText().toString();
                        four_digi_=four_digit_code.getText().toString();
                        number_plate_original=state_+city_+af_city_+four_digi_;
                        if (trie.search(number_plate_original.toUpperCase()).equals("")) {
                            Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),Edit_page.class);
                            intent.putExtra("number_plate",number_plate_original.toUpperCase());
                            loadingdialog.dismissDialog();
                            startActivity(intent);
                        }
                    }
                },3000);

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