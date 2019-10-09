package com.movie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.movie.myapplication.R;
import com.movie.myapplication.model.Item;

/*

   Put event data to ListView

*/

public class eventItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private Item[] items;

    public eventItemAdapter(Context context, Item[] items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = items[position];

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        }

        TextView eventTxt = convertView.findViewById(R.id.textEvent);
        TextView SdateTxt = convertView.findViewById(R.id.textSdate);

        eventTxt.setText(item.getTitle());

        SdateTxt.setText(item.getsDate());

        return convertView;
    }


}
