package com.example.rto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin_dashboard extends AppCompatActivity {
    TextView logout;
    Button search, add, delete, edit;
    EditText number_plate_original;
    String numberplate_;
    Timer timer;
    String pattern = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";
    Pattern patternObj = Pattern.compile(pattern);

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_dashboard);

        final loading_op loadingdialog = new loading_op(Admin_dashboard.this);
        reference = FirebaseDatabase.getInstance().getReference("database");
        number_plate_original = findViewById(R.id.et_numberplate_original_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_dashboard.this, R.style.AlertDialogTheme);


        logout = findViewById(R.id.btn_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add = findViewById(R.id.btn_Add);
        add.setOnClickListener(v -> {
            hideSoftKeyboard(Admin_dashboard.this);
            numberplate_ = number_plate_original.getText().toString().toUpperCase();
            Matcher m = patternObj.matcher(numberplate_);
            if (!(numberplate_).isEmpty()) {
                number_plate_original.setError(null);
                loadingdialog.startloading_loading();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (m.find()) {
                            reference.child(numberplate_).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    Thread thread = new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                Toast.makeText(getApplicationContext(), "Number plate already exist!", Toast.LENGTH_SHORT).show();
                                                loadingdialog.dismissDialog();
                                            });
                                        }
                                    };
                                    thread.start();

                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Insert_page.class);
                                    intent.putExtra("number_plate_insert", numberplate_);
                                    loadingdialog.dismissDialog();
                                    startActivity(intent);
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

            } else {
                number_plate_original.setError("Vehicle number not entered!");
                number_plate_original.requestFocus();
            }
        });

        search = findViewById(R.id.btn_search);
        search.setOnClickListener(v -> {
            hideSoftKeyboard(Admin_dashboard.this);
            loadingdialog.startloading_loading();
            numberplate_ = number_plate_original.getText().toString().toUpperCase();
            Matcher m_search = patternObj.matcher(numberplate_);
            if (!(numberplate_).isEmpty()) {
                number_plate_original.setError(null);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int flag = 1;
                        if (m_search.find()) {
                            reference.child(numberplate_).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    Intent intent = new Intent(getApplicationContext(), Search.class);
                                    intent.putExtra("number_plate_search", numberplate_);
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
            } else {
                loadingdialog.dismissDialog();
                number_plate_original.setError("Vehicle number not entered!");
                number_plate_original.requestFocus();
            }
        });

        delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(v -> {
            hideSoftKeyboard(Admin_dashboard.this);
            numberplate_ = number_plate_original.getText().toString().toUpperCase();
            Matcher m_delete = patternObj.matcher(numberplate_);
            if (!(numberplate_).isEmpty()) {
                number_plate_original.setError(null);
                View view = LayoutInflater.from(Admin_dashboard.this).inflate(R.layout.warning_dialog, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
                );
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (m_delete.find()) {
                            reference.child(numberplate_).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    Thread thread = new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                reference.child(numberplate_).removeValue();
                                                Toast.makeText(getApplicationContext(), "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            });
                                        }
                                    };
                                    thread.start();
                                } else {
                                    Thread thread = new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
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
                                        alertDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Please enter valid number plate!", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            };
                            thread.start();
                        }

                    }
                });

                view.findViewById(R.id.button_cancel_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            } else {
                number_plate_original.setError("Vehicle number not entered!");
                number_plate_original.requestFocus();
            }
        });

        edit = findViewById(R.id.btn_edit);
        edit.setOnClickListener(v -> {
            hideSoftKeyboard(Admin_dashboard.this);
            numberplate_ = number_plate_original.getText().toString().toUpperCase();
            Matcher m_edit = patternObj.matcher(numberplate_);
            if (!(numberplate_).isEmpty()) {
                number_plate_original.setError(null);
                loadingdialog.startloading_loading();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (m_edit.find()) {
                            reference.child(numberplate_).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    Intent intent = new Intent(getApplicationContext(), Edit_page.class);
                                    intent.putExtra("number_plate", numberplate_);
                                    loadingdialog.dismissDialog();
                                    startActivity(intent);
                                } else {
                                    Thread thread = new Thread() {
                                        public void run() {
                                            runOnUiThread(() -> {
                                                Toast.makeText(getApplicationContext(), "Number plate does not exist!", Toast.LENGTH_SHORT).show();
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
            } else {
                number_plate_original.setError("Vehicle number not entered!");
                number_plate_original.requestFocus();
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