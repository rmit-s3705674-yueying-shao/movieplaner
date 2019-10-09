package com.movie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.myapplication.R;
import com.movie.myapplication.model.MovieItem;

/*

   Put movie data to ListView

*/

public class movieItemAdapter extends ArrayAdapter<MovieItem> {

    private Context context;
    private MovieItem[] movieItem;

    public movieItemAdapter(Context context, MovieItem[] movieItems) {
        super(context, 0, movieItems);
        this.context = context;
        this.movieItem = movieItems;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItem movie = movieItem[position];

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        }

        ImageView movieImage = convertView.findViewById(R.id.movieImage);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtYear = convertView.findViewById(R.id.txtYear);

        movieImage.setImageResource(movie.getimgID());
        txtTitle.setText(movie.getTitle());
        txtYear.setText(movie.getYear());
        return convertView;
    }
}
