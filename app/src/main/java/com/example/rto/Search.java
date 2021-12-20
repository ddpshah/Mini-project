package com.example.rto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Search extends AppCompatActivity {
    ImageView backbutton, car;
    String numberplate, registration_date_, engine_no_, Owner_name_, fuel_type_, Vehicle_class_, maker_model_, rc_status_;
    boolean alcazar, breeza, creta, dzire, harrier, hector, i20, nexon_ev, scorpio, seltos, swift, thar, xuv700;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);
        car = findViewById(R.id.default_car);
        backbutton = findViewById(R.id.imageView);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();

        int page = intent.getExtras().getInt("flag");
        if (page == 1) {
            String numberplate_ = intent.getStringExtra("number_plate_search");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(numberplate_);
            readData(numberplate_);
        } else if (page == 0) {
            String s = intent.getStringExtra("number_plate_search_user");
            TextView tv;
            tv = findViewById(R.id.tv_numberplateview);
            tv.setText(s);
            readData(s);
        }
    }

    private void car_image(String maker_model_) {

        if (maker_model_.equals("Tata Harrier")) {
            car.setImageResource(R.drawable.harrier);
        } else if (maker_model_.equals("MG Hector")) {
            car.setImageResource(R.drawable.hector);
        } else if (maker_model_.equals("Suzuki Dzire")) {
            car.setImageResource(R.drawable.dzire);
        } else if (maker_model_.equals("Mahindra XUV")) {
            car.setImageResource(R.drawable.xuv700);
        } else if (maker_model_.equals("Hyundai Alcazar")) {
            car.setImageResource(R.drawable.alcazar);
        } else if (maker_model_.equals("Vitara Brezza")) {
            car.setImageResource(R.drawable.brezza);
        } else if (maker_model_.equals("Hyundai Creta")) {
            car.setImageResource(R.drawable.creta);
        } else if (maker_model_.equals("Hyundai i20 N-Line")) {
            car.setImageResource(R.drawable.i20);
        } else if (maker_model_.equals("Tata Nexon EV")) {
            car.setImageResource(R.drawable.nexon_ev);
        } else if (maker_model_.equals("Mahindra Scorpio")) {
            car.setImageResource(R.drawable.scorpio);
        } else if (maker_model_.equals("Kia Seltos")) {
            car.setImageResource(R.drawable.seltos);
        } else if (maker_model_.equals("Suzuki Swift")) {
            car.setImageResource(R.drawable.swift);
        } else if (maker_model_.equals("Mahindra Thar")) {
            car.setImageResource(R.drawable.thar);
        }
        else if (maker_model_.equals("Suzuki Alto")) {
            car.setImageResource(R.drawable.alto);
        }
        else if (maker_model_.equals("MG Astor")) {
            car.setImageResource(R.drawable.astor);
        }
        else if (maker_model_.equals("Suzuki Baleno")) {
            car.setImageResource(R.drawable.baleno);
        }
        else if (maker_model_.equals("Kia Carnival")) {
            car.setImageResource(R.drawable.carnival);
        }
        else if (maker_model_.equals("Suzuki Celerio")) {
            car.setImageResource(R.drawable.celerio);
        }
        else if (maker_model_.equals("Suzuki Ciaz")) {
            car.setImageResource(R.drawable.ciaz);
        }
        else if (maker_model_.equals("Renault Duster")) {
            car.setImageResource(R.drawable.duster);
        }
        else if (maker_model_.equals("Suzuki Ertiga")) {
            car.setImageResource(R.drawable.ertiga);
        }
        else if (maker_model_.equals("Renault Kiger")) {
            car.setImageResource(R.drawable.kiger);
        }
        else if (maker_model_.equals("Renault Kwid")) {
            car.setImageResource(R.drawable.kwid);
        }
        else if (maker_model_.equals("Volkswagen Polo")) {
            car.setImageResource(R.drawable.polo);
        }
        else if (maker_model_.equals("Tata Punch")) {
            car.setImageResource(R.drawable.punch);
        }
        else if (maker_model_.equals("Tata Safari")) {
            car.setImageResource(R.drawable.safari);
        }
        else if (maker_model_.equals("Suzuki S-Cross")) {
            car.setImageResource(R.drawable.scross);
        }
        else if (maker_model_.equals("Kia Sonet")) {
            car.setImageResource(R.drawable.sonet);
        }
        else if (maker_model_.equals("Tata Tiago")) {
            car.setImageResource(R.drawable.tiago);
        }
        else if (maker_model_.equals("Tata Tigor")) {
            car.setImageResource(R.drawable.tigor);
        }
        else if (maker_model_.equals("Volkswagen Vento")) {
            car.setImageResource(R.drawable.vento);
        }
        else if (maker_model_.equals("Suzuki Wagon-R")) {
            car.setImageResource(R.drawable.wagonr);
        }
        else if (maker_model_.equals("xuv300")) {
            car.setImageResource(R.drawable.xuv300);
        }else {
            car.setImageResource(R.drawable.myimage);
        }
    }

    private void readData(String number_plate) {

        reference = FirebaseDatabase.getInstance().getReference("database");
        reference.child(number_plate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

                        car_image(maker_model_);

                        TextView registration_date, engine_no, owner_name, vehicle_class, fuel_type, maker_model, rc_status;
                        registration_date = findViewById(R.id.tv_registration_number);
                        engine_no = findViewById(R.id.tv_engine_no);
                        owner_name = findViewById(R.id.tv_owner_name);
                        vehicle_class = findViewById(R.id.tv_vehicle_class);
                        fuel_type = findViewById(R.id.tv_fuel_type);
                        maker_model = findViewById(R.id.tv_model);
                        rc_status = findViewById(R.id.tv_rc_status);

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
                    Toast.makeText(getApplicationContext(), "Failed to retrieve Data!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}