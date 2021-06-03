package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_profile extends AppCompatActivity implements View.OnClickListener {

    TextView tvNama, tvemail, tvPoints;
    Button btnSetting, btnEditProfile, btnCoupons, btnHistory, btnLogout;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();

        tvNama      = findViewById(R.id.tvProfileNama);
        tvemail     = findViewById(R.id.tvProfileEmail);
        tvPoints    = findViewById(R.id.tvProfilepoints);

        btnSetting      = findViewById(R.id.btnSetting);
        btnEditProfile  = findViewById(R.id.btnEditProfile);
        btnHistory      = findViewById(R.id.btnProfileHistory);
        btnCoupons      = findViewById(R.id.btnProfileCoupons);
        btnLogout       = findViewById(R.id.btnProfileLogout);

        btnSetting.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnCoupons.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        loadProfile();


    }

    private void loadProfile() {

        Query getProfile = dbReference.child("user").child(fbAuth.getCurrentUser().getUid());

        getProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user User = snapshot.getValue(user.class);

                tvNama.setText(User.getNama());
                tvemail.setText(User.getEmail());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnCoupons.getId()){


        }else if(v.getId() == btnEditProfile.getId()){


        }else if (v.getId() == btnHistory.getId()){


        }else if (v.getId() == btnSetting.getId()){


        }else if(v.getId() == btnLogout.getId()){

            fbAuth.signOut();
            Intent intent = new Intent(this, activity_login.class);
            startActivity(intent);
            finish();

        }

    }
}