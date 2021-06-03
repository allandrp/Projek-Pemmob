package com.example.projekpemmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.viewHolder.FoodHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {
    private ArrayList<Food> daftarFood;
    private Context context;
    FoodHolder.getRvListener rvListener;

    public FoodAdapter(ArrayList<Food> daftarFood, Context context, FoodHolder.getRvListener rvListener) {
        this.daftarFood = daftarFood;
        this.context    = context;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_food,parent,false);
        FoodHolder vhfood = new FoodHolder(v, rvListener);

        return vhfood;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {

        holder.getTvNama().setText(daftarFood.get(position).getNama());
        holder.getTvHarga().setText("Rp. "+NumberFormat.getInstance(Locale.ITALY).format(daftarFood.get(position).getHarga()));
        holder.getTvDeskripsi().setText(daftarFood.get(position).getDeskripsi());

    }

    @Override
    public int getItemCount() {
        return daftarFood.size();
    }
}
