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

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener, MiniFoodHolder.getRvListener {


    TextView tvSeeAllFood;
    FirebaseAuth fbAuth;
    private RecyclerView rvFood;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    private ArrayList<Food> daftarFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();
        rvFood = findViewById(R.id.rvFood);
        fbDB = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        tvSeeAllFood = findViewById(R.id.tvSeeAllFood);

        tvSeeAllFood.setOnClickListener(this);
        loadData();
//        btnLogout = findViewById(R.id.btnFoodList);
//        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == tvSeeAllFood.getId()) {
            Intent intent = new Intent(this, FoodListActivity.class);
            startActivity(intent);
        }
    }

    void loadData() {

        Query checkUser = fbDB.getReference("foods");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<Food>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    Log.d("TEST_TOSTRING", food.toString());
                    daftarFood.add(food);
                }

                MiniFoodAdapter adpFood = new MiniFoodAdapter(daftarFood, MainMenuActivity.this, MainMenuActivity.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(MainMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void getRvClick(int position) {

        Intent intentFood = new Intent(this, FoodDetailActivity.class);
        intentFood.putExtra(FoodDetailActivity.EXTRA_NAME, daftarFood.get(position).getName());
        startActivity(intentFood);

    }
}