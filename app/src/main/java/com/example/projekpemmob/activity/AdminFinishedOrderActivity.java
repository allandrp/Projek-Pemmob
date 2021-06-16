package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.AdminOrderAdapter;
import com.example.projekpemmob.model.History;
import com.example.projekpemmob.viewHolder.AdminOrderHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminFinishedOrderActivity extends AppCompatActivity implements AdminOrderHolder.getRvListener, View.OnClickListener {

    RecyclerView rvHistory;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    Button btnBack;
    ArrayList<History> listHistory = new ArrayList<>();
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<String> historyIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_finished_order);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        rvHistory   = findViewById(R.id.rvHistory);
        btnBack     = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadData();
    }

    private void loadData() {
//        Query dataHistory = dbReference.child("history").child(fbAuth.getCurrentUser().getUid());
        Query checkUser = dbReference.child("history");
        ArrayList<String> userIds = new ArrayList<>();

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot innerSnapshot : snapshot.getChildren()) {
                    if (innerSnapshot.exists()) {
                        userIds.add(innerSnapshot.getKey());

                        Query checkHistory = dbReference.child("history").child(innerSnapshot.getKey());
                        checkHistory.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot snpsht : snapshot.getChildren()) {
                                        History temp = snpsht.getValue(History.class);

                                        if (temp.getStatus().equals("done")) {
                                            listHistory.add(temp);
                                            userList.add(innerSnapshot.getKey());
                                            historyIdList.add(snpsht.getKey());
                                        }
                                    }

                                    AdminOrderAdapter hAdapter = new AdminOrderAdapter(listHistory, AdminFinishedOrderActivity.this);
                                    rvHistory.setLayoutManager(new LinearLayoutManager(AdminFinishedOrderActivity.this));
                                    rvHistory.setAdapter(hAdapter);

                                } else {
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

//        Toast.makeText(this, String.valueOf(userIds.size()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getRvClick(int position) {

    }
}