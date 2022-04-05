package com.example.orderin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.orderin.R;
import com.example.orderin.items.Food;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Food> {

private int resourceLayout;
private Context mContext;

public ListAdapter(Context context, int resource, List<Food> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
        LayoutInflater vi;
        vi = LayoutInflater.from(mContext);
        v = vi.inflate(resourceLayout, null);
        }

        Food p = getItem(position);

        if (p != null) {
        TextView tt1 = (TextView) v.findViewById(R.id.orderAmount);
        TextView tt2 = (TextView) v.findViewById(R.id.orderName);
        TextView tt3 = (TextView) v.findViewById(R.id.orderPrice);

        if (tt1 != null) {
        tt1.setText("1x");
        }

        if (tt2 != null) {
        tt2.setText(p.getName());
        }

        if (tt3 != null) {
        tt3.setText(String.valueOf(p.getPrice()));
        }
        }

        return v;
        }

        }
