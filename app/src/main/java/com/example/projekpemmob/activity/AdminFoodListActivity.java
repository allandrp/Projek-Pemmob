package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.FoodAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.viewHolder.FoodHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminFoodListActivity extends AppCompatActivity implements FoodHolder.getRvListener {

    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    private RecyclerView rvFood;
    private ArrayList<Food> daftarFood;
    private TextView tvFoodListsTitle;
    private Button btnBack;
    FoodAdapter adpFood = new FoodAdapter(daftarFood, this, this);

    public static final String EXTRA_CATEGORY = "category";
    public static String categoryState = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_list);

        fbDB = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        rvFood = findViewById(R.id.rv_foods);
        tvFoodListsTitle    = findViewById(R.id.tvFoodListsTitle);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadData();
    }

    void loadData() {
        Query checkUser = fbDB.getReference("foods");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);

                    categoryState = "ALL";
                    daftarFood.add(food);
                }

                adpFood = new FoodAdapter(daftarFood, AdminFoodListActivity.this, AdminFoodListActivity.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(AdminFoodListActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getRvClick(int position) {
        Intent intentFood = new Intent(this, AdminFoodDetailActivity.class);
        intentFood.putExtra(FoodDetailActivity.EXTRA_NAME, daftarFood.get(position).getName());
        startActivity(intentFood);
    }
}