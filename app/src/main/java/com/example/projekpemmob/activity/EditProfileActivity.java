package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    FirebaseStorage fbStor;
    StorageReference stReference;
    ProgressBar pb;
    String tempUrl = "temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        fbStor      = FirebaseStorage.getInstance();
        stReference = fbStor.getReference("user");

        imgProfile      = findViewById(R.id.id_imgViewPhotoEP);
        btnBack         = findViewById(R.id.btnBack);
        txName          = findViewById(R.id.id_txNamaEP);
        txPhone         = findViewById(R.id.id_txPhoneEP);
        pb              = findViewById(R.id.progressBarFoto);
        pb.setVisibility(View.INVISIBLE);

        getImage();

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

                    tempUrl = snapshot.getValue().toString();
                    stReference = stReference.child(fbAuth.getCurrentUser().getUid()).child(snapshot.getValue().toString());

                    stReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.d("uri", "alamat : "+uri.toString());
                            Picasso.with(EditProfileActivity.this).load(uri).into(imgProfile);

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
            stReference = fbStor.getReference("user").child(fbAuth.getCurrentUser().getUid()).child(fbAuth.getCurrentUser().getUid()+"."+getExtension(imageUri));
            stReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    stReference = fbStor.getReference("user").child(fbAuth.getCurrentUser().getUid()).child(tempUrl);
                    stReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dbReference.child("profile image").child(fbAuth.getCurrentUser().getUid()).setValue(fbAuth.getCurrentUser().getUid()+"."+getExtension(imageUri));
                            Toast.makeText(EditProfileActivity.this, "UPLOAD SUCCESS", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("gagal upload", "onFailure: "+e.getStackTrace());

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    Log.d("proses upload", "onFailure: "+"Proses");

                    pb.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    private String getExtension(Uri uri){

        ContentResolver cR  = getContentResolver();
        MimeTypeMap mime    = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }
}