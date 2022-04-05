package com.example.orderin.items;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Food implements Serializable {
    private String name;
    private String description;
    private double price;
    private int id;
    private int category;
    private static int pos = 0;

    public Food(int id, String name, String description, double price, int category) {
        this.id = id;
        this.name = name;
        this.description = description;
        price = roundDouble(price);
        this.price = price;
        this.category = category;
        pos++;
    }


    public int getCategory() {
        return category;
    }

    private double roundDouble(Double num){
        DecimalFormat df = new DecimalFormat("#.##");
        num = Double.valueOf(df.format(num));
        return num;
    }

    public int getId() {
        return id;
    }

    public static int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}
