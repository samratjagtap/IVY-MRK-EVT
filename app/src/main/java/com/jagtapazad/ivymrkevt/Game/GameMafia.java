package com.jagtapazad.ivymrkevt.Game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt2;
import com.jagtapazad.ivymrkevt.R;
import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.User.RegisterUser2;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class GameMafia extends AppCompatActivity {

    ImageView back;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mafia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}

