package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.HistoryAdapter;
import com.example.projekpemmob.model.History;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    ImageView imgProfile;
    RecyclerView rvHistory;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    ArrayList<History> listHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();

        btnBack     = findViewById(R.id.id_btnBack);
        imgProfile  = findViewById(R.id.img_Profile_History);
        rvHistory   = findViewById(R.id.rvHistory);

        btnBack.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        loadData();
    }

    private void getImage() {
        Query qImage = dbReference.child("profile image").child(fbAuth.getCurrentUser().getUid());
        qImage.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    FirebaseStorage fbStorage       = FirebaseStorage.getInstance();
                    StorageReference stReference    = fbStorage.getReference("user");
                    stReference = stReference.child(fbAuth.getCurrentUser().getUid()).child(snapshot.getValue().toString());

                    stReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.d("uri", "alamat : "+uri.toString());
                            Picasso.with(HistoryActivity.this).load(uri).into(imgProfile);

                        }
                    });



                }else{

                    imgProfile.setImageResource(R.drawable.ic_profile);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadData() {

        getImage();

        Query dataHistory = dbReference.child("history").child(fbAuth.getCurrentUser().getUid());

        dataHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for (DataSnapshot snpsht : snapshot.getChildren()) {

                        Log.d("cobasnapsot history", "onDataChange: "+snpsht.getValue().toString());
                        History temp = snpsht.getValue(History.class);
                        listHistory.add(temp);
                        Log.d("cobaHistory", "onDataChange: "+temp.getTotalPrice());

                    }

                    HistoryAdapter hAdapter = new HistoryAdapter(listHistory);
                    rvHistory.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    rvHistory.setAdapter(hAdapter);

                }else{

                    Toast.makeText(HistoryActivity.this, "DATA NOT FOUND", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == imgProfile.getId()){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }else if (v.getId() == btnBack.getId()){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }
}