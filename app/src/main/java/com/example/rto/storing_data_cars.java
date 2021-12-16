package com.example.rto;

public class storing_data_cars {
    String number_plate,registration_date,engine_no,owner_name,vehicle_class,fuel_type,maker_model,rc_status;

    public storing_data_cars() {
    }

    public String getVehicle_class() {
        return vehicle_class;
    }

    public void setVehicle_class(String vehicle_class) {
        this.vehicle_class = vehicle_class;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getMaker_model() {
        return maker_model;
    }

    public void setMaker_model(String maker_model) {
        this.maker_model = maker_model;
    }

    public String getRc_status() {
        return rc_status;
    }

    public void setRc_status(String rc_status) {
        this.rc_status = rc_status;
    }

    public storing_data_cars(String number_plate, String registration_date, String engine_no, String owner_name, String vehicle_class, String fuel_type, String maker_model, String rc_status) {
        this.number_plate = number_plate;
        this.registration_date = registration_date;
        this.engine_no = engine_no;
        this.owner_name = owner_name;
        this.vehicle_class=vehicle_class;
        this.fuel_type=fuel_type;
        this.maker_model=maker_model;
        this.rc_status=rc_status;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no= engine_no;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}
