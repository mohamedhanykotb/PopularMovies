package com.example.user.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmovies.DATA.Movies_Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 10/3/2015.
 */
public class DetailsListAdapter extends BaseAdapter {

    private ArrayList<trailer> trailers_data ;
    private Movie details;
    private ArrayList<review> reviews_data;
    private Context context ;
    private LayoutInflater inflater ;
    private int Num_trailers , Num_reviews;

    private View details_view, review_view , trailer_view ;


    public DetailsListAdapter(Context context , Movie details , ArrayList<trailer> trailers_data , ArrayList<review> reviews_data )
    {
        this.context = context ;
        this.trailers_data = trailers_data ;
        this.details=details;
        this.reviews_data=reviews_data;
        Num_trailers = trailers_data.size();
        Num_reviews = reviews_data.size();
        inflater = LayoutInflater.from(context);
        details_view = inflater.inflate(R.layout.datails_item, null);
        review_view =  inflater.inflate(R.layout.review_list_item, null);
        trailer_view =  inflater.inflate(R.layout.trailer_list_item, null);
    }

    @Override
    public int getCount ()
    {
        return Num_trailers+Num_reviews+1;
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
            if (rootView != inflater.inflate(R.layout.datails_item, null))
                rootView = inflater.inflate(R.layout.datails_item, null);

            TextView title = (TextView)rootView.findViewById(R.id.title);
            TextView date = (TextView)rootView.findViewById(R.id.date);
            TextView pop = (TextView)rootView.findViewById(R.id.textView);
            TextView vote = (TextView)rootView.findViewById(R.id.textView3);
            TextView overview = (TextView)rootView.findViewById(R.id.overview);
            ImageView image = (ImageView)rootView.findViewById(R.id.imageView2);

            title.setText(details.getTitle());
            date.setText(details.getDate());
            vote.setText(details.getVote_avrage() + "/10");
            pop.setText(details.getPopularity() + "");
            overview.setText(details.getOverview());
            Picasso.with(context).load(details.getPoster_path()).into(image);

            final Button StarButton = (Button)rootView.findViewById(R.id.button);
            if(exist_in_favoraites())
                StarButton.setText("Delet");

            StarButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    if (exist_in_favoraites()) {
                        Uri favoraits_uri = Movies_Contract.Favorites.buildFavoritesUriById(details.getId());
                        context.getContentResolver().delete(favoraits_uri, null, null);
                        StarButton.setText("Star");
                    } else {
                        set_in_dp();
                        StarButton.setText("Delet");
                    }
                }
            });

            if(Num_trailers<1)
            {
                TextView sub_title = (TextView)rootView.findViewById(R.id.trailers);
                sub_title.setVisibility(View.GONE);
            }


            rootView.setOnClickListener(null);

        } else if(position<=Num_trailers)
        {
            if(rootView!=inflater.inflate(R.layout.trailer_list_item, null))
                rootView = inflater.inflate(R.layout.trailer_list_item, null);

            TextView trailer_name = (TextView)rootView.findViewById(R.id.name_textview);
            trailer_name.setText(trailers_data.get(position-1).getName()+" ");

        }

        else if (position >Num_trailers && position <=Num_trailers+Num_reviews) {

            if(rootView!=inflater.inflate(R.layout.review_list_item, null))
                rootView = inflater.inflate(R.layout.review_list_item, null);

            int Inner_position = position - Num_trailers-1;

            TextView review = (TextView)rootView.findViewById(R.id.review_textview);
            review.setText(reviews_data.get(Inner_position).getContent() + " ");

            TextView author = (TextView)rootView.findViewById(R.id.author_textview);
            author.setText(reviews_data.get(Inner_position).getAuthor() + " :");

            if(Num_reviews!=0 && Inner_position == 0)
            {
                TextView sub_title = (TextView)rootView.findViewById(R.id.reviews);
                sub_title.setVisibility(View.VISIBLE);
            }


            rootView.setOnClickListener(null);
        }


        return  rootView;
    }

    private boolean exist_in_favoraites()
    {
        boolean exist_in_favoraites = false;

        Uri favoraits_uri = Movies_Contract.Favorites.CONTENT_URI;
        Cursor cr= context.getContentResolver().query(favoraits_uri, null, null, null, null);
        while (cr.moveToNext())
            if(details.getId().equals(cr.getString(Movies_Contract.Favorites.COLUMN_MOVIE_ID_INDEX))){
                exist_in_favoraites = true;
                break;
            }
        return exist_in_favoraites;
    }

    private void set_in_dp(){

        Uri favoraits_uri = Movies_Contract.Favorites.CONTENT_URI;

        ContentValues cv = new ContentValues();
        cv.put(Movies_Contract.Favorites.COLUMN_DATE, details.getDate());
        cv.put(Movies_Contract.Favorites.COLUMN_LENGTH, "120 ");
        cv.put(Movies_Contract.Favorites.COLUMN_MOVIE_ID, details.getId());
        cv.put(Movies_Contract.Favorites.COLUMN_OVERVIEW, details.getOverview());
        cv.put(Movies_Contract.Favorites.COLUMN_POPULARITY, details.getPopularity());
        cv.put(Movies_Contract.Favorites.COLUMN_POSTER_PATH, details.getPoster_path());
        cv.put(Movies_Contract.Favorites.COLUMN_TITLE, details.getTitle());
        cv.put(Movies_Contract.Favorites.COLUMN_VOTE_AVERAGE, details.getVote_avrage());

        context.getContentResolver().insert(favoraits_uri, cv);

        Uri trailers_uri = Movies_Contract.TRAILERS.CONTENT_URI;
        for (int i = 0; i < trailers_data.size(); i++) {
            cv = new ContentValues();
            cv.put(Movies_Contract.TRAILERS.COLUMN_MOVIE_ID, details.getId());
            cv.put(Movies_Contract.TRAILERS.COLUMN_KEY, trailers_data.get(i).getKey());
            cv.put(Movies_Contract.TRAILERS.COLUMN_NAME, trailers_data.get(i).getName());

            context.getContentResolver().insert(trailers_uri, cv);
        }

        Uri reviews_uri = Movies_Contract.REVIEWS.CONTENT_URI;
        for (int i = 0; i < reviews_data.size(); i++) {
            cv = new ContentValues();
            cv.put(Movies_Contract.REVIEWS.COLUMN_MOVIE_ID, details.getId());
            cv.put(Movies_Contract.REVIEWS.COLUMN_AUTHOR, reviews_data.get(i).getAuthor());
            cv.put(Movies_Contract.REVIEWS.COLUMN_CONTENT, reviews_data.get(i).getContent());

            context.getContentResolver().insert(reviews_uri, cv);
        }
    }
}

