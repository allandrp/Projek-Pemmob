package com.example.projekpemmob.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.History;
import com.example.projekpemmob.viewHolder.FoodHolder;
import com.example.projekpemmob.viewHolder.HistoryHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

    ArrayList<History>listHistory = new ArrayList<>();

    public HistoryAdapter(ArrayList<History> listHistory) {
        this.listHistory = listHistory;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_history,parent,false);
        return new HistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  HistoryHolder holder, int position) {
        holder.getTvHarga().setText("Rp"+ NumberFormat.getInstance(Locale.ITALY).format(listHistory.get(position).getTotalPrice()));
        holder.getTvTanggal().setText(listHistory.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }
}
