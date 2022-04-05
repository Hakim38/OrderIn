package com.example.orderin.items;

import android.util.Log;

import com.example.orderin.items.Food;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {

    private static Connection connection;

    private final String database = "postgres";
    private final String host = "10.0.2.2";
    private final int port = 5432;
    private final String user = "postgres";
    private final String password = "Hakimek9";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;

    public Database(){
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();
        //this.disconnect;
        System.out.println("connection status: " + status);
        Log.d("testLog", "connection status: " + status);
    }

    public String getDatabase() {
        return database;
    }

    private void connect(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Log.d("testLog", "trying to connect");
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    status = true;
                    System.out.println("connected: " + status);
                    Log.d("testLog", "connected: " + status);


                }
                catch (Exception e){
                    status = false;
                    System.out.println(e.getMessage());
                    Log.d("testLog", e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch (Exception e){
            e.printStackTrace();
            status = false;
        }
    }

    public Connection getExtraConnection(){
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }

    public HashMap<String, List<Food>> getFoodList(){
        Statement st = null;
        List<Food> foodList = new ArrayList<>();
        List<String> category = new ArrayList<>();
        HashMap<String, List<Food>> foodItem = new HashMap<>();
        try {
            st = connection.createStatement();

            String sql;
            sql = "SELECT food_id, food_name, food_description, price, category_id FROM food";
            ResultSet rs = st.executeQuery(sql);
            Food newFood = null;
            while (rs.next()) {
                //Retrieve by column name
                newFood = new Food(Integer.parseInt(rs.getString("food_id")),
                        rs.getString("food_name"), rs.getString("food_description")
                        , Double.parseDouble(rs.getString("price")), Integer.parseInt(rs.getString("category_id")));
                foodList.add(newFood);
                rs.getString("food_description");

            }

            sql = "SELECT category_name FROM food_category";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                //Retrieve by column name
                category.add(rs.getString("category_name"));
                //Log.d("categoryLog", "category: " + rs.getString("category_name"));
            }
            rs.close();
            st.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < category.size(); i++){
            List<Food> temp = new ArrayList<>();
            for (int x = 0; x < foodList.size(); x++){

                if (foodList.get(x).getCategory() == i + 1){
                    temp.add(foodList.get(x));
                    //Log.d("categoryLog", "temp: " + temp.get(x).getName());
                }
            }
            foodItem.put(category.get(i), temp);

        }
        Log.d("categoryLog", "category: " + foodItem.keySet().toString());
        return foodItem;
    }

    public List<String> getCategory(){
        Statement st = null;
        List<String> category = new ArrayList<>();
        try {
            st = connection.createStatement();

            String sql;
            sql = "SELECT category_name FROM food_category";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                //Retrieve by column name
                category.add(rs.getString("category_name"));
                //Log.d("categoryLog", "category: " + rs.getString("category_name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return category;
    }
    public void insertCustomer(int id, int tableNum){
        Statement st;
        try {
            st = connection.createStatement();
            String sql;
            sql = "INSERT INTO customer (customer_id, table_num) VALUES (" + id + ", " + tableNum + ")";
            st.executeQuery(sql);
            st.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertOrder(int customerId, int foodId){
        try
        {

            Statement st;
            st = connection.createStatement();
            String sql;
            sql = "INSERT INTO food_order (customer_id, food_id) VALUES (" + customerId + ", " + foodId + ")";
            Log.d("menuClick", "order made: " + sql);
            st.executeQuery(sql);
            Log.d("menuClick", "order made: " + sql);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
