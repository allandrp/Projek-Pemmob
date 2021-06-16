package com.example.projekpemmob.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.adapter.AdminOrderAdapter;

public class AdminOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvHarga, tvTanggal, tvDeskripsi, tvStatus;
    Context context;
    AdminOrderHolder.getRvListener rvListener;

    public AdminOrderHolder(@NonNull View itemView, AdminOrderHolder.getRvListener mRvListener) {
        super(itemView);

        tvHarga     = itemView.findViewById(R.id.tvOrderCost);
        tvTanggal   = itemView.findViewById(R.id.tvOrderDate);
        tvDeskripsi = itemView.findViewById(R.id.tvFoodsList);
        tvStatus    = itemView.findViewById(R.id.tvStatus);
        context     = itemView.getContext();
        this.rvListener = mRvListener;
        itemView.setOnClickListener(this);
    }

    public TextView getTvDeskripsi() {
        return tvDeskripsi;
    }

    public TextView getTvHarga() {
        return tvHarga;
    }

    public TextView getTvTanggal() {
        return tvTanggal;
    }

    public Context getContext() {
        return context;
    }

    public TextView getTvStatus() {
        return tvStatus;
    }

    @Override
    public void onClick(View view) {
        rvListener.getRvClick(getAdapterPosition());
    }

    public interface getRvListener{
        void getRvClick(int position);
    }
}
