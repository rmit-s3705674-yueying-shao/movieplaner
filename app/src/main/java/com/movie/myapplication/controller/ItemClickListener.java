package com.movie.myapplication.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Item;
import com.movie.myapplication.view.EventDetail;
import com.movie.myapplication.view.MainActivity;

public class ItemClickListener implements AdapterView.OnItemClickListener {

    private MainActivity ma;
   private  Item[] items;

    public ItemClickListener(MainActivity ma, Item[] items) {
        this.ma = ma;
        this.items = items;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // get the title of the clicked event
        String text = items[position].getTitle();

        DBHelper dbHelper = new DBHelper(ma, "movieplan.db", null, 1);
        //   dbHelper.getWritableDatabase();

        // go to the event detail
        Intent intent = new Intent();
        intent.setClass(ma, EventDetail.class);
        intent.putExtra("eventID", dbHelper.getEventbyTitle(text).getId());
        ma.startActivity(intent);


    }


}
