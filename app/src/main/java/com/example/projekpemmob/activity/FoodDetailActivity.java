package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_NAME = "food_name";

    private TextView tvNama, tvHarga, tvDeskripsi, tvJumlah, tvHargaTotal;
    private ImageView imgFood, imgRating;
    private RecyclerView rvReview;
    private Button btnAddChart, btnPlus, btnMinus, btnBack;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbReference;
    private FirebaseAuth fbAuth;
    private int counter = 0;
    private int hargaMakanan, jumlahPesan;
    private String namaMakanan = "soto", descMakanan, userID, categoryIntent;

    private FirebaseStorage fbStorage;
    private ProgressBar pb;

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
        //rvReview    = findViewById(R.id.rvFoodReview);
        tvJumlah    = findViewById(R.id.tvFoodQty);
        btnAddChart = findViewById(R.id.btnFoodAddChart);
        btnMinus    = findViewById(R.id.btnFoodMinus);
        btnBack     = findViewById(R.id.btnBack);
        btnPlus     = findViewById(R.id.btnFoodPlus);
        imgFood     = findViewById(R.id.img_food);
        imgRating   = findViewById(R.id.imgFoodRating);
        tvHargaTotal= findViewById(R.id.tvTotalHarga);
        pb = findViewById(R.id.progressBarFoto);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnAddChart.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        fbStorage = FirebaseStorage.getInstance();

        loadData();
        pb.setVisibility(View.INVISIBLE);
    }

    private void loadData() {

        Intent intent = getIntent();
        String tempNama = String.valueOf(intent.getStringExtra(EXTRA_NAME));
        Query loadData = fbDB.getReference("foods").orderByChild("name").equalTo(tempNama);
        loadData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("coba2", "onDataChange: "+snapshot.getValue());
                Food food       = snapshot.getChildren().iterator().next().getValue(Food.class);
                hargaMakanan    = food.getPrice();
                namaMakanan     = food.getName();
                descMakanan     = food.getDescription();

                int ratingImage = 0;

                if (food.getRating() >= 0 && food.getRating() < 0.7) {
                    ratingImage = R.drawable.no_stars;
                } else if (food.getRating() >= 0.7 && food.getRating() < 1.7) {
                    ratingImage = R.drawable.one_stars;
                } else if (food.getRating() >= 1.7 && food.getRating() < 2.7) {
                    ratingImage = R.drawable.two_stars;
                } else if (food.getRating() >= 2.7 && food.getRating() < 3.7) {
                    ratingImage = R.drawable.three_stars;
                } else if (food.getRating() >= 3.7 && food.getRating() < 4.7) {
                    ratingImage = R.drawable.four_stars;
                } else if (food.getRating() >= 4.7) {
                    ratingImage = R.drawable.five_stars;
                }

                imgRating.setImageResource(ratingImage);

                StorageReference gsRef = fbStorage.getReferenceFromUrl(food.getImagePath());
                gsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.e("IMAGE_URL", "uri: " + uri.toString());

                    Glide.with(getApplicationContext())
                            .load(uri)
                            .apply(new RequestOptions().override(720, 720))
                            .into(imgFood);
                });

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
                    tvHargaTotal.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(hargaMakanan*fCart.getJumlahPesan()));
                    Log.d("fcart", "onDataChange: "+ fCart.getJumlahPesan());

                }else{

                    tvHargaTotal.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(0));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnPlus.getId()) {

            counter = counter + 1;
            tvJumlah.setText(String.valueOf(counter));
            int total = hargaMakanan * counter;
            tvHargaTotal.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(total));

        } else if (v.getId() == btnMinus.getId()) {

            if (counter <= 0) {
                counter = 0;
                tvJumlah.setText(String.valueOf(counter));
            } else {
                counter = counter - 1;
                tvJumlah.setText(String.valueOf(counter));
            }

            int total = hargaMakanan * counter;
            tvHargaTotal.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(total));

        }else if (v.getId() == btnAddChart.getId()){

            if(Integer.parseInt(tvJumlah.getText().toString())>0){

                FoodCart dataMakanan = new FoodCart(Integer.valueOf(tvJumlah.getText().toString()), hargaMakanan, namaMakanan);
                dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(tvNama.getText().toString()).setValue(dataMakanan);
                finish();

            }else{

                dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(tvNama.getText().toString()).removeValue();
                finish();

            }

        }else if(v.getId() == btnBack.getId()){
            finish();
        }
    }
}