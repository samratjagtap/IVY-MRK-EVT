package com.jagtapazad.ivymrkevt.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jagtapazad.ivymrkevt.HelperClasses.LeaderClass;
import com.jagtapazad.ivymrkevt.HelperClasses.NoticeAdapter;
import com.jagtapazad.ivymrkevt.HelperClasses.NoticeClass;
import com.jagtapazad.ivymrkevt.HelperClasses.PostAdapter;
import com.jagtapazad.ivymrkevt.R;

public class Notice extends AppCompatActivity {

    ImageView back;

    RecyclerView NoticeList;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        NoticeList = findViewById(R.id.noticelist);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });


        NoticeList.setLayoutManager(new LinearLayoutManager(this));
        NoticeList.setHasFixedSize(true);

        Query noticequery = fStore.collection("Notice")
                .orderBy("number", Query.Direction.DESCENDING)
                .limit(10);

        FirestoreRecyclerOptions<NoticeClass> options = new FirestoreRecyclerOptions.Builder<NoticeClass>()
                .setQuery(noticequery, NoticeClass.class)
                .build();

        adapter = new NoticeAdapter(options);

        NoticeList.setAdapter(adapter);

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}