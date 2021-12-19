package com.example.rto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import java.util.Timer;
import java.util.TimerTask;

public class Edit_page extends AppCompatActivity {
    ImageView backbutton;

    String numberplate, registration_date_, engine_no_, Owner_name_, fuel_type_, Vehicle_class_, maker_model_, rc_status_;
    EditText registration_date, engine_no, owner_name, vehicle_class, fuel_type, maker_model, rc_status;
    DatabaseReference reference;
    TextView number_plate_tv;
    Timer timer;
    loading_op loadingdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_edit_page);
        loadingdialog = new loading_op(Edit_page.this);

        registration_date = findViewById(R.id.et_registration_edit);
        engine_no = findViewById(R.id.et_engine_no_edit);
        owner_name = findViewById(R.id.et_ownername_edit);
        vehicle_class = findViewById(R.id.et_vehicle_class);
        fuel_type = findViewById(R.id.et_fueltype_edit);
        maker_model = findViewById(R.id.et_model_edit);
        rc_status = findViewById(R.id.et_rc_status_edit);

        reference = FirebaseDatabase.getInstance().getReference("database");

        backbutton = findViewById(R.id.imageView2);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        numberplate = intent.getStringExtra("number_plate");
        number_plate_tv = findViewById(R.id.et_number_plate1);
        number_plate_tv.setText(numberplate);

        readData(numberplate);
    }

    public void update(View view) {
        if (isRegistration_date_changed() || isEngineNochanged() || isOwnerNamechanged() || isVehicleClasschanged() || isFuelTypechanged() || isModelchanged() || isRCStatuschanged()) {
            loadingdialog.startloading_loading();
            while (isRegistration_date_changed() || isEngineNochanged() || isOwnerNamechanged() || isVehicleClasschanged() || isFuelTypechanged() || isModelchanged() || isRCStatuschanged() == true) {

            }
            Toast.makeText(getApplicationContext(), "Data has been updated!", Toast.LENGTH_SHORT).show();
            loadingdialog.dismissDialog();
        } else {
            Toast.makeText(getApplicationContext(), "Same data cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isRCStatuschanged() {
        if (!rc_status_.equals(rc_status.getText().toString())) {

            reference.child(numberplate).child("rc_status").setValue(rc_status.getText().toString());
            rc_status_ = rc_status.getText().toString();
            return true;

        } else {
            return false;
        }
    }

    private boolean isModelchanged() {
        if (!maker_model_.equals(maker_model.getText().toString())) {

            reference.child(numberplate).child("maker_model").setValue(maker_model.getText().toString());
            maker_model_ = maker_model.getText().toString();

            return true;

        } else {
            return false;
        }
    }

    private boolean isFuelTypechanged() {
        if (!fuel_type_.equals(fuel_type.getText().toString())) {

            reference.child(numberplate).child("fuel_type").setValue(fuel_type.getText().toString());
            fuel_type_ = fuel_type.getText().toString();
            return true;

        } else {
            return false;
        }
    }

    private boolean isVehicleClasschanged() {
        if (!Vehicle_class_.equals(vehicle_class.getText().toString())) {

            reference.child(numberplate).child("vehicle_class").setValue(vehicle_class.getText().toString());
            Vehicle_class_ = vehicle_class.getText().toString();
            return true;

        } else {
            return false;
        }
    }

    private boolean isOwnerNamechanged() {
        if (!Owner_name_.equals(owner_name.getText().toString())) {

            reference.child(numberplate).child("owner_name").setValue(owner_name.getText().toString());
            Owner_name_ = owner_name.getText().toString();
            return true;

        } else {
            return false;
        }
    }

    private boolean isEngineNochanged() {
        if (!engine_no_.equals(engine_no.getText().toString())) {

            reference.child(numberplate).child("engine_no").setValue(engine_no.getText().toString());
            engine_no_ = engine_no.getText().toString();
            return true;

        } else {
            return false;
        }
    }

    private boolean isRegistration_date_changed() {
        if (!registration_date_.equals(registration_date.getText().toString())) {

            reference.child(numberplate).child("registration_date").setValue(registration_date.getText().toString());
            registration_date_ = registration_date.getText().toString();
            return true;

        } else {

            return false;

        }
    }

    private void readData(String number_plate) {

        reference = FirebaseDatabase.getInstance().getReference("database");
        reference.child((number_plate)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                        Toast.makeText(getApplicationContext(), "Car Details Found!", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        registration_date_ = String.valueOf(dataSnapshot.child("registration_date").getValue());
                        engine_no_ = String.valueOf(dataSnapshot.child("engine_no").getValue());
                        Owner_name_ = String.valueOf(dataSnapshot.child("owner_name").getValue());
                        Vehicle_class_ = String.valueOf(dataSnapshot.child("vehicle_class").getValue());
                        fuel_type_ = String.valueOf(dataSnapshot.child("fuel_type").getValue());
                        maker_model_ = String.valueOf(dataSnapshot.child("maker_model").getValue());
                        rc_status_ = String.valueOf(dataSnapshot.child("rc_status").getValue());
                        String number_plate = String.valueOf(dataSnapshot.child("numberplate").getValue());

                        registration_date.setText(registration_date_);
                        engine_no.setText(engine_no_);
                        owner_name.setText(Owner_name_);
                        vehicle_class.setText(Vehicle_class_);
                        fuel_type.setText(fuel_type_);
                        maker_model.setText(maker_model_);
                        rc_status.setText(rc_status_);


                    } else {
                        Toast.makeText(getApplicationContext(), "Car with this number doesn't exist!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}