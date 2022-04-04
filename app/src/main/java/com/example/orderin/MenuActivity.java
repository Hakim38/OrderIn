package com.example.orderin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu;
    private MenuSource source;
    private int tableNum, id;
    private ArrayList<Food> foodList, orderList;
    private ExpandableListView listView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvMenu = findViewById(R.id.tvMenu);
        Intent intent = getIntent();
        //takes table number from scan activity
        tableNum = intent.getIntExtra(MainActivity.INTENT_KEY, 0);

        tvMenu.setText("Table " + tableNum);
        id = new Random().nextInt();

        db = new Database();
        db.insertCustomer(id, tableNum);

        source = new MenuSource(this);
        foodList = db.getFoodList();
        orderList = new ArrayList<>();
        listView = findViewById(R.id.expandList);

        /*
        test = new ArrayList<>();
        //gets values from txt document
        test = source.readLine("menu_test.txt");
        //TODO get resources for menu and pass into foodList
        for (int i = 0; i < test.size(); i++){
            String[] temp = test.get(i).split(";");
            Food newFood = new Food(temp[0], temp[1], Double.parseDouble(temp[2]));
            foodList.add(newFood);
        }


        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        //custom list adapter for menu
        CustomAdapter adapter = new CustomAdapter(foodList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        orderList.add(foodList.get(position));
                        db.insertOrder(id, position + 1);
                        //adapter.setVisibility(!adapter.isVisibility());
                        //adapter.notifyItemChanged(position);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Log.d("menuClick", "long click");
                    }
                })
        );
*/
        FloatingActionButton menuCart = findViewById(R.id.menuButtonCart);
        menuCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i = 0; i < orderList.size(); i++) {
                    db.insertOrder(id, orderList.get(i).getId());
                }
                orderList.clear();
            }
        });
    }
}
