package com.example.projekpemmob.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvNama, tvHarga, tvDeskripsi;
    Context context;
    FoodHolder.getRvListener rvListener;

    public FoodHolder(@NonNull View itemView, FoodHolder.getRvListener mRvListener) {
        super(itemView);

        context     = itemView.getContext();
        tvNama      = itemView.findViewById(R.id.tvNamaMakanan);
        tvHarga     = itemView.findViewById(R.id.tvHargaMakanan);
        tvDeskripsi = itemView.findViewById(R.id.tvDeskripsiMakanan);
        this.rvListener = mRvListener;
        itemView.setOnClickListener(this);

    }

    public TextView getTvNama() {
        return tvNama;
    }

    public TextView getTvHarga() {
        return tvHarga;
    }

    public TextView getTvDeskripsi() {
        return tvDeskripsi;
    }

    @Override
    public void onClick(View view) {

        rvListener.getRvClick(getAdapterPosition());

    }

    public interface getRvListener{

        void getRvClick(int position);

    }

}
