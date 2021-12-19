package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import java.util.Objects;

public class loading_user_admin{

    private Activity activity;
    private AlertDialog dialog;

    loading_user_admin(Activity myActivity){
        activity=myActivity;
    }

    void startloading_user_admin(){

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading,null));
        builder.setCancelable(false);

        dialog=builder.create();
        dialog.show();

    }

    void dismissDialog(){
        dialog.dismiss();

    }


}