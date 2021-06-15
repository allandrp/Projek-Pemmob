package com.example.projekpemmob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.FoodAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.viewHolder.FoodHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMenu extends AppCompatActivity implements View.OnClickListener, FoodHolder.getRvListener {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    private RecyclerView rvFood;
    private ArrayList<Food> daftarFood;
    private Button btnProfile;
    private TextView tvTotalHarga, tvQtyCart, tvFoodListsTitle;
    private CardView cvCart;
    private Button btnback;
    private ImageView imgProfile;
    FoodAdapter adpFood = new FoodAdapter(daftarFood, this, this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();

        cvCart              = findViewById(R.id.cvCart);
        tvQtyCart           = findViewById(R.id.tvQtyCart);
        tvTotalHarga        = findViewById(R.id.tvTotalHarga);
        rvFood              = findViewById(R.id.rv_foods);
        tvFoodListsTitle    = findViewById(R.id.tvFoodListsTitle);
        btnback             = findViewById(R.id.btnBack);
        imgProfile          = findViewById(R.id.img_Profile);

        cvCart.setOnClickListener(this);
        btnback.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        cvCart.setVisibility(View.GONE);

        loadData();

    }

    private void loadData() {

        Query FoodData = dbReference.child("foods");

        FoodData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Food food = dataSnapshot.getValue(Food.class);
                    daftarFood.add(food);
                }

                adpFood = new FoodAdapter(daftarFood, AdminMenu.this, AdminMenu.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(AdminMenu.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getRvClick(int position) {

        Intent intent = new Intent(this, AddFoodActivity.class);
        intent.putExtra("category", daftarFood.get(position).getCategory());
        intent.putExtra("name", daftarFood.get(position).getName());
        intent.putExtra("aksi", "update");
        startActivity(intent);

    }
}
