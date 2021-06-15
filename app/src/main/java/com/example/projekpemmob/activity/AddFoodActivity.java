package com.example.projekpemmob.activity;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
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


public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    EditText etFoodName, etFoodDesc, etFoodPrice;
    TextView tvTitle;
    RatingBar rbFood;
    String nameIntent, categoryIntent;
    ImageView imgView;
    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    FirebaseStorage fbStor;
    StorageReference stReference;
    ProgressBar pbFoto;
    int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    String tempUrl = "temp";
    String tempPath = "";

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        fbStor      = FirebaseStorage.getInstance();
        stReference = fbStor.getReference();

        btnSave     = findViewById(R.id.id_btSaveAD);
        etFoodName  = findViewById(R.id.id_txNamaAD);
        etFoodDesc  = findViewById(R.id.id_txDescAD);
        etFoodPrice = findViewById(R.id.id_txHargaAD);
        tvTitle     = findViewById(R.id.textViewTitleAD);
        rbFood      = findViewById(R.id.ratingBar);
        imgView     = findViewById(R.id.id_imgViewPhotoAD);
        pbFoto      = findViewById(R.id.progressBarFoto);

        btnSave.setOnClickListener(this);
        imgView.setClickable(true);
        imgView.setOnClickListener(this);

        pbFoto.setVisibility(View.VISIBLE);

        loadData();
        getImage();

    }

    private void loadData(){

        Intent intent   = getIntent();

        if(intent.getStringExtra("aksi").equalsIgnoreCase("update")){

            nameIntent      = intent.getStringExtra("name");
            categoryIntent  = intent.getStringExtra("category");

            tvTitle.setText("UPDATE "+ categoryIntent.toUpperCase());
            btnSave.setText("SAVE "+ categoryIntent.toUpperCase());

            Query getItem = dbReference.child("foods").child(nameIntent);

            getItem.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        Food food = snapshot.getValue(Food.class);
                        etFoodName.setText(food.getName());
                        etFoodPrice.setText(String.valueOf(food.getPrice()));
                        etFoodDesc.setText(food.getDescription());
                        rbFood.setRating((float)food.getRating());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }

    private void getImage() {
        Query qImage = dbReference.child("foods").child(nameIntent).child("imagePath");
        qImage.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    tempUrl = snapshot.getValue().toString();
                    stReference = fbStor.getReference().child("foods").child(nameIntent).child(snapshot.getValue().toString());
                    stReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.d("uri", "alamat : "+uri.toString());
                            Picasso.with(AddFoodActivity.this).load(uri).into(imgView);
                            pbFoto.setVisibility(View.GONE);

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            imgView.setImageResource(R.drawable.ic_profile);
                            pbFoto.setVisibility(View.GONE);
                        }
                    });



                }else{

                    imgView.setImageResource(R.drawable.ic_profile);
                    pbFoto.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == imgView.getId()){

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        }

    }

    private String getExtension(Uri uri){

        ContentResolver cR  = getContentResolver();
        MimeTypeMap mime    = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imgView);
            stReference = fbStor.getReference("foods").child(nameIntent).child(nameIntent+"."+getExtension(imageUri));
            stReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    stReference = fbStor.getReference("foods").child(nameIntent).child(tempUrl);
                    stReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dbReference.child("foods").child(nameIntent).child("imagePath").setValue(nameIntent+"."+getExtension(imageUri));
                            Toast.makeText(AddFoodActivity.this, "UPLOAD SUCCESS", Toast.LENGTH_SHORT).show();
                            pbFoto.setVisibility(View.GONE);
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

                    pbFoto.setVisibility(View.VISIBLE);

                }
            });
        }
    }
}
