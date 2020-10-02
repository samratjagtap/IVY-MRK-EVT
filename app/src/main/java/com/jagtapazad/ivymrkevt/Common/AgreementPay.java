package com.jagtapazad.ivymrkevt.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.jagtapazad.ivymrkevt.Events.TreasureHunt;
import com.jagtapazad.ivymrkevt.R;

import java.util.HashMap;
import java.util.Map;

public class AgreementPay extends AppCompatActivity {

    Button finpay;
    CheckBox check;

    ScrollView sv;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    public static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    Uri uri;
    String approvalRefNo;

    public static String payerName, UpiId, msgNote, sendAmount, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_pay);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        finpay = findViewById(R.id.finalPay);
        check = findViewById(R.id.checkbox);
        sv = findViewById(R.id.sv);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        payerName="Azad Jagtap";
        UpiId="jagtapazad@okicici";
        msgNote="IVY MRK | Treasure Hunt";
        sendAmount="99";

        finpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!check.isChecked()){
                    check.setError("Required");
                    sv.scrollTo(0,sv.getBottom());
                }else {
                    showSure();
                }
            }
        });
    }

    void showSure() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.logoutalert, null);

        Button btn_yes = view.findViewById(R.id.alertyes);
        Button btn_no = view.findViewById(R.id.alertcancle);
        TextView AlertText= view.findViewById(R.id.alertText);

        final AlertDialog logoutAlert = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        AlertText.setText("PROCEED PAYMENT?");
        logoutAlert.setCancelable(false);
        logoutAlert.show();


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri = getUpiPaymentUri(payerName, UpiId, msgNote, sendAmount);
                payWithGpay(GPAY_PACKAGE_NAME);

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


    private static Uri getUpiPaymentUri(String name, String upiId, String note, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGpay(String packageName) {

        if (isAppInstalled(this, packageName)) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent, 0);

        } else {
            Toast.makeText(AgreementPay.this, "Google Pay is NOT Installed.\nPlease Install and Try Again.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            approvalRefNo = data.getStringExtra("txnRef");
        }
        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(AgreementPay.this, "Transaction Successful.", Toast.LENGTH_SHORT).show();

            long clueno =0 ;
            long score=0;
            boolean treasurePay=true;
            String clue = "My crib receives first sunshine here, it has a bell but silence everywhere, come to me and find peace anywhere.";
            Toast.makeText(this, clue + clueno, Toast.LENGTH_LONG).show();

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            Map<String, Object> user = new HashMap<>();
            user.put("TreasureHuntclueNo", clueno);
            user.put("TreasureHuntclue", clue);
            user.put("score", score);
            user.put("TreasureHuntPayment", treasurePay);

            fStore.collection("users").document(userID)
                    .set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AgreementPay.this, "Welcome To IVY's Treasure Hunt!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AgreementPay.this, "Error!", Toast.LENGTH_LONG).show();
                }
            });


            startActivity(new Intent(getApplicationContext(), TreasureHunt.class));
            finish();

        } else {
            Toast.makeText(AgreementPay.this, "Transaction Failed.\nPlease Try Again.", Toast.LENGTH_SHORT).show();
        }

    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}