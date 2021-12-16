package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class Search extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        Intent intent=getIntent();
        int page=intent.getExtras().getInt("flag");
        if(page==1) {
            String s = intent.getStringExtra("number_plate_search");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(s);
        }
        else if(page==0){
            String s = intent.getStringExtra("number_plate_search_user");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(s);
        }


    }
}