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
import com.example.projekpemmob.adapter.adapterFood;
import com.example.projekpemmob.model.food;
import com.example.projekpemmob.model.foodCart;
import com.example.projekpemmob.viewHolder.holderFood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_FoodList extends AppCompatActivity implements View.OnClickListener, holderFood.getRvListener {

    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    RecyclerView rvFood;
    ArrayList<food>daftarFood;
    Button btnProfile;
    TextView tvTotalHarga, tvQtyCart;
    CardView cvCart;

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("restart", "onRestart: ");
        Intent intentRestart = new Intent(this, activity_FoodList.class);
        startActivity(intentRestart);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__food_list);

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

        dbReference.child("food").child("sate").setValue(new food("Sate", "makanan khas madura", 20000));
        dbReference.child("food").child("soto").setValue(new food("Soto", "makanan khas lamongan", 10000));
        dbReference.child("food").child("steak").setValue(new food("Steak", "makanan khas madura", 15000));
        dbReference.child("food").child("ronde").setValue(new food("ronde", "makanan khas lamongan", 25000));


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
                        foodCart dataFood = dataSnapshot.getValue(foodCart.class);
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

                daftarFood = new ArrayList<food>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    food Food = dataSnapshot.getValue(food.class);
                    daftarFood.add(Food);
                }

                adapterFood adpFood = new adapterFood(daftarFood, activity_FoodList.this, activity_FoodList.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(activity_FoodList.this));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnProfile.getId()){

            Intent intentProfile = new Intent(this, activity_profile.class);
            startActivity(intentProfile);

        }else if (v.getId() == cvCart.getId()){

            Intent intentCart = new Intent(this, activity_cart.class);
            startActivity(intentCart);

        }

    }

    @Override
    public void getRvClick(int position) {

        Intent intentFood = new Intent(this, activity_food.class);
        intentFood.putExtra("nama", daftarFood.get(position).getNama());
        startActivity(intentFood);

    }
}