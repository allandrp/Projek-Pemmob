package com.example.projekpemmob.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;

public class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvNama, tvHarga, tvJumlahPesan, tvTotal;
    Button  btnDelete, btnTambah, btnMinus;
    CartHolder.getRvListener rvListener;

    public CartHolder(@NonNull View itemView, CartHolder.getRvListener mrvListener) {
        super(itemView);
        tvNama          = itemView.findViewById(R.id.tvCartNama);
        tvHarga         = itemView.findViewById(R.id.tvCartHarga);
        tvJumlahPesan   = itemView.findViewById(R.id.tvCartJumlah);
        tvTotal         = itemView.findViewById(R.id.tvCartTotalHarga);
        btnDelete       = itemView.findViewById(R.id.btnCartDelete);
        btnTambah       = itemView.findViewById(R.id.btnCardTambah);
        btnMinus        = itemView.findViewById(R.id.btnCartMinus);
        this.rvListener = mrvListener;

        itemView.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnTambah.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

    }

    public TextView getTvNama() {
        return tvNama;
    }

    public TextView getTvHarga() {
        return tvHarga;
    }

    public TextView getTvJumlahPesan() {
        return tvJumlahPesan;
    }

    public TextView getTvTotal() {
        return tvTotal;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnTambah() {
        return btnTambah;
    }

    public Button getBtnMinus() {
        return btnMinus;
    }

    public getRvListener getRvListener() {
        return rvListener;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == btnDelete.getId()){
            rvListener.onDeleteClick(getAdapterPosition());

        }else if(view.getId() == btnTambah.getId()){
            rvListener.onAddClick(getAdapterPosition());
        }else if(view.getId() == btnMinus.getId()){
            rvListener.onMinusClick(getAdapterPosition());
        }else{
            rvListener.getRvClick(getAdapterPosition());
        }

    }

    public interface getRvListener{

        void getRvClick(int position);
        void onAddClick(int position);
        void onMinusClick(int position);
        void onDeleteClick(int position);

    }

}
