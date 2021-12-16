package com.example.rto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Search extends AppCompatActivity {
    ImageView backbutton;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);
        backbutton=findViewById(R.id.imageView);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Admin_dashboard.class);
                startActivity(intent);
                finish();
            }
        });


        Intent intent=getIntent();
        int page=intent.getExtras().getInt("flag");
        if(page==1) {
            String numberplate_ = intent.getStringExtra("number_plate_search");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(numberplate_);

            readData(numberplate_);

        }
        else if(page==0){
            String s = intent.getStringExtra("number_plate_search_user");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(s);
        }


    }

    private void readData(String number_plate){

        reference= FirebaseDatabase.getInstance().getReference("database");
        reference.child(number_plate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(getApplicationContext(), "Car Details Found!", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot=task.getResult();
                        String registration_date_=String.valueOf(dataSnapshot.child("registration_date").getValue());
                        String engine_no_=String.valueOf(dataSnapshot.child("engine_no").getValue());
                        String Owner_name_=String.valueOf(dataSnapshot.child("owner_name").getValue());
                        String Vehicle_class_=String.valueOf(dataSnapshot.child("vehicle_class").getValue());
                        String fuel_type_=String.valueOf(dataSnapshot.child("fuel_type").getValue());
                        String maker_model_=String.valueOf(dataSnapshot.child("maker_model").getValue());
                        String rc_status_=String.valueOf(dataSnapshot.child("rc_status").getValue());
                        String number_plate=String.valueOf(dataSnapshot.child("numberplate").getValue());

                        TextView registration_date,engine_no,owner_name,vehicle_class,fuel_type,maker_model,rc_status;
                        registration_date=findViewById(R.id.tv_registration_number);
                        engine_no=findViewById(R.id.tv_engine_no);
                        owner_name=findViewById(R.id.tv_owner_name);
                        vehicle_class=findViewById(R.id.tv_vehicle_class);
                        fuel_type=findViewById(R.id.tv_fuel_type);
                        maker_model=findViewById(R.id.tv_model);
                        rc_status=findViewById(R.id.tv_rc_status);

                        registration_date.setText(registration_date_);
                        engine_no.setText(engine_no_);
                        owner_name.setText(Owner_name_);
                        vehicle_class.setText(Vehicle_class_);
                        fuel_type.setText(fuel_type_);
                        maker_model.setText(maker_model_);
                        rc_status.setText(rc_status_);


                    }else {
                        Toast.makeText(getApplicationContext(), "Car with this number doesn't exist!", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve Data!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}