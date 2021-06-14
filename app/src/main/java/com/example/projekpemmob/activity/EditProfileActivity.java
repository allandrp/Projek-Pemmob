package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgProfile;
    Button btnBack;
    EditText txName, txPhone;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();

        imgProfile  = findViewById(R.id.id_imgViewPhotoEP);
        btnBack     = findViewById(R.id.btnBack);
        txName      = findViewById(R.id.id_txNamaEP);
        txPhone     = findViewById(R.id.id_txPhoneEP);

        imgProfile.setImageResource(R.drawable.ic_profile);

        btnBack.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        loadData();

    }

    private void loadData(){

        Query getUser = dbReference.child("user").child(fbAuth.getCurrentUser().getUid());

        getUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user   = snapshot.getValue(User.class);
                txName.setText(user.getName());
                txPhone.setText(user.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnBack.getId()){

            finish();

        }

        if(v.getId() ==  imgProfile.getId()){

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(imgProfile);

        }
    }
}