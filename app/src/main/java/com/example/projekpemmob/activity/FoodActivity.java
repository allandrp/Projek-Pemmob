package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvNama, tvHarga, tvDeskripsi, tvJumlah;
    RecyclerView rvReview;
    Button btnAddChart, btnPlus, btnMinus;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    int counter = 0;
    int hargaMakanan, jumlahPesan;
    String namaMakanan = "soto", descMakanan, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        fbAuth      = FirebaseAuth.getInstance();
        userID      =fbAuth.getCurrentUser().getUid();
        fbDB        = FirebaseDatabase.getInstance();
        dbReference = fbDB.getReference();
        tvNama      = findViewById(R.id.tvFoodNama);
        tvDeskripsi = findViewById(R.id.tvFoodDeskripsi);
        tvHarga     = findViewById(R.id.tvFoodHarga);
        rvReview    = findViewById(R.id.rvFoodReview);
        tvJumlah    = findViewById(R.id.tvFoodQty);
        btnAddChart = findViewById(R.id.btnFoodAddChart);
        btnMinus    = findViewById(R.id.btnFoodMinus);
        btnPlus     = findViewById(R.id.btnFoodPlus);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnAddChart.setOnClickListener(this);

        loadData();
    }

    private void loadData() {

        Intent intent = getIntent();
        String tempNama = String.valueOf(intent.getStringExtra("nama"));
        Query loadData = fbDB.getReference("food").orderByChild("name").equalTo(tempNama);
        loadData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("coba2", "onDataChange: "+snapshot.getValue());
                Food Food       = snapshot.getChildren().iterator().next().getValue(Food.class);
                hargaMakanan    = Food.getPrice();
                namaMakanan     = Food.getName();
                descMakanan     = Food.getDescription();
                tvNama.setText(namaMakanan);
                tvHarga.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(hargaMakanan));
                tvDeskripsi.setText(descMakanan);
                loadQty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    public void loadQty(){

        Query loadCart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).orderByChild("nama").equalTo(namaMakanan);

        loadCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Log.d("cobaFood", "onDataChange: "+snapshot.getValue());
                    FoodCart fCart = snapshot.getChildren().iterator().next().getValue(FoodCart.class);
                    tvJumlah.setText(String.valueOf(fCart.getJumlahPesan()));
                    counter = fCart.getJumlahPesan();
                    Log.d("fcart", "onDataChange: "+ fCart.getJumlahPesan());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnPlus.getId()){

            counter = counter + 1;
            tvJumlah.setText(String.valueOf(counter));

        }else if (v.getId() == btnMinus.getId()){

            if(counter <= 0){

                counter = 0;
                tvJumlah.setText(String.valueOf(counter));

            }else{

                counter = counter - 1;
                tvJumlah.setText(String.valueOf(counter));

            }

        }else if (v.getId() == btnAddChart.getId()){

            if(Integer.parseInt(tvJumlah.getText().toString())>0){

                FoodCart dataMakanan = new FoodCart(Integer.valueOf(tvJumlah.getText().toString()), hargaMakanan, namaMakanan);
                dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(tvNama.getText().toString()).setValue(dataMakanan);
                finish();

            }else{

                dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(tvNama.getText().toString()).removeValue();
                finish();

            }

        }
    }
}