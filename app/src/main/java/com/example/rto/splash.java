package com.example.rto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class splash extends AppCompatActivity {
    ImageView logo, splashImg;
    TextView appName;
    LottieAnimationView lottieAnimationView;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();


        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        splashImg = findViewById(R.id.bg);
        lottieAnimationView = findViewById(R.id.lottie);

        splashImg.animate().translationY(-1600).setDuration(4000).setStartDelay(5000);
        logo.animate().translationY(1400).setDuration(4000).setStartDelay(5000);
        appName.animate().translationY(1400).setDuration(4000).setStartDelay(5000);
        lottieAnimationView.animate().translationX(1600).setDuration(5000).setStartDelay(8000);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 6000);
    }

}