package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Insert_page extends AppCompatActivity {
    Button Insert;
    TextInputEditText number_plate_val,registration_date_val,engine_no_val,owner_name_val,vehicle_class_val,fuel_type_val,maker_val,rc_val;
    String number_plate_,registration_date_,engine_no_,owner_name_,vehicle_class_,fuel_type_,maker_,rc_status_;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent=getIntent();
        String s=intent.getStringExtra("number_plate_insert");
        setContentView(R.layout.activity_insert_page);
        EditText et;
        et=findViewById(R.id.et_number_plate_input);
        et.setText(s);

        number_plate_val=findViewById(R.id.et_number_plate_input);
        registration_date_val=findViewById(R.id.et_registrationinput);
        engine_no_val=findViewById(R.id.et_engine_no);
        owner_name_val=findViewById(R.id.et_ownername_insert_input);
        vehicle_class_val=findViewById(R.id.vehicleclass_insert_input);
        fuel_type_val=findViewById(R.id.et_fueltype_insert);
        maker_val=findViewById(R.id.Maker_model_insert_input);
        rc_val=findViewById(R.id.et_rc_status_insert);

        Insert=findViewById(R.id.btn_insert_input);
        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_plate_=number_plate_val.getText().toString();
                registration_date_=registration_date_val.getText().toString();
                engine_no_=engine_no_val.getText().toString();
                owner_name_=owner_name_val.getText().toString();
                vehicle_class_=vehicle_class_val.getText().toString();
                fuel_type_=fuel_type_val.getText().toString();
                maker_=maker_val.getText().toString();
                rc_status_=rc_val.getText().toString();

                if(!(number_plate_).isEmpty()){
                    number_plate_val.setError(null);
                    if(!(registration_date_).isEmpty()){
                        registration_date_val.setError(null);
                        if(!(engine_no_).isEmpty()){
                            engine_no_val.setError(null);
                            if(!(owner_name_).isEmpty()){
                                owner_name_val.setError(null);
                                if(!(vehicle_class_).isEmpty()){
                                    vehicle_class_val.setError(null);
                                    if(!(fuel_type_).isEmpty()){
                                        fuel_type_val.setError(null);
                                        if(!(maker_).isEmpty()){
                                            maker_val.setError(null);
                                            if(!(rc_status_).isEmpty()){
                                                rc_val.setError(null);
                                                firebaseDatabase=FirebaseDatabase.getInstance();
                                                reference= firebaseDatabase.getReference("database");
                                                String numberplate=number_plate_val.getText().toString();
                                                String registrationdate=registration_date_val.getText().toString();
                                                String engineno=engine_no_val.getText().toString();
                                                String ownername=owner_name_val.getText().toString();
                                                String vehicleclass=vehicle_class_val.getText().toString();
                                                String fueltype=fuel_type_val.getText().toString();
                                                String makermodel=maker_val.getText().toString();
                                                String rcstatus=rc_val.getText().toString();

                                                storing_data_cars storing_dataobj1=new storing_data_cars(registrationdate,engineno,ownername,vehicleclass,fueltype,makermodel,rcstatus,numberplate);
                                                reference.child(numberplate).setValue(storing_dataobj1);
                                                Toast.makeText(getApplicationContext(), "Car Details entered Successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else {
                                                rc_val.setError("RC status not Entered!");
                                            }
                                        }else {
                                            maker_val.setError("Maker/Model name not Entered!");
                                        }
                                    }else {
                                        fuel_type_val.setError("Fuel type not Entered!");
                                    }
                                }else {
                                    vehicle_class_val.setError("Vehicle class not Entered!");
                                }
                            }else {
                                owner_name_val.setError("Owner name not Entered!");
                            }
                        }else {
                            engine_no_val.setError("Engine number not Entered!");
                        }
                    }else {
                        registration_date_val.setError("Registration Date not Entered!");
                    }

                }else {
                    number_plate_val.setError("Number Plate not Entered!");

                }

            }
        });


    }
}