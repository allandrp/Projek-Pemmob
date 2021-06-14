package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.User;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvNama, tvemail, tvPoints;
    Button btnSetting, btnEditProfile, btnCoupons, btnHistory, btnLogout, btnBack;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    ImageView imgProfile;

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
        imgProfile  = findViewById(R.id.I_Profile);

        btnEditProfile  = findViewById(R.id.btnEditProfile);
        btnHistory      = findViewById(R.id.btnProfileHistory);
        btnCoupons      = findViewById(R.id.btnProfileCoupons);
        btnLogout       = findViewById(R.id.btnProfileLogout);
        btnBack         = findViewById(R.id.btnProfileBack);

        btnEditProfile.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnCoupons.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        loadProfile();


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
                            Picasso.with(ProfileActivity.this).load(uri).into(imgProfile);

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

    private void loadProfile() {

        getImage();

        Query getProfile = dbReference.child("user").child(fbAuth.getCurrentUser().getUid());

        getProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User User = snapshot.getValue(com.example.projekpemmob.model.User.class);

                tvNama.setText(User.getName());
                tvemail.setText(User.getEmail());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnLogout.getId()){

            fbAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else if(v.getId() == btnEditProfile.getId()){

            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);

        }else if (v.getId() == btnHistory.getId()){

            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);

        }else if (v.getId() == btnBack.getId()){

            finish();

        }else if(v.getId() == btnCoupons.getId()){


        }else if (v.getId() == btnSetting.getId()){

        }

    }
}