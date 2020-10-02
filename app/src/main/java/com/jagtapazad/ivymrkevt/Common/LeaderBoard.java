package com.jagtapazad.ivymrkevt.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jagtapazad.ivymrkevt.HelperClasses.LeaderClass;
import com.jagtapazad.ivymrkevt.HelperClasses.PostAdapter;
import com.jagtapazad.ivymrkevt.R;

public class LeaderBoard extends AppCompatActivity {

    TextView name, userscore;
    TextView fbusername, fbuserscore;

    RecyclerView RankingList;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.userName);
        userscore = findViewById(R.id.userScore);
        fbuserscore = findViewById(R.id.fbuserscore);
        fbusername = findViewById(R.id.fbusername);

        RankingList = findViewById(R.id.rankingList);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();


        readData(new FirebaseCallback() {
            @Override
            public void onCallback(long score, String fname) {
                name.setText(fname);
                userscore.setText(Long.toString(score));
            }
        });

        RankingList.setLayoutManager(new LinearLayoutManager(this));
        RankingList.setHasFixedSize(true);

        //Query
        Query query = fStore.collection("LeaderBoard")
                .orderBy("score", Query.Direction.DESCENDING)
                .whereGreaterThan("score",0)
                .limit(27);

        //Recycler View Options
        FirestoreRecyclerOptions<LeaderClass> options = new FirestoreRecyclerOptions.Builder<LeaderClass>()
                .setQuery(query, LeaderClass.class)
                .build();

        adapter = new PostAdapter(options);

        RankingList.setAdapter(adapter);

//        fStore.collection("LeaderBoard")
//                .orderBy("score", Query.Direction.DESCENDING)
//                .limit(10)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Toast.makeText(LeaderBoard.this, document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();
//                                String fbusername = document.getString("fname");
//                                long fbuserscore = document.getLong("score");
//
//                                Toast.makeText(LeaderBoard.this, fbusername + fbuserscore, Toast.LENGTH_LONG).show();
//
//
//                            }
//                        } else {
//                            Toast.makeText(LeaderBoard.this, "Error:" + task.getException(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void readData(final FirebaseCallback firebaseCallback) {

        fStore.collection("users").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {


                        String username = doc.getString("fname");
                        long score = doc.getLong("score");

                        firebaseCallback.onCallback(score, username);

                    } else {
                        Toast.makeText(LeaderBoard.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LeaderBoard.this, "Error ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private interface FirebaseCallback {
        void onCallback(long BaseClueNO, String username);
    }


    public void back(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}