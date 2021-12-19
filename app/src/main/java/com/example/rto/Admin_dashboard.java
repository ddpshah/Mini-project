package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Admin_dashboard extends AppCompatActivity {
    TextView logout;
    Button search,add,delete,edit;
    TextInputEditText state,city,after_city,four_digit;
    String number_plate_original;
    EditText state_code,city_code,after_city_code,four_digit_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_dashboard);
        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

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
                String state_=state_code.getText().toString();
                String city_=city_code.getText().toString();
                String af_city_=after_city_code.getText().toString();
                String four_digi_=four_digit_code.getText().toString();
                number_plate_original=state_+city_+af_city_+four_digi_;
                Intent intent=new Intent(getApplicationContext(),Insert_page.class);
                intent.putExtra("number_plate_insert",number_plate_original);
                startActivity(intent);
            }
        });

        search=findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=1;
                String state_=state_code.getText().toString();
                String city_=city_code.getText().toString();
                String af_city_=after_city_code.getText().toString();
                String four_digi_=four_digit_code.getText().toString();
                number_plate_original=state_+city_+af_city_+four_digi_;
                Intent intent=new Intent(getApplicationContext(),Search.class);
                intent.putExtra("number_plate_search",number_plate_original);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });

        delete=findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Respective Car Details deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        edit=findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state_=state_code.getText().toString();
                String city_=city_code.getText().toString();
                String af_city_=after_city_code.getText().toString();
                String four_digi_=four_digit_code.getText().toString();
                number_plate_original=state_+city_+af_city_+four_digi_;
                Intent intent=new Intent(getApplicationContext(),Edit_page.class);
                intent.putExtra("number_plate",number_plate_original);
                startActivity(intent);
            }
        });
    }
}