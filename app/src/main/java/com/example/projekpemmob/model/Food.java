package com.example.projekpemmob.model;

public class Food{
    private String name, description, imagePath, category;
    private int price;
    private double rating;

    public static final String CATEGORY_FOOD = "food";
    public static final String CATEGORY_DRINK = "drink";

    public enum CATEGORIES {
        Food, Drink
    }

    public Food(String name, String description, String imagePath, int price, double rating, String category) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.rating = rating;
        this.category = category;
    }

    public Food() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }
}
