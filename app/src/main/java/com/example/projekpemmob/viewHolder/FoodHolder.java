package com.example.projekpemmob.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekpemmob.R;

import org.w3c.dom.Text;

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvName, tvPrice, tvDescription;
    ImageView imageRating, imageFood;
    Context context;
    FoodHolder.getRvListener rvListener;

    public FoodHolder(@NonNull View itemView, FoodHolder.getRvListener mRvListener) {
        super(itemView);

        context     = itemView.getContext();
        tvName = itemView.findViewById(R.id.tv_food_name);
        tvPrice = itemView.findViewById(R.id.tv_food_price);
        tvDescription = itemView.findViewById(R.id.tv_food_description);
        imageRating = itemView.findViewById(R.id.img_stars);
        imageFood   = itemView.findViewById(R.id.img_food);
        this.rvListener = mRvListener;
        itemView.setOnClickListener(this);

    }

    public TextView getTvName() {
        return tvName;
    }

    public TextView getTvPrice() {
        return tvPrice;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public ImageView getImageRating() {
        return imageRating;
    }

    public ImageView getImageFood() {
        return imageFood;
    }

    @Override
    public void onClick(View view) {

        rvListener.getRvClick(getAdapterPosition());

    }

    public interface getRvListener{

        void getRvClick(int position);

    }

}
