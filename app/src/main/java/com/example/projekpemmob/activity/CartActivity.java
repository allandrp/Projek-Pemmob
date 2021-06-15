package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.projekpemmob.adapter.CartAdapter;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.model.History;
import com.example.projekpemmob.viewHolder.CartHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartHolder.getRvListener {

    TextView tvTotalHarga;
    ImageView imgProfile;
    Button btnPesan, btnBack;
    ArrayList<FoodCart>listCart = new ArrayList<>();
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    RecyclerView rvCart;
    CartAdapter adpCart = new CartAdapter(listCart, CartActivity.this, CartActivity.this);
    private int totalHarga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fbAuth          = FirebaseAuth.getInstance();
        fbDB            = FirebaseDatabase.getInstance();
        dbReference     = fbDB.getReference();

        tvTotalHarga    = findViewById(R.id.tvCartTotalHarga);
        btnPesan        = findViewById(R.id.btnCartPesan);
        btnBack         = findViewById(R.id.btnBack);
        rvCart          = findViewById(R.id.rv_Cart);
        imgProfile      = findViewById(R.id.img_Profile);

        btnPesan.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        tvTotalHarga.setText(String.valueOf(totalHarga));

        rvCart.setAdapter(adpCart);
        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        loadData();

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
                            Picasso.with(CartActivity.this).load(uri).into(imgProfile);

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

    private void updateData(ArrayList<FoodCart> list){





    }

    private void loadData() {

        getImage();

        Query dataChart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid());

        dataChart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    listCart.clear();
                    totalHarga = 0;
                    adpCart.notifyDataSetChanged();
                    
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        FoodCart fCart = dataSnapshot.getValue(FoodCart.class);
                        listCart.add(fCart);
                        totalHarga = totalHarga + fCart.getHarga()*fCart.getJumlahPesan();
                    }

                    Query dataFood = dbReference.child("foods");

                    dataFood.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String deleteData;

                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                Food food = dataSnapshot.getValue(Food.class);
                                int temp = 0, index = 100;
                                for(int i = 0; i < listCart.size(); i++){

                                    Log.d("delete data", "onDataChange: "+listCart.get(i).getNama().equals(food.getName()));
                                    Log.d("delete data", "nama cart : "+listCart.get(i).getNama());
                                    Log.d("delete data", "nama food : "+food.getName());
                                    Log.d("delete data", "onDataChange: "+"panjang list = " +String.valueOf(listCart.size()));

                                    index = i;
                                    if(listCart.get(i).getNama().equalsIgnoreCase(food.getName())){

                                        dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listCart.get(i).getNama()).child("harga").setValue(food.getPrice());
                                        temp = 1;

                                    }
//                                    if(temp == 0){
//                                        dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listCart.get(index).getNama()).removeValue();
//                                        listCart.remove(index);
//                                        adpCart.notifyItemRemoved(index);
//                                    }
                                }



                                adpCart.notifyDataSetChanged();
                                tvTotalHarga.setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(totalHarga));


                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btnPesan.getId()){

            Intent intent = new Intent(this, ProcessOrderActivity.class);
            startActivity(intent);

        }else if (view.getId() == btnBack.getId()){

            finish();

        }else if (view.getId() == imgProfile.getId()){

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }

    }

    @Override
    public void getRvClick(int position) {

        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra(FoodDetailActivity.EXTRA_NAME, listCart.get(position).getNama());
        startActivity(intent);

    }

    @Override
    public void onAddClick(int position) {

        dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listCart.get(position).getNama()).child("jumlahPesan").setValue(listCart.get(position).getJumlahPesan()+1);
        adpCart.notifyDataSetChanged();

    }

    @Override
    public void onMinusClick(int position) {

        if(listCart.get(position).getJumlahPesan()>1){

            dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listCart.get(position).getNama()).child("jumlahPesan").setValue(listCart.get(position).getJumlahPesan()-1);
            adpCart.notifyDataSetChanged();

        }else{

            onDeleteClick(position);
            if(listCart.size() == 0){

                finish();

            }


        }
    }

    @Override
    public void onDeleteClick(int position) {

        dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listCart.get(position).getNama()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                }
            }
        });
        totalHarga  = totalHarga - listCart.get(position).getHarga()*listCart.get(position).getJumlahPesan();
        tvTotalHarga.setText(String.valueOf(totalHarga));
        listCart.remove(position);
        adpCart.notifyItemRemoved(position);
    }
}