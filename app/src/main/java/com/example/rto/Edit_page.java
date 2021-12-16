package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class Edit_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_page);


        Intent intent=getIntent();
        String s=intent.getStringExtra("number_plate");
        EditText et;
        et=findViewById(R.id.et_number_plate1);
        et.setText(s);
    }
}