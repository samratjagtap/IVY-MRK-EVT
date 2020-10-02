package com.jagtapazad.ivymrkevt.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt;
import com.jagtapazad.ivymrkevt.Game.GameMafia;
import com.jagtapazad.ivymrkevt.Game.GamePoker;
import com.jagtapazad.ivymrkevt.Game.GamePsych;
import com.jagtapazad.ivymrkevt.Home;
import com.jagtapazad.ivymrkevt.R;
import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.User.Profile;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView menuicon;
    LinearLayout contentView, gameMafia, gamePsych, gamePoker;
    LinearLayout eventTreasure, comingsoon;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //Variable
    static final float END_SCALE = 0.7f;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        //Hooks
        menuicon = findViewById(R.id.menu_icon_home);
        contentView = findViewById(R.id.content);
        gameMafia = findViewById(R.id.mafia);
        gamePsych = findViewById(R.id.psych);
        gamePoker = findViewById(R.id.poker);
        eventTreasure = findViewById(R.id.treasurehunt);
        comingsoon = findViewById(R.id.comingsoon);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.navigation_main);

        //Hide Show Items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);

        navigationDrawer();

        //Notification
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });


        gameMafia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GameMafia.class));
                finish();
            }
        });

        gamePsych.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GamePsych.class));
                finish();
            }
        });
        gamePoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GamePoker.class));
                finish();
            }
        });

        eventTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TreasureHunt.class));
            }
        });

        comingsoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ComingSoon.class));
            }
        });

    }

    //Navigation Drawer

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setScrimColor(getResources().getColor(R.color.navblue));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                break;
            case R.id.nav_aboutus:
                Intent intent2 = new Intent(Dashboard.this, AboutUs.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_eventsnow:
                startActivity(new Intent(getApplicationContext(), EventNow.class));
                finish();
                break;
            case R.id.nav_logout: {
                showLogout();
                break;
            }
            case R.id.nav_notice: {
                startActivity(new Intent(getApplicationContext(), Notice.class));
                finish();
                break;
            }
            case R.id.nav_profile: {
                startActivity(new Intent(getApplicationContext(), Profile.class));
                finish();
                break;
            }
            case R.id.nav_hostevent:
                Intent intent1 = new Intent(Dashboard.this, HostEvent.class);
                startActivity(intent1);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Dashboard.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Logout Alert Box
    void showLogout() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.logoutalert, null);

        Button btn_yes = view.findViewById(R.id.alertyes);
        Button btn_no = view.findViewById(R.id.alertcancle);

        final AlertDialog logoutAlert = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        logoutAlert.setCancelable(false);
        logoutAlert.show();


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
                logoutAlert.dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAlert.dismiss();
                navigationView.setCheckedItem(R.id.nav_home);
            }
        });


    }

    public void leaderboard(View view) {

        startActivity(new Intent(getApplicationContext(), LeaderBoard.class));
    }

}