package com.example.orderin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.orderin.adapters.ExpandAdapter;
import com.example.orderin.items.Database;
import com.example.orderin.items.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    public static final String INTENT_KEY = "LIST_INTENT";
    public static final String INTENT_KEY_ID = "ID_INTENT";

    private TextView tvMenu;
    private int tableNum, id;
    private List<Food>  orderList;
    private List<String>  categoryList;
    HashMap<String, List<Food>> listItem;
    private ExpandableListView listView;
    private Database db;
    private ExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvMenu = findViewById(R.id.tvMenu);
        Intent intent = getIntent();

        //takes table number from scan activity
        tableNum = intent.getIntExtra(ScannerActivity.INTENT_KEY, 0);

        tvMenu.setText("Table " + tableNum);
        id = new Random().nextInt();

        db = new Database();
        db.insertCustomer(id, tableNum);

        orderList = new ArrayList<>();
        categoryList = db.getCategory();
        listItem = db.getFoodList();
        listView = findViewById(R.id.expandList);
        listView.setIndicatorBounds(0,0);
        adapter = new ExpandAdapter(this, categoryList, listItem);

        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v
                    , int groupPosition, int childPosition, long id) {
                orderList.add(listItem.get(categoryList.get(groupPosition)).get(childPosition));

                return false;
            }
        });


        FloatingActionButton menuCart = findViewById(R.id.menuButtonCart);
        menuCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ARRAYLIST", (Serializable) orderList);
                intent.putExtra(INTENT_KEY, bundle);
                intent.putExtra(INTENT_KEY_ID, id);
                startActivity(intent);
                orderList.clear();
            }
        });
    }
}
