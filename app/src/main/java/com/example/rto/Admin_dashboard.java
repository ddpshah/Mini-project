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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin_dashboard extends AppCompatActivity {
    private static final String TAG = "";
    TextView logout;
    Button search, add, delete, edit;
    String number_plate_original, state_, city_, af_city_, four_digi_;
    EditText state_code, city_code, after_city_code, four_digit_code;
    Timer timer;
    String pattern = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
    Pattern patternObj = Pattern.compile(pattern);

    DatabaseReference reference;
    Trie trie = new Trie();

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

        final loading_op loadingdialog = new loading_op(Admin_dashboard.this);

        reference = FirebaseDatabase.getInstance().getReference("database");

        state_code = findViewById(R.id.et_statecode);
        city_code = findViewById(R.id.et_citycode);
        after_city_code = findViewById(R.id.et_aftercitycode);
        four_digit_code = findViewById(R.id.et_four_digit_no);

        logout = findViewById(R.id.btn_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add = findViewById(R.id.btn_Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);

                loadingdialog.startloading_loading();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        state_ = state_code.getText().toString();
                        city_ = city_code.getText().toString();
                        af_city_ = after_city_code.getText().toString();
                        four_digi_ = four_digit_code.getText().toString();
                        number_plate_original = (state_ + city_ + af_city_ + four_digi_).toUpperCase();
                        Matcher m = patternObj.matcher(number_plate_original);
                        if (m.find()) {
                            if (trie.search(number_plate_original).equals("")) {
                                trie.insert(number_plate_original);
                                Intent intent = new Intent(getApplicationContext(), Insert_page.class);
                                intent.putExtra("number_plate_insert", number_plate_original);
                                loadingdialog.dismissDialog();
                                startActivity(intent);
                            } else {
                                Thread thread = new Thread() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Number plate already exist!", Toast.LENGTH_SHORT).show();
                                                loadingdialog.dismissDialog();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }
                        } else {
                            Thread thread = new Thread() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                            loadingdialog.dismissDialog();
                                        }
                                    });
                                }
                            };
                            thread.start();
                        }
                    }
                }, 3000);

            }
        });

        search = findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                loadingdialog.startloading_loading();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int flag = 1;
                        state_ = state_code.getText().toString();
                        city_ = city_code.getText().toString();
                        af_city_ = after_city_code.getText().toString();
                        four_digi_ = four_digit_code.getText().toString();
                        number_plate_original = (state_ + city_ + af_city_ + four_digi_).toUpperCase();
                        Matcher m_search=patternObj.matcher(number_plate_original);
                        if (m_search.find()) {
                            if (trie.search(number_plate_original).equals("")) {
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
                                intent.putExtra("number_plate_search", number_plate_original);
                                intent.putExtra("flag", flag);
                                loadingdialog.dismissDialog();
                                startActivity(intent);
                            }
                        } else {
                            Thread thread = new Thread() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                            loadingdialog.dismissDialog();
                                        }
                                    });
                                }
                            };
                            thread.start();

                        }
                    }
                }, 3000);

            }
        });

        delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                state_ = state_code.getText().toString().toUpperCase();
                city_ = city_code.getText().toString().toUpperCase();
                af_city_ = after_city_code.getText().toString().toUpperCase();
                four_digi_ = four_digit_code.getText().toString().toUpperCase();
                number_plate_original = (state_ + city_ + af_city_ + four_digi_).toUpperCase();
                Matcher m_delete=patternObj.matcher(number_plate_original);
                if (m_delete.find()) {
                    if (trie.search(number_plate_original).equals("")) {
                        Thread thread = new Thread() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                                        loadingdialog.dismissDialog();
                                    }
                                });
                            }
                        };
                        thread.start();

                    } else {
                        Thread thread = new Thread() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        trie.remove(number_plate_original);
                                        reference.child(number_plate_original).removeValue();
                                        Toast.makeText(getApplicationContext(), "Car Details deleted successfully!", Toast.LENGTH_SHORT).show();
                                        loadingdialog.dismissDialog();
                                    }
                                });
                            }
                        };
                        thread.start();

                    }
                } else {
                    Thread thread = new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                    loadingdialog.dismissDialog();
                                }
                            });
                        }
                    };
                    thread.start();

                }
            }
        });

        edit = findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Admin_dashboard.this);
                loadingdialog.startloading_loading();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        state_ = state_code.getText().toString();
                        city_ = city_code.getText().toString();
                        af_city_ = after_city_code.getText().toString();
                        four_digi_ = four_digit_code.getText().toString();
                        number_plate_original = (state_ + city_ + af_city_ + four_digi_).toUpperCase();
                        Matcher m_edit=patternObj.matcher(number_plate_original);
                        if (m_edit.find()) {
                            if (trie.search(number_plate_original).equals("")) {
                                Thread thread = new Thread() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                                                loadingdialog.dismissDialog();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Edit_page.class);
                                intent.putExtra("number_plate", number_plate_original);
                                loadingdialog.dismissDialog();
                                startActivity(intent);
                            }
                        } else {
                            Thread thread = new Thread() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                            loadingdialog.dismissDialog();
                                        }
                                    });
                                }
                            };
                            thread.start();
                        }
                    }
                }, 3000);

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