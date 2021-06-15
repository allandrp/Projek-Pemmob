package com.example.projekpemmob.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projekpemmob.R;
import com.example.projekpemmob.model.Food;
import com.example.projekpemmob.viewHolder.FoodHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {
    private ArrayList<Food> foodList;
    private Activity activity;
    FoodHolder.getRvListener rvListener;

    public FoodAdapter(ArrayList<Food> foodList, Activity activity, FoodHolder.getRvListener rvListener) {
        this.foodList   = foodList;
        this.activity   = activity;
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
        Food food = foodList.get(position);

        holder.getTvName().setText(food.getName());
        holder.getTvPrice().setText("Rp"+NumberFormat.getInstance(Locale.ITALY).format(food.getPrice()));
        holder.getTvDescription().setText(food.getDescription());

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

        if (!activity.isFinishing()) {
            Glide.with(holder.itemView.getContext())
                    .load(ratingImage)
                    .apply(new RequestOptions().override(200, 200))
                    .into(holder.getImageRating());
        }

        gsRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.e("IMAGE_URL", "uri: " + uri.toString());

            if (!activity.isFinishing()) {
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .apply(new RequestOptions().override(300, 300))
                        .into(holder.getImageFood());
            }

        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
