package com.jagtapazad.ivymrkevt.Common;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt2;
import com.jagtapazad.ivymrkevt.Game.GameMafia;
import com.jagtapazad.ivymrkevt.HelperClasses.LoadingDialog;
import com.jagtapazad.ivymrkevt.R;
import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.User.RegisterUser2;

import java.util.HashMap;
import java.util.Map;

public class HostEvent extends AppCompatActivity {

    ImageView back;
    Button enquiry;
    EditText eventType, headCount, venue, date, email, details;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;


    private Boolean validateType() {
        String val = eventType.getText().toString();

        if (val.isEmpty()) {
            eventType.setError("Required");
            return false;
        } else {
            eventType.setError(null);
            return true;
        }
    }

    private Boolean validateHeadCount() {
        String val = headCount.getText().toString();

        if (val.isEmpty()) {
            headCount.setError("Required");
            return false;
        } else {
            headCount.setError(null);
            return true;
        }
    }

    private Boolean validateVenue() {
        String val = venue.getText().toString();

        if (val.isEmpty()) {
            venue.setError("Required");
            return false;
        } else {
            venue.setError(null);
            return true;
        }
    }

    private Boolean validateDate() {
        String val = date.getText().toString();

        if (val.isEmpty()) {
            date.setError("Required");
            return false;
        } else {
            date.setError(null);
            return true;
        }
    }

    private Boolean validateMessage() {
        String val = details.getText().toString();

        if (val.isEmpty()) {
            details.setError("Required");
            return false;
        } else {
            details.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = email.getText().toString();

        if (val.isEmpty()) {
            email.setError("Required");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_event);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        eventType = findViewById(R.id.eventtype);
        headCount = findViewById(R.id.headcount);
        venue = findViewById(R.id.hostvenue);
        date = findViewById(R.id.hostdate);
        email = findViewById(R.id.hostemail);
        details = findViewById(R.id.detailmessage);

        enquiry = findViewById(R.id.sendquery);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        final LoadingDialog loadingDialog = new LoadingDialog(HostEvent.this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });

        enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateMessage() | !validateEmail() | !validateHeadCount() | !validateType() | !validateVenue() | !validateDate()) {
                    return;
                }

                showSure();
                //Get all the Values

            }
        });


    }

    void showSure() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.logoutalert, null);

        Button btn_yes = view.findViewById(R.id.alertyes);
        Button btn_no = view.findViewById(R.id.alertcancle);
        TextView AlertText = view.findViewById(R.id.alertText);

        final AlertDialog logoutAlert = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        AlertText.setText("SEND ENQUIRY?");
        logoutAlert.setCancelable(false);
        logoutAlert.show();


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String HostEmail = email.getText().toString();
                final String HostType = eventType.getText().toString();
                final String HostMessage = details.getText().toString();
                final String HostHeadCount = headCount.getText().toString();
                final String HostVenue = venue.getText().toString();
                final String HostDate = date.getText().toString();


                DocumentReference documentReference = fStore.collection("HostEvent").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("Email", HostEmail);
                user.put("Event Type", HostType);
                user.put("Date", HostDate);
                user.put("Crowd", HostHeadCount);
                user.put("Venue", HostVenue);
                user.put("Details", HostMessage);
                documentReference.set(user);

                venue.setText("");
                date.setText("");
                headCount.setText("");
                details.setText("");
                eventType.setText("");
                email.setText("");


                Toast.makeText(HostEvent.this, "Enquiry Send Successfully.\nWe will Contact You Soon.", Toast.LENGTH_LONG).show();

                logoutAlert.dismiss();

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAlert.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}