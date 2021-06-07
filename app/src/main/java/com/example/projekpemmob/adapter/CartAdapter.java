package com.example.projekpemmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.FoodCart;
import com.example.projekpemmob.viewHolder.CartHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartHolder>{

    ArrayList<FoodCart>listMakanan = new ArrayList<>();
    private Context context;
    CartHolder.getRvListener rvListener;
    int angka = 0;

    public CartAdapter(ArrayList<FoodCart> listMakanan, Context context, CartHolder.getRvListener rvListener) {
        this.listMakanan = listMakanan;
        this.context = context;
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

        holder.getTvNama().setText(listMakanan.get(position).getNama());
        holder.getTvHarga().setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(listMakanan.get(position).getHarga()));
        holder.getTvJumlahPesan().setText(String.valueOf(listMakanan.get(position).getJumlahPesan()));
        holder.getNo().setText();

    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }
}
