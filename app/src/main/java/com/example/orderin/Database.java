package com.example.orderin;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    private static Connection connection;

    private final String database = "test";
    private final String host = "10.0.2.2";
    private final int port = 5433;
    private final String user = "postgres";
    private final String password = "postgres";
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

    public ArrayList<Food> getFoodList(){
        Statement st = null;
        ArrayList<Food> foodList = new ArrayList<>();
        try {
            st = connection.createStatement();

            String sql;
            sql = "SELECT food_id, food_name, food_description, price FROM food";
            ResultSet rs = st.executeQuery(sql);
            Food newFood = null;
            while (rs.next()) {
                //Retrieve by column name
                newFood = new Food(Integer.parseInt(rs.getString("food_id")),rs.getString("food_name"), rs.getString("food_description"), Double.parseDouble(rs.getString("price")));
                foodList.add(newFood);

                rs.getString("food_description");

            }
            rs.close();
            st.close();
            return foodList;
        }
        catch (Exception e){
            e.printStackTrace();
            return foodList;
        }
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
