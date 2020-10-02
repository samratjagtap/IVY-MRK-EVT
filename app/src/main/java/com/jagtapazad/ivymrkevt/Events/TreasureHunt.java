package com.jagtapazad.ivymrkevt.Events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jagtapazad.ivymrkevt.Common.AgreementPay;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Common.LeaderBoard;
import com.jagtapazad.ivymrkevt.HelperClasses.LoadingDialog;
import com.jagtapazad.ivymrkevt.R;
import com.jagtapazad.ivymrkevt.User.LoginActivity;

public class TreasureHunt extends AppCompatActivity {

    ImageView back,checkGO;
    Button agreePay;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    Boolean paid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_hunt);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        checkGO = findViewById(R.id.checkGO);
        agreePay = findViewById(R.id.regPay);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();


        final LoadingDialog loadingDialog= new LoadingDialog(TreasureHunt.this);
        loadingDialog.startDialog();

        readData(new TreasureHunt.FirebaseCallback() {
            @Override
            public void onCallback(boolean basepaid) {
                if (basepaid) {
                    agreePay.setText("Enter Event!");
                    checkGO.setVisibility(View.VISIBLE);
                }
                loadingDialog.dismissDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });

        agreePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreePay.getText() == "Enter Event!") {
                    startActivity(new Intent(getApplicationContext(), TreasureHunt2.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), AgreementPay.class));
                }
            }
        });

    }


    private void readData(final TreasureHunt.FirebaseCallback firebaseCallback) {

        fStore.collection("users").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {

                        if (doc.getBoolean("TreasureHuntPayment") != null) {
                            paid = doc.getBoolean("TreasureHuntPayment");
                            firebaseCallback.onCallback(paid);
                        }else{
                            firebaseCallback.onCallback(false);
                        }

                    } else {
                        Toast.makeText(TreasureHunt.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TreasureHunt.this, "Error ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private interface FirebaseCallback {
        void onCallback(boolean basepaid);
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }

    public void leaderboard(View view) {
        startActivity(new Intent(getApplicationContext(), LeaderBoard.class));
    }
}