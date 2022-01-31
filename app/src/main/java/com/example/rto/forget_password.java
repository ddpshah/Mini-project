package com.example.rto;

import static com.example.rto.MainActivity.hideSoftKeyboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class forget_password extends AppCompatActivity {

    private EditText forEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_forget_password);
        forEmail = findViewById(R.id.et_email_forget);
        Button forget = findViewById(R.id.btn_proceed_verify);

        forget.setOnClickListener(v -> {
            hideSoftKeyboard(forget_password.this);
            email = forEmail.getText().toString().toLowerCase();
            if (!(email.isEmpty())) {
                forEmail.setError(null);
                showAlertDialog();
                /*FirebaseAuth auth = FirebaseAuth.getInstance();
                View view = LayoutInflater.from(forget_password.this).inflate(R.layout.email_sent_forgot_password, (ConstraintLayout) findViewById(R.id.layoutDialog_password_forget)
                );
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                view.findViewById(R.id.button_proceed).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(forget_password.this, MainActivity.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                });*/
            } else {
                forEmail.setError("Email not Entered!");
                forEmail.requestFocus();
            }


        });
    }

    private void showAlertDialog() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(forget_password.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(forget_password.this).inflate(R.layout.email_sent_forgot_password,(ConstraintLayout) findViewById(R.id.layoutDialogContainer_success));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                view.findViewById(R.id.button_proceed).setOnClickListener(v -> {
                    alertDialog.dismiss();
                    Intent intent = new Intent(forget_password.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}