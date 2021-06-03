package com.example.projekpemmob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.adapterCart;
import com.example.projekpemmob.adapter.adapterFood;
import com.example.projekpemmob.model.food;
import com.example.projekpemmob.model.foodCart;
import com.example.projekpemmob.viewHolder.holderCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class activity_cart extends AppCompatActivity implements View.OnClickListener, holderCart.getRvListener {

    TextView tvTotalHarga;
    Button btnPesan;
    ArrayList<foodCart>listCart = new ArrayList<>();
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbReference;
    RecyclerView rvCart;
    adapterCart adpCart = new adapterCart(listCart, activity_cart.this, activity_cart.this);
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
        rvCart          = findViewById(R.id.rvCart);

        btnPesan.setOnClickListener(this);
        tvTotalHarga.setText(String.valueOf(totalHarga));

        rvCart.setAdapter(adpCart);
        rvCart.setLayoutManager(new LinearLayoutManager(activity_cart.this));

        loadData();

    }

    private void loadData() {

        Query dataChart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid());

        dataChart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    listCart.clear();
                    totalHarga = 0;
                    adpCart.notifyDataSetChanged();

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        foodCart fCart = dataSnapshot.getValue(foodCart.class);
                        listCart.add(fCart);
                        totalHarga = totalHarga + fCart.getHarga()*fCart.getJumlahPesan();
                    }

                    adpCart.notifyDataSetChanged();
                    tvTotalHarga.setText(String.valueOf(totalHarga));


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

            dbReference.child("history").child(fbAuth.getCurrentUser().getUid()).child(String.valueOf(UUID.randomUUID())).setValue(listCart);
            listCart.clear();
            dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).removeValue();
            Intent intent = new Intent(this, activity_FoodList.class);
            startActivity(intent);
            finish();

        }

    }

    @Override
    public void getRvClick(int position) {

        Intent intent = new Intent(this, activity_food.class);
        intent.putExtra("nama", listCart.get(position).getNama());
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