package com.example.orderin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.orderin.adapters.ListAdapter;
import com.example.orderin.items.Database;
import com.example.orderin.items.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private Database db;
    private List<Food> orderList;
    private int id;
    private FloatingActionButton button;
    private ListView listView;
    private TextView txtTotal,txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        txtEmpty = findViewById(R.id.orderEmpty);
        txtEmpty.setVisibility(View.GONE);
        //id = getIntent().getIntExtra(MenuActivity.INTENT_KEY, 0);
        Intent intent = getIntent();
        id = intent.getIntExtra(MenuActivity.INTENT_KEY_ID, 0);
        Log.d("testLog", "id: " + id);
        Bundle bundle = intent.getBundleExtra(MenuActivity.INTENT_KEY);
        orderList = (ArrayList<Food>) bundle.getSerializable("ARRAYLIST");
        db = new Database();

        listView = findViewById(R.id.orderListView);
        ListAdapter listAdapter = new ListAdapter(this, R.layout.list_adapter, orderList);
        listView.setAdapter(listAdapter);

        makeTotal();

        button = findViewById(R.id.orderButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < orderList.size(); i++) {
                    db.insertOrder(id, orderList.get(i).getId());
                }
                orderList.clear();
                listAdapter.notifyDataSetChanged();
                makeTotal();
                finish();
            }
        });
    }

    private void makeTotal(){
        txtTotal = findViewById(R.id.orderTotal);
        double total = 0;
        if (!orderList.isEmpty()) {
            txtEmpty.setVisibility(View.GONE);
            for (int i = 0; i < orderList.size(); i++) {
                total += orderList.get(i).getPrice();
            }
        }
        else {
            txtEmpty.setVisibility(View.VISIBLE);
        }
        txtTotal.setText("Total: " + roundDouble(total) + " €");
    }

    private double roundDouble(Double num){
        DecimalFormat df = new DecimalFormat("#.##");
        num = Double.valueOf(df.format(num));
        return num;
    }
}