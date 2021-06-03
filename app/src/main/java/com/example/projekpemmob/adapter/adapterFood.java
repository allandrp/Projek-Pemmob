package com.example.projekpemmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.food;
import com.example.projekpemmob.viewHolder.holderFood;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class adapterFood extends RecyclerView.Adapter<holderFood> {
    private ArrayList<food> daftarFood;
    private Context context;
    holderFood.getRvListener rvListener;

    public adapterFood(ArrayList<food> daftarFood, Context context, holderFood.getRvListener rvListener) {
        this.daftarFood = daftarFood;
        this.context    = context;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public holderFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_food,parent,false);
        holderFood vhfood = new holderFood(v, rvListener);

        return vhfood;
    }

    @Override
    public void onBindViewHolder(@NonNull holderFood holder, int position) {

        holder.getTvNama().setText(daftarFood.get(position).getNama());
        holder.getTvHarga().setText("Rp. "+NumberFormat.getInstance(Locale.ITALY).format(daftarFood.get(position).getHarga()));
        holder.getTvDeskripsi().setText(daftarFood.get(position).getDeskripsi());

    }

    @Override
    public int getItemCount() {
        return daftarFood.size();
    }
}
