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
    private ArrayList<Food> foodList;
    private Context context;
    FoodHolder.getRvListener rvListener;

    public FoodAdapter(ArrayList<Food> foodList, Context context, FoodHolder.getRvListener rvListener) {
        this.foodList = foodList;
        this.context    = context;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_food,parent,false);

        return new FoodHolder(v, rvListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {

        holder.getTvName().setText(foodList.get(position).getName());
        holder.getTvPrice().setText("Rp. "+NumberFormat.getInstance(Locale.ITALY).format(foodList.get(position).getPrice()));
        holder.getTvDescription().setText(foodList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
