package com.example.orderin;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Food {
    private String name;
    private String description;
    private double price;
    private int id;
    private boolean visible;
    private static int pos = 0;

    public Food(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        price = roundDouble(price);
        this.price = price;
        this.visible = false;
        pos++;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
