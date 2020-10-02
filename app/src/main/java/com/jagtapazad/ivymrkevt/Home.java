package com.jagtapazad.ivymrkevt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt2;
import com.jagtapazad.ivymrkevt.Game.GameMafia;
import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.User.Payment;
import com.jagtapazad.ivymrkevt.User.RegisterUser2;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = findViewById(R.id.textView);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
    }

    public void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan To Get The Next Clue!");
        intentIntegrator.initiateScan();
    }

    public void LogoutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void newAct(View view) {
        startActivity(new Intent(getApplicationContext(), TreasureHunt2.class));
    }

    public void newAct2(View view) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    public void newAct3(View view) {
        startActivity(new Intent(getApplicationContext(), GameMafia.class));
        finish();
    }

    public void newAct4(View view) {
        startActivity(new Intent(getApplicationContext(), Payment.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                textView.setText("Cancelled");
            } else {

                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(intentResult.getContents());
                    //setting values to textviews
                    String a = obj.getString("name");
                    String b = obj.getString("address");
                    textView.setText(obj.getString("name"));
                    Toast.makeText(this, b, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, "Not A Clue!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}