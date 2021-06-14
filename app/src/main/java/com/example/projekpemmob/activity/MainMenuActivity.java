package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.MiniFoodAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.viewHolder.MiniFoodHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvSeeAllFood, tvSeeAllDrink, tvClock, tvProfileName;
    private CardView btnAllCategories;
    private RecyclerView rvFood;
    private RecyclerView rvDrink;
    private ArrayList<Food> daftarFood;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = fbDB.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();
        rvFood = findViewById(R.id.rvFood);
        rvDrink = findViewById(R.id.rvDrink);
        tvSeeAllFood = findViewById(R.id.tvSeeAllFood);
        tvSeeAllDrink = findViewById(R.id.tvSeeAllDrink);
        btnAllCategories = findViewById(R.id.btnAllFood);

        tvClock = findViewById(R.id.tvClock);
        tvProfileName = findViewById(R.id.tvProfileNama);

        tvSeeAllFood.setOnClickListener(this);
        tvSeeAllDrink.setOnClickListener(this);
        btnAllCategories.setOnClickListener(this);

        loadAllData();
//        btnLogout = findViewById(R.id.btnFoodList);
//        btnLogout.setOnClickListener(this);
    }

    private void loadAllData() {
        loadData(Food.CATEGORIES.Food, rvFood);
        loadData(Food.CATEGORIES.Drink, rvDrink);

        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        tvClock.setText(currentDateTimeString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FoodListActivity.categoryState = "ALL";
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == tvSeeAllFood.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            FoodListActivity.categoryState = Food.CATEGORY_FOOD;
            startActivity(intent);
        }

        if (view.getId() == tvSeeAllDrink.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            FoodListActivity.categoryState = Food.CATEGORY_DRINK;
            startActivity(intent);
        }

        if (view.getId() == btnAllCategories.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
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