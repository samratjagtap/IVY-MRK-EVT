package com.jagtapazad.ivymrkevt.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.HelperClasses.LoadingDialog;
import com.jagtapazad.ivymrkevt.Home;
import com.jagtapazad.ivymrkevt.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser2 extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button callFinalSignIn, callFinalRegister;
    TextInputLayout regUsername;

    String userID;
    String gender;

    DatePickerDialog.OnDateSetListener date;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString().trim();
        String noWhiteSpace = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field Cannot Be Empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username Too Long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White Spaces Not Allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        callFinalSignIn = findViewById(R.id.RegisterSignInButton);
        callFinalRegister = findViewById(R.id.RegisterButton);
        regUsername = findViewById(R.id.RegisterUsername);
        radioGroup = findViewById(R.id.radioGender);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        gender="NA";

        final LoadingDialog loadingDialog= new LoadingDialog(RegisterUser2.this);

        callFinalSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


        callFinalRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateUsername()) {
                    return;
                }

                //Get all the Values

                final String username = regUsername.getEditText().getText().toString();
                final String name = getIntent().getStringExtra("FullName");
                final String email = getIntent().getStringExtra("Email");
                final String pass = getIntent().getStringExtra("Pass");
                final String phone = getIntent().getStringExtra("Phone");
                final long score=0;

                loadingDialog.startDialog();

                // User storing in Database | FireStore

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterUser2.this, "Verification Sent!", Toast.LENGTH_SHORT).show();
                                }
                            });


                            radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                            if(radioButton != null) {
                                gender = (String) radioButton.getText();
                            }

                            Toast.makeText(RegisterUser2.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("fname", name);
                            user.put("pass", pass);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("gender", gender);
                            user.put("score", score);
                            documentReference.set(user);

                            DocumentReference scoring=fStore.collection("LeaderBoard").document(userID);
                            Map<String, Object> leaderboard = new HashMap<>();
                            leaderboard.put("fname", name);
                            leaderboard.put("score", score);
                            scoring.set(leaderboard);

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();


                        } else {
                            Toast.makeText(RegisterUser2.this, "Error Occured", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
        finish();
    }

//TODO Complete the code wit phone no, radio, date spinner and username input

}
