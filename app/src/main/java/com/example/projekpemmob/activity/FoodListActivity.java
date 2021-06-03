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

public class FoodListActivity extends AppCompatActivity implements View.OnClickListener, FoodHolder.getRvListener {

    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    RecyclerView rvFood;
    ArrayList<Food>daftarFood;
    Button btnProfile;
    TextView tvTotalHarga, tvQtyCart;
    CardView cvCart;

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("restart", "onRestart: ");
        Intent intentRestart = new Intent(this, FoodListActivity.class);
        startActivity(intentRestart);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        fbAuth      = FirebaseAuth.getInstance();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();

        btnProfile      = findViewById(R.id.btnFoodListProfile);
        cvCart          = findViewById(R.id.cvCart);
        tvQtyCart       = findViewById(R.id.tvQtyCart);
        tvTotalHarga    = findViewById(R.id.tvTotalHarga);
        rvFood          = findViewById(R.id.rvFood);

        cvCart.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

        dbReference.child("food").child("sate").setValue(new Food("Sate", "makanan khas madura", 20000));
        dbReference.child("food").child("soto").setValue(new Food("Soto", "makanan khas lamongan", 10000));
        dbReference.child("food").child("steak").setValue(new Food("Steak", "makanan khas madura", 15000));
        dbReference.child("food").child("ronde").setValue(new Food("ronde", "makanan khas lamongan", 25000));


        loadCart();
        loadData();

        if(Integer.valueOf(tvQtyCart.getText().toString()) == 0){

            cvCart.setVisibility(View.INVISIBLE);

        }else{

            cvCart.setVisibility(View.VISIBLE);

        }


    }

    private void loadCart(){

        Query dataCart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid());

        dataCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Log.d("coba3", "onDataChange: "+snapshot.getValue());
                    int jumlahPesanan = 0, totalHarga = 0;

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        FoodCart dataFood = dataSnapshot.getValue(FoodCart.class);
                        jumlahPesanan   = jumlahPesanan+dataFood.getJumlahPesan();
                        totalHarga      = totalHarga+(dataFood.getHarga()*dataFood.getJumlahPesan());
                    }

                    tvQtyCart.setText(String.valueOf(jumlahPesanan));
                    tvTotalHarga.setText(String.valueOf(totalHarga));


                    if(Integer.valueOf(tvQtyCart.getText().toString()) == 0){

                        cvCart.setVisibility(View.INVISIBLE);

                    }else{

                        cvCart.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void loadData(){

        Query checkUser = fbDB.getReference("food");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<Food>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Food Food = dataSnapshot.getValue(com.example.projekpemmob.model.Food.class);
                    daftarFood.add(Food);
                }

                FoodAdapter adpFood = new FoodAdapter(daftarFood, FoodListActivity.this, FoodListActivity.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(FoodListActivity.this));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnProfile.getId()){

            Intent intentProfile = new Intent(this, ProfileActivity.class);
            startActivity(intentProfile);

        }else if (v.getId() == cvCart.getId()){

            Intent intentCart = new Intent(this, CartActivity.class);
            startActivity(intentCart);

        }

    }

    @Override
    public void getRvClick(int position) {

        Intent intentFood = new Intent(this, FoodActivity.class);
        intentFood.putExtra("nama", daftarFood.get(position).getNama());
        startActivity(intentFood);

    }
}