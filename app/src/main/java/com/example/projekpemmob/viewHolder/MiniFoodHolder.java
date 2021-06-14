package com.example.projekpemmob.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;

public class MiniFoodHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvPrice;
    ImageView imageRating, imageFood;
    CardView cvMiniFoodCard;
    Context context;

    public MiniFoodHolder(@NonNull View itemView) {
        super(itemView);

        context         = itemView.getContext();
        tvName          = itemView.findViewById(R.id.tv_food_name);
        tvPrice         = itemView.findViewById(R.id.tv_food_price);
        imageRating     = itemView.findViewById(R.id.img_stars);
        imageFood       = itemView.findViewById(R.id.img_food);
        cvMiniFoodCard  = itemView.findViewById(R.id.cvMiniFoodCard);

    }

    public TextView getTvName() {
        return tvName;
    }

    public TextView getTvPrice() {
        return tvPrice;
    }

    public ImageView getImageRating() {
        return imageRating;
    }

    public ImageView getImageFood() {
        return imageFood;
    }

    public CardView getCvMiniFoodCard() {
        return cvMiniFoodCard;
    }

    public Context getContext() {
        return context;
    }
}
