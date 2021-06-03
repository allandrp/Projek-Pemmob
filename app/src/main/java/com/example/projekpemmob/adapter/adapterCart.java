package com.example.projekpemmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.foodCart;
import com.example.projekpemmob.viewHolder.holderCart;
import com.example.projekpemmob.viewHolder.holderFood;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class adapterCart extends RecyclerView.Adapter<holderCart>{

    ArrayList<foodCart>listMakanan = new ArrayList<>();
    private Context context;
    holderCart.getRvListener rvListener;

    public adapterCart(ArrayList<foodCart> listMakanan, Context context, holderCart.getRvListener rvListener) {
        this.listMakanan = listMakanan;
        this.context = context;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public holderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart,parent,false);
        holderCart vhCart = new holderCart(v, rvListener);

        return vhCart;
    }

    @Override
    public void onBindViewHolder(@NonNull holderCart holder, int position) {

        holder.getTvNama().setText(listMakanan.get(position).getNama());
        holder.getTvHarga().setText("Rp. "+ NumberFormat.getInstance(Locale.ITALY).format(listMakanan.get(position).getHarga()));
        holder.getTvJumlahPesan().setText(String.valueOf(listMakanan.get(position).getJumlahPesan()));

    }

    @Override
    public int getItemCount() {
        return listMakanan.size();
    }
}
