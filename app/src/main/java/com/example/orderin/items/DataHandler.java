package com.example.orderin.items;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static DataHandler handler;
    private List<Food> list;

    private DataHandler() {
    }

    public static DataHandler getInstance() {
        if (handler == null)
            handler = new DataHandler();
        return handler;
    }

    public List<Food> getList() {
        return list;
    }

    public void setList(List<Food> list) {
        this.list = list;
    }
}
