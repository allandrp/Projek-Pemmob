package com.example.projekpemmob.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;
import com.example.projekpemmob.model.History;
import com.example.projekpemmob.viewHolder.AdminOrderHolder;
import com.example.projekpemmob.viewHolder.HistoryHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderHolder> {

    ArrayList<History>listHistory = new ArrayList<>();
    AdminOrderHolder.getRvListener rvListener;

    public AdminOrderAdapter(ArrayList<History> listHistory, AdminOrderHolder.getRvListener rvListener) {
        this.listHistory = listHistory;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public AdminOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_order,parent,false);
        return new AdminOrderHolder(v, rvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull  AdminOrderHolder holder, int position) {

        int itemCount = 0;

        for(int i = 0; i<listHistory.get(position).getListMakanan().size(); i++){

            itemCount = itemCount+listHistory.get(position).getListMakanan().get(i).getJumlahPesan();

        }

        holder.getTvHarga().setText(itemCount+" items, "+"Rp "+ NumberFormat.getInstance(Locale.ITALY).format(listHistory.get(position).getTotalPrice()));
//        holder.getTvStatus().setText(listHistory.get(position).getStatus());
        String strHistory = "";

        for(int j = 0; j<listHistory.get(position).getListMakanan().size(); j++){

            if(j == listHistory.get(position).getListMakanan().size()-1){
                strHistory = strHistory + listHistory.get(position).getListMakanan().get(j).getJumlahPesan()+" "+listHistory.get(position).getListMakanan().get(j).getNama();
            }else{
                strHistory = strHistory + listHistory.get(position).getListMakanan().get(j).getJumlahPesan()+" "+listHistory.get(position).getListMakanan().get(j).getNama()+", ";
            }

        }

        holder.getTvTanggal().setText(listHistory.get(position).getDate());
        holder.getTvDeskripsi().setText(strHistory);

        String status = listHistory.get(position).getStatus();

        if (status.equals("done")) {
            holder.getTvStatus().setText(status);
            holder.getTvStatus().setTextColor(holder.getContext().getResources().getColor(R.color.blue_main));
        } else {
            holder.getTvStatus().setText(status);
        }
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }
}
