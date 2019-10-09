package com.movie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.movie.myapplication.R;
import com.movie.myapplication.model.Attendees;
/*

   Put attendee data to ListView

*/

public class attendeesAdapter extends ArrayAdapter<Attendees> {

    private Context context;
    private Attendees[] attendees;

    public attendeesAdapter(Context context, Attendees[] attendees) {
        super(context, 0, attendees);
        this.context = context;
        this.attendees = attendees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Attendees attendee = attendees[position];

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendees_item, parent, false);
        }

        TextView nameTxt = convertView.findViewById(R.id.txtName);
        TextView phoneTxt = convertView.findViewById(R.id.txtPhone);

        nameTxt.setText(attendee.getName());
        phoneTxt.setText(attendee.getPhoneNum());

        return convertView;
    }
}
