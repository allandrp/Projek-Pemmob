package com.example.projekpemmob.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.viewHolder.CartHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartHolder>{

    ArrayList<FoodCart>listMakanan = new ArrayList<>();
    private Activity activity;
    CartHolder.getRvListener rvListener;
    FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbFoodReference = fbDatabase.getReference().child("foods");
    int angka = 0;

    public CartAdapter(ArrayList<FoodCart> listMakanan, Activity activity, CartHolder.getRvListener rvListener) {
        this.listMakanan = listMakanan;
        this.activity = activity;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_cart,parent,false);
        CartHolder vhCart = new CartHolder(v, rvListener);

        return vhCart;
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        FoodCart foodCart = listMakanan.get(position);

        Query foodQuery = dbFoodReference.orderByChild("name").equalTo(foodCart.getNama());
        foodQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getChildren().iterator().next().getValue(Food.class);
                holder.getTvNama().setText(food.getName());
                holder.getTvDescription().setText(food.getDescription());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference gsRef = storage.getReferenceFromUrl(food.getImagePath());

                gsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.e("IMAGE_URL", "uri: " + uri.toString());

                    if (!activity.isFinishing()) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .apply(new RequestOptions().override(300, 300))
                                .into(holder.getImgCartFood());
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.getTvHarga().setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(foodCart.getHarga()));
        holder.getTvJumlahPesan().setText(String.valueOf(foodCart.getJumlahPesan()));
        angka = listMakanan.indexOf(foodCart)+1;
        holder.getNo().setText(String.valueOf(angka));

    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }
}
