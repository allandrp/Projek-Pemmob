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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.FoodAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.viewHolder.FoodHolder;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodListActivity extends AppCompatActivity implements View.OnClickListener, FoodHolder.getRvListener {

    public static final String EXTRA_CATEGORY = "category";
    public static String categoryState = "ALL";

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

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();
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

        loadData(categoryState);

        if (Integer.parseInt(tvQtyCart.getText().toString()) == 0) {

            cvCart.setVisibility(View.INVISIBLE);

        } else {

            cvCart.setVisibility(View.VISIBLE);

        }
    }

    private void loadCart() {

        Query dataCart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid());

        dataCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Log.d("coba3", "onDataChange: " + snapshot.getValue());
                    int jumlahPesanan = 0, totalHarga = 0;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FoodCart dataFood = dataSnapshot.getValue(FoodCart.class);
                        jumlahPesanan = jumlahPesanan + dataFood.getJumlahPesan();
                        totalHarga = totalHarga + (dataFood.getHarga() * dataFood.getJumlahPesan());
                    }

                    tvQtyCart.setText(String.valueOf(jumlahPesanan));
                    tvTotalHarga.setText("Rp. " + NumberFormat.getInstance(Locale.ITALY).format(totalHarga));

                    if (Integer.valueOf(tvQtyCart.getText().toString()) == 0) {

                        cvCart.setVisibility(View.INVISIBLE);

                    } else {

                        cvCart.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void loadData(String category) {

        getImage();


        Query checkUser = fbDB.getReference("foods");

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                daftarFood = new ArrayList<Food>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    Log.d("TEST_TOSTRING", food.toString());
                    if (category.equals(Food.CATEGORY_FOOD)) {
                        if (food.getCategory().equals(Food.CATEGORY_FOOD)) {
                            daftarFood.add(food);
                            tvFoodListsTitle.setText(R.string.foods);
                        }
                    } else if (category.equals(Food.CATEGORY_DRINK)) {
                        if (food.getCategory().equals(Food.CATEGORY_DRINK)) {
                            daftarFood.add(food);
                            tvFoodListsTitle.setText(R.string.drinks);
                        }
                    } else {
                        daftarFood.add(food);
                        categoryState = "ALL";
                    }


                }

                loadCart();
                adpFood = new FoodAdapter(daftarFood, FoodListActivity.this, FoodListActivity.this);
                rvFood.setAdapter(adpFood);
                rvFood.setLayoutManager(new LinearLayoutManager(FoodListActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                            Picasso.with(FoodListActivity.this).load(uri).into(imgProfile);

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
    public void onClick(View v) {

        if (v.getId() == cvCart.getId()) {

            Intent intentCart = new Intent(this, CartActivity.class);
            startActivity(intentCart);

        } else if (v.getId() == btnback.getId()) {

            finish();

        }else if (v.getId() == imgProfile.getId()){

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }

    }

    @Override
    public void getRvClick(int position) {

        Intent intentFood = new Intent(this, FoodDetailActivity.class);
        intentFood.putExtra(FoodDetailActivity.EXTRA_NAME, daftarFood.get(position).getName());
        startActivity(intentFood);

    }
}