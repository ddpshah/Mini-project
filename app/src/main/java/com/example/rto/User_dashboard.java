package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class User_dashboard extends AppCompatActivity {
    TextView logout;
    Button Search;
    String number_plate_original;
    EditText state_code,city_code,after_city_code,four_digit_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_dashboard);
        state_code=findViewById(R.id.et_statecode_user);
        city_code=findViewById(R.id.et_citycode_user);
        after_city_code=findViewById(R.id.et_aftercitycode_user);
        four_digit_code=findViewById(R.id.et_four_digit_no_user);

        logout=findViewById(R.id.btn_log_out_user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Search=findViewById(R.id.btn_search_user);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                String state_=state_code.getText().toString();
                String city_=city_code.getText().toString();
                String af_city_=after_city_code.getText().toString();
                String four_digi_=four_digit_code.getText().toString();
                number_plate_original=state_+city_+af_city_+four_digi_;
                Intent intent=new Intent(getApplicationContext(),Search.class);
                intent.putExtra("number_plate_search_user",number_plate_original);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
    }
}