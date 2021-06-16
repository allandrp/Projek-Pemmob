package com.example.projekpemmob.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;

public class HistoryHolder extends RecyclerView.ViewHolder {

    TextView tvHarga, tvTanggal, tvDeskripsi, tvStatus;
    Context context;

    public HistoryHolder(@NonNull View itemView) {
        super(itemView);

        tvHarga     = itemView.findViewById(R.id.tv_harga_history);
        tvTanggal   = itemView.findViewById(R.id.tv_date_history);
        tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi_History);
        context     = itemView.getContext();
        tvStatus    = itemView.findViewById(R.id.tvStatus);

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
}
