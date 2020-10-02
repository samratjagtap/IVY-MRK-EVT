package com.jagtapazad.ivymrkevt.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Home;
import com.jagtapazad.ivymrkevt.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    Button callSignIn, callRegister;
    TextInputLayout regName, regEmail, regPass, regPhone;
    ImageView logo;
    TextView txt;

    CountryCodePicker ccp;


    private Boolean validateName() {
        String val = regName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regName.setError("Field Cannot Be Empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field Cannot Be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid Email Address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = regPhone.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhone.setError("Field Cannot Be Empty");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPass.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
            regPass.setError("Field Cannot Be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPass.setError("Password is Too Weak");
            return false;
        } else {
            regPass.setError(null);
            regPass.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        callSignIn = findViewById(R.id.RegisterSignInButton);
        callRegister = findViewById(R.id.RegisterButton);
        logo = findViewById(R.id.regLogo);
        txt = findViewById(R.id.regTxt);
        regName = findViewById(R.id.RegisterFullName);
        regPass = findViewById(R.id.RegisterPassword);
        regEmail = findViewById(R.id.RegisterEmail);
        regPhone = findViewById(R.id.RegisterPhone);

        ccp=findViewById(R.id.countrycode);


//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), RegisterUser2.class));
//            finish();
//        }

        // Animation Pairs

        callSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callsignin = new Intent(RegisterUser.this, LoginActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(callSignIn, "regsign_box");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterUser.this, pairs);
                startActivity(callsignin, options.toBundle());
                finish();
            }
        });

        callRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateName() | !validateEmail() | !validatePassword() | !validatePhone()) {
                    return;
                }

                //Get all the Values

                String name = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String pass = regPass.getEditText().getText().toString();
                String phone = ccp.getFullNumberWithPlus() + "-" + regPhone.getEditText().getText().toString();

                Intent intent=new Intent(RegisterUser.this, RegisterUser2.class);

                intent.putExtra("FullName",name);
                intent.putExtra("Email",email);
                intent.putExtra("Pass",pass);
                intent.putExtra("Phone",phone);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
