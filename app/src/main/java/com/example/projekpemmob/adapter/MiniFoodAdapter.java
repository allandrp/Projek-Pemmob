package com.example.projekpemmob.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projekpemmob.R;
import com.example.projekpemmob.activity.FoodDetailActivity;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.viewHolder.MiniFoodHolder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class MiniFoodAdapter extends RecyclerView.Adapter<MiniFoodHolder> {
    private ArrayList<Food> foodList;
    private Context context;

    public MiniFoodAdapter(ArrayList<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context    = context;
    }

    @NonNull
    @Override
    public MiniFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_mini_food,parent,false);

        return new MiniFoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniFoodHolder holder, int position) {
        Food food = foodList.get(position);

        holder.getTvName().setText(food.getName());
        holder.getTvPrice().setText("Rp"+NumberFormat.getInstance(Locale.ITALY).format(food.getPrice()));

        int ratingImage = 0;

        if (food.getRating() >= 0 && food.getRating() < 0.7) {
            ratingImage = R.drawable.no_stars;
        } else if (food.getRating() >= 0.7 && food.getRating() < 1.7) {
            ratingImage = R.drawable.one_stars;
        } else if (food.getRating() >= 1.7 && food.getRating() < 2.7) {
            ratingImage = R.drawable.two_stars;
        } else if (food.getRating() >= 2.7 && food.getRating() < 3.7) {
            ratingImage = R.drawable.three_stars;
        } else if (food.getRating() >= 3.7 && food.getRating() < 4.7) {
            ratingImage = R.drawable.four_stars;
        } else if (food.getRating() >= 4.7) {
            ratingImage = R.drawable.five_stars;
        }


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference gsRef = storage.getReferenceFromUrl(food.getImagePath());
            gsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.e("IMAGE_URL", "uri: " + uri.toString());

                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .apply(new RequestOptions().override(300, 300))
                        .into(holder.getImageFood());
            });


        Glide.with(holder.itemView.getContext())
                .load(ratingImage)
                .apply(new RequestOptions().override(200, 200))
                .into(holder.getImageRating());



        holder.getCvMiniFoodCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFood = new Intent(context, FoodDetailActivity.class);
                intentFood.putExtra(FoodDetailActivity.EXTRA_NAME, food.getName());
                context.startActivity(intentFood);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
