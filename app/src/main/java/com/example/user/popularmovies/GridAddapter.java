package com.example.user.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 8/28/2015.
 */
public class GridAddapter extends BaseAdapter {

    ArrayList<String> weak_basic_data ;
    Context context ;
    LayoutInflater inflater ;

    public GridAddapter(Context context , ArrayList<String> weak_basic_data  )
    {
        this.context = context ;
        this.weak_basic_data = weak_basic_data ;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount ()
    {
        return weak_basic_data.size();
    }

    @Override
    public Object getItem (int position )
    {
        return weak_basic_data.get(position);
    }

    @Override
    public long getItemId (int position )
    {
        return position;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent )
    {
        View item_view = convertView ;
        if(item_view==null)
            item_view = inflater.inflate(R.layout.grid_item , null);


        ImageView grid_image = (ImageView)item_view.findViewById(R.id.imageView);

        Picasso.with(context).load(weak_basic_data.get(position)).into(grid_image);




        return  item_view;
    }

}
