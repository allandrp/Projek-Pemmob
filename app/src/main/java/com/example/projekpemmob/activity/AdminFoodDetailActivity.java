package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminFoodDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_NAME = "food_name";

    private Button btnBack, btnSaveFood;
    private ImageView imgFood;
    private EditText etFoodName, etFoodDescription, etFoodPrice;

    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    private FirebaseStorage fbStorage;

    private String tempNama;
    private String key = "";
    private int hargaMakanan, jumlahPesan;
    private String namaMakanan = "soto", descMakanan, userID, categoryIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_detail);

        btnBack = findViewById(R.id.btnBack);
        btnSaveFood = findViewById(R.id.btnSaveFood);
        imgFood = findViewById(R.id.imgFood);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodDescription = findViewById(R.id.etFoodDescription);
        etFoodPrice = findViewById(R.id.etFoodPrice);

        btnBack.setOnClickListener(this);
        btnSaveFood.setOnClickListener(this);

        fbDB = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        fbStorage = FirebaseStorage.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnSaveFood.getId()) {
            String foodName = etFoodName.getText().toString();
            String foodPrice = etFoodPrice.getText().toString();
            String foodDescription = etFoodDescription.getText().toString();

            Query updateData = fbDB.getReference("foods").orderByChild("name").equalTo(tempNama);
            updateData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().child("name").setValue(foodName);
                        ds.getRef().child("price").setValue(Integer.parseInt(foodPrice));
                        ds.getRef().child("description").setValue(foodDescription);
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            finish();
        }
    }

    private void loadData() {

        Intent intent = getIntent();
        tempNama = String.valueOf(intent.getStringExtra(EXTRA_NAME));
        Query loadData = fbDB.getReference("foods").orderByChild("name").equalTo(tempNama);
        loadData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("coba2", "onDataChange: "+snapshot.getValue());

                if(snapshot.exists()){
                    Food food = snapshot.getChildren().iterator().next().getValue(Food.class);
                    hargaMakanan = food.getPrice();
                    namaMakanan = food.getName();
                    descMakanan = food.getDescription();

                    if(food.getImagePath().equalsIgnoreCase("coba")){

                    }else{
                        StorageReference gsRef = fbStorage.getReferenceFromUrl(food.getImagePath());
                        gsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.e("IMAGE_URL", "uri: " + uri.toString());

                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .apply(new RequestOptions().override(720, 720))
                                    .into(imgFood);
                        });
                    }
                    etFoodName.setText(namaMakanan);
                    etFoodPrice.setText(hargaMakanan + "");
                    etFoodDescription.setText(descMakanan);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}