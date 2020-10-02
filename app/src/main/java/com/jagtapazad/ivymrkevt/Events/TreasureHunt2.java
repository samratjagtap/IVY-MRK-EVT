package com.jagtapazad.ivymrkevt.Events;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jagtapazad.ivymrkevt.Common.AgreementPay;
import com.jagtapazad.ivymrkevt.Common.Dashboard;
import com.jagtapazad.ivymrkevt.Common.LeaderBoard;
import com.jagtapazad.ivymrkevt.HelperClasses.LoadingDialog;
import com.jagtapazad.ivymrkevt.R;
import com.jagtapazad.ivymrkevt.User.LoginActivity;
import com.jagtapazad.ivymrkevt.User.Payment;
import com.jagtapazad.ivymrkevt.User.RegisterUser2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TreasureHunt2 extends AppCompatActivity {

    private TextView textView, textWarn, scoreboard, textCorrect;

    ImageView imgviewurl;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_hunt_2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);


        textView = findViewById(R.id.textView);
        textWarn = findViewById(R.id.textWarning);
        textCorrect = findViewById(R.id.textCorrect);
        scoreboard = findViewById(R.id.scoreBoard);
        imgviewurl = findViewById(R.id.imgView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();


        readData(new FirebaseCallback() {
            @Override
            public void onCallback(long BaseClueNO) {

            }
        });


    }

    private void readData(final FirebaseCallback firebaseCallback) {

        fStore.collection("users").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {

//                        int baseClueNo=Integer.parseInt(Objects.requireNonNull(doc.getString("TreasureHuntclueNo")));

                        String baseClue = doc.getString("TreasureHuntclue");
                        String baseImgUrl = doc.getString("TreasureImgURL");
                        long baseClueNo = doc.getLong("TreasureHuntclueNo");
                        textView.setText(baseClue);

                        if (baseImgUrl != "") {
                            new DownloadImageTask((ImageView) imgviewurl)
                                    .execute(baseImgUrl);
                        }

                        scoreboard.setText("Score: " + baseClueNo * 10);

                        firebaseCallback.onCallback(baseClueNo);

                    } else {
                        Toast.makeText(TreasureHunt2.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TreasureHunt2.this, "Error ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private interface FirebaseCallback {
        void onCallback(long BaseClueNO);
    }


    public void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan To Get The Next Clue!");
        intentIntegrator.initiateScan();
    }

    public void back(View view) {

        startActivity(new Intent(getApplicationContext(), TreasureHunt.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

            } else {

                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(intentResult.getContents());
                    //setting values to textviews
                    //{"clueNum":"1","clue":"erdf a v u y d b u a js n d n ","img":" "}

                    String a = obj.getString("clueNum");
                    final long clueno = Long.parseLong(a);

                    final String clue = obj.getString("clue");
                    final String imgurl = obj.getString("img");


                    readData(new FirebaseCallback() {
                        @Override
                        public void onCallback(long BaseClueNO) {
                            if (BaseClueNO + 1 == clueno) {

                                textView.setText(clue);

                                if (imgurl != "") {
                                    new DownloadImageTask((ImageView) imgviewurl)
                                            .execute(imgurl);
                                }

                                correct();

                                scoreboard.setText("Score: " + clueno * 10);

                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("TreasureHuntclueNo", clueno);
                                user.put("TreasureHuntclue", clue);
                                user.put("TreasureImgURL", imgurl);

                                DocumentReference scoringRef = fStore.collection("LeaderBoard").document(userID);

                                fStore.collection("users").document(userID)
                                        .set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TreasureHunt2.this, "Data Error", Toast.LENGTH_LONG).show();
                                    }
                                });

                                documentReference.update("score", FieldValue.increment(10));
                                scoringRef.update("score", FieldValue.increment(10));

                            } else {
                                String textClue = "INCORRECT CLUE!";
                                warning(textClue);
                            }

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    String text = "NOT AN IVY's CLUE!";
                    warning(text);

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void correct() {
        textCorrect.setVisibility(View.VISIBLE);
//        textCorrect.setText("CORRECT CLUE!");

        textCorrect.postDelayed(new Runnable() {
            @Override
            public void run() {
                textCorrect.setVisibility(View.INVISIBLE);
            }
        }, 5000);
        textCorrect.setVisibility(View.VISIBLE);
    }

    private void warning(String text) {
        textWarn.setVisibility(View.VISIBLE);
        textWarn.setText(text);
        textWarn.postDelayed(new Runnable() {
            @Override
            public void run() {
                textWarn.setVisibility(View.INVISIBLE);
            }
        }, 5000);
        textWarn.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TreasureHunt.class));
        finish();
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void leaderboard(View view) {

        startActivity(new Intent(getApplicationContext(), LeaderBoard.class));
    }

}