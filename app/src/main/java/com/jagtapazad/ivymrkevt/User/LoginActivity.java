package com.jagtapazad.ivymrkevt.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.HelperClasses.LoadingDialog;
import com.jagtapazad.ivymrkevt.HelperClasses.emailalert;
import com.jagtapazad.ivymrkevt.Home;
import com.jagtapazad.ivymrkevt.R;

public class LoginActivity extends AppCompatActivity {

    Button callSignIn, callSignUp,forgetPwd;
    TextInputLayout logEmail, logPass;
    ImageView logo;
    TextView LoginTXT;

    LottieAnimationView SplashLogo;

    FirebaseAuth fAuth;

//    public void didTapButton(View view) {
//        callSignIn = findViewById(R.id.SignInbutton);
//        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.buttonclick);
//        callSignIn.startAnimation(myAnim);
//    }

    private Boolean validateEmail() {
        String val = logEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            logEmail.setError("Field Cannot Be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            logEmail.setError("Invalid Email Address");
            return false;
        } else {
            logEmail.setError(null);
            logEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = logPass.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
            logPass.setError("Field Cannot Be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            logPass.setError("Password Do Not Match");
            return false;
        } else {
            logPass.setError(null);
            logPass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean forgetEmail(String email) {

        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            return false;
        } else if (!email.matches(emailPattern)) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        callSignUp = findViewById(R.id.SignUpButton);
        callSignIn = findViewById(R.id.SignInbutton);
        forgetPwd = findViewById(R.id.ForgetPassword);
        logo = findViewById(R.id.LoginLogo);
        logPass = findViewById(R.id.Password);
        logEmail = findViewById(R.id.Email);
        LoginTXT = findViewById(R.id.LoginTXT);
        SplashLogo= findViewById(R.id.SplashLogo);

        fAuth = FirebaseAuth.getInstance();

        final LoadingDialog loadingDialog= new LoadingDialog(LoginActivity.this);
        final emailalert emailalert= new emailalert(LoginActivity.this);

        if (fAuth.getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSure();

            }
        });

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startDialog();
                Intent callregister = new Intent(LoginActivity.this, RegisterUser.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(callSignIn, "regsign_box");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(callregister, options.toBundle());
                finish();
            }
        });

        callSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                loadingDialog.startDialog();

                final String email = logEmail.getEditText().getText().toString();
                final String pass = logPass.getEditText().getText().toString();


                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if (fAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent callhome = new Intent(LoginActivity.this, Dashboard.class);
                                startActivity(callhome);
                                finish();
                            } else {
                                loadingDialog.dismissDialog();
                                emailalert.startDialog();
                                Toast.makeText(LoginActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

    void showSure() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.forgetpasword, null);

        Button btn_yes = view.findViewById(R.id.alertyes);
        final Button btn_no = view.findViewById(R.id.alertcancle);
        final TextView AlertText= view.findViewById(R.id.alertText);
        final EditText forgetEmail= view.findViewById(R.id.forgetEmail);

        final AlertDialog logoutAlert = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        logoutAlert.setCancelable(false);
        logoutAlert.show();

//        Toast.makeText(LoginActivity.this, ""+ frgtemail, Toast.LENGTH_SHORT).show();



        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String frgtemail = forgetEmail.getText().toString();

                if(forgetEmail(frgtemail)) {
                    fAuth.sendPasswordResetEmail(frgtemail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Please Check Your Email.\nPassword Query Sent Successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                    logoutAlert.dismiss();
                }
                else{
                    forgetEmail.setError("Invalid Entry");
                }


            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAlert.dismiss();
            }
        });


    }

}