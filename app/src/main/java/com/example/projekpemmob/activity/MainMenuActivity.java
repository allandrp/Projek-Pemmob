package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.MiniFoodAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.viewHolder.MiniFoodHolder;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvSeeAllFood, tvSeeAllDrink, tvClock, tvProfileName;
    private CardView btnAllCategories, btnFoodCategory, btnDrinkCategory, btnRecommendedCategory;
    private ImageButton btnProfileHistory;
    private RecyclerView rvFood;
    private RecyclerView rvDrink;
    private ArrayList<Food> daftarFood;
    private ImageView imgProfile;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = fbDB.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth              = FirebaseAuth.getInstance();
        rvFood              = findViewById(R.id.rvFood);
        rvDrink             = findViewById(R.id.rvDrink);
        tvSeeAllFood        = findViewById(R.id.tvSeeAllFood);
        tvSeeAllDrink       = findViewById(R.id.tvSeeAllDrink);
        btnAllCategories    = findViewById(R.id.btnAllFood);
        btnFoodCategory    = findViewById(R.id.btnCategoryFood);
        btnDrinkCategory    = findViewById(R.id.btnCategoryDrink);
        btnRecommendedCategory    = findViewById(R.id.btnCategoryRecommended);
        imgProfile          = findViewById(R.id.img_Profile);
        btnProfileHistory   = findViewById(R.id.btnProfileHistory);

        tvClock = findViewById(R.id.tvClock);
        tvProfileName = findViewById(R.id.tvProfileNama);

        tvSeeAllFood.setOnClickListener(this);
        tvSeeAllDrink.setOnClickListener(this);
        btnAllCategories.setOnClickListener(this);
        btnFoodCategory.setOnClickListener(this);
        btnDrinkCategory.setOnClickListener(this);
        btnRecommendedCategory.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        btnProfileHistory.setOnClickListener(this);

        loadAllData();

    }


    private void loadAllData() {

        getImage();

        Query AccountName   = dbReference.child("user").child(fbAuth.getCurrentUser().getUid()).child("name");
        AccountName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("nama", "onDataChange: "+snapshot.getValue());
                    tvProfileName.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadData(Food.CATEGORIES.Food, rvFood);
        loadData(Food.CATEGORIES.Drink, rvDrink);

        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        tvClock.setText(currentDateTimeString);
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
                            Picasso.with(MainMenuActivity.this).load(uri).into(imgProfile);

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

    @Override
    protected void onResume() {
        super.onResume();
        FoodListActivity.categoryState = "ALL";
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == tvSeeAllFood.getId() || view.getId() == btnFoodCategory.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            FoodListActivity.categoryState = Food.CATEGORY_FOOD;
            startActivity(intent);
        }

        if (view.getId() == tvSeeAllDrink.getId() || view.getId() == btnDrinkCategory.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            FoodListActivity.categoryState = Food.CATEGORY_DRINK;
            startActivity(intent);
        }

        if (view.getId() == btnAllCategories.getId() || view.getId() == btnRecommendedCategory.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            startActivity(intent);
        }

        if(view.getId() == imgProfile.getId()){

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }

        if (view.getId() == btnProfileHistory.getId()) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        }
    }



    void loadData(Food.CATEGORIES category, RecyclerView recyclerView) {

        Query checkUser = fbDB.getReference("foods");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    Log.d("TEST_TOSTRING", food.toString());
                    if (category == Food.CATEGORIES.Food) {
                        if (food.getCategory().equals(Food.CATEGORY_FOOD)) {
                            daftarFood.add(food);
                        }
                    } else if (category == Food.CATEGORIES.Drink) {
                        if (food.getCategory().equals(Food.CATEGORY_DRINK)) {
                            daftarFood.add(food);
                        }
                    }
                }

                MiniFoodAdapter adpFood = new MiniFoodAdapter(daftarFood, MainMenuActivity.this);
                recyclerView.setAdapter(adpFood);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}