package com.jagtapazad.ivymrkevt.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.R;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        int SPLASH_TIME_OUT = 1500;

        final Animation aniZoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);


        //Hooks

        logo = findViewById(R.id.Logo);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashScreen.this, LoginActivity.class);

                logo.startAnimation(aniZoom);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(logo, "logo_img");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                startActivity(loginIntent, options.toBundle());
                finish();


            }
        }, SPLASH_TIME_OUT);
    }
}