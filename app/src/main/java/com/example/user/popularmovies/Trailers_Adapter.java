package com.example.user.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 10/3/2015.
 */
public class Trailers_Adapter extends BaseAdapter {

    ArrayList<trailer> trailers_data ;
    Movie details;
    ArrayList<String> reviews_data;
    Context context ;
    LayoutInflater inflater ;

    public Trailers_Adapter(Context context , Movie details , ArrayList<trailer> trailers_data , ArrayList<String> reviews_data )
    {
        this.context = context ;
        this.trailers_data = trailers_data ;
        this.details=details;
        this.reviews_data=reviews_data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount ()
    {
        return trailers_data.size()+reviews_data.size()+1;
    }

    @Override
    public Object getItem (int position )
    {
        return 1;
    }

    @Override
    public long getItemId (int position )
    {
        return position;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent )
    {
        View rootView = convertView ;

        if(position==0) {
            if (rootView == null)
                rootView = inflater.inflate(R.layout.datails_item, null);

            TextView title = (TextView)rootView.findViewById(R.id.title);
            TextView date = (TextView)rootView.findViewById(R.id.date);
            TextView pop = (TextView)rootView.findViewById(R.id.textView);
            TextView vote = (TextView)rootView.findViewById(R.id.textView3);
            ImageView image = (ImageView)rootView.findViewById(R.id.imageView2);
            ImageView vote_image = (ImageView)rootView.findViewById(R.id.vote_image);

            title.setText(details.getTitle());
            date.setText(details.getDate());
            vote.setText(details.getVote_avrage() + "");
            pop.setText(details.getPopularity()+"");


            //vote_image my_vote_image = new vote_image(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
            //vote_image.setImageBitmap(my_vote_image.get_image(Double.valueOf(arguments.getString("vote")), 10));

             Picasso.with(context).load(details.getPoster_path()).into(image);

            Button StarButton = (Button)rootView.findViewById(R.id.button);
            StarButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences fv_sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

                    SharedPreferences.Editor editor = fv_sh.edit();
                    String str = fv_sh.getString("favorites", ",");
                    Log.d("TAG4", str + " 2");
                    StringBuilder fv = new StringBuilder(str);
                    fv.append(details.getId()).append(",");
                    Log.d("TAG4", fv.toString());

                    editor.putString("favorites", fv.toString());
                    editor.commit();

                    Log.d("TAG4", fv_sh.getString("favorites", ",1 "));
                }
            });

            rootView.setEnabled(false);
            rootView.setOnClickListener(null);

        } else if(position<=trailers_data.size())
        {
            if (rootView == null)
                rootView = inflater.inflate(R.layout.trailer_list_item, null);
            TextView trailer_name = (TextView)rootView.findViewById(R.id.name_textview);

            trailer_name.setText(trailers_data.get(position-1).getName());

        }

        else {
            if (rootView == null)
                rootView = inflater.inflate(R.layout.review_list_item, null);

            TextView review = (TextView)rootView.findViewById(R.id.review_textview);
            review.setText(reviews_data.get(position - trailers_data.size()-1));

            rootView.setEnabled(false);
            rootView.setOnClickListener(null);
        }


        return  rootView;
    }

}
