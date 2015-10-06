package com.example.user.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Created by user on 8/28/2015.
 */
public class MoviesData {

    private JSONObject jsondata ;
    private JSONObject this_movie ;
    private JSONArray all_movies = null;
    ArrayList <Movie> movies_array = new ArrayList<Movie>();
    ArrayList <Movie> buffer = new ArrayList<Movie>();

    public MoviesData (String JsonStr)
            throws JSONException {
        jsondata = new JSONObject(JsonStr);
        if(jsondata.has("results"))
            all_movies = jsondata.getJSONArray("results");
       // Log.d("TAG155", all_movies.length()+" ");
        movies_array = get_all_movies();

        buffer = movies_array;
    }

    private String get_id()
            throws JSONException {
        String id ;

        if(this_movie.has("id"))
            id = this_movie.getString("id");
        else
            id ="";

        return id;
    }

    private String get_title()
            throws JSONException {

        String title ;

        if(this_movie.has("title"))
            title = this_movie.getString("title");
        else
            title ="";

        return title;
    }
    private String get_date()
            throws JSONException {

        String date ;

        if(this_movie.has("release_date"))
            date = this_movie.getString("release_date");
        else
            date ="";

        return date;
    }
    private String get_poster_path()
            throws JSONException {
        String poster_path ;

        if(this_movie.has("poster_path"))
            poster_path = "http://image.tmdb.org/t/p/"+"w185"+this_movie.getString("poster_path");
        else
            poster_path ="";

        return poster_path;
    }

    private double get_popularity()
            throws JSONException {
        double popularity ;

        if(this_movie.has("popularity"))
            popularity = this_movie.getDouble("popularity");
        else
            popularity =0;

        return popularity;
    }

    private double get_vote_avrage()
            throws JSONException {
        double vote_avrage ;

        if(this_movie.has("vote_average"))
            vote_avrage = this_movie.getDouble("vote_average");
        else
            vote_avrage =0;

        return vote_avrage;
    }


    public ArrayList<Movie> get_all_movies ()
    throws JSONException {
        Movie one_movie;
        ArrayList<Movie> output_movies_array = new ArrayList<Movie>();

        for (int i = 0; i < all_movies.length(); i++) {
            one_movie = new Movie();
            this_movie = all_movies.getJSONObject(i);

            one_movie.setTitle(get_title());
            one_movie.setId(get_id());
            one_movie.setDate(get_date());
            one_movie.setPoster_path(get_poster_path());
            one_movie.setVote_avrage(get_vote_avrage());
            one_movie.setPopularity(get_popularity());

            output_movies_array.add(one_movie);

        }
        return output_movies_array;
    }

    public ArrayList<String> get_all_posters ()
    {
        Movie one_movie;
        ArrayList<String> output_array = new ArrayList<>();

        for (int i = 0; i < movies_array.size(); i++) {
            output_array.add(movies_array.get(i).getPoster_path());
            Log.d("vote", movies_array.get(i).getVote_avrage() + " ");
        }

        return output_array;
    }

    public void vote_priority ()
    {
        buffer = movies_array;
        Collections.sort(movies_array);
    }

    public void favorites_priority (String str)
    {
        StringTokenizer st = new StringTokenizer(str, ",");
        Log.d("TAG555", str);
        buffer = new ArrayList<Movie>();
        while (st.hasMoreTokens())
        {
            int id  = Integer.parseInt(st.nextToken());
          //  Log.d("TAG555", String.valueOf(id) + " " + movies_array.size());
            for ( int i=0 ; i< movies_array.size(); i ++) {
             //  Log.d("TAG555", movies_array.get(i).getId() + "  " + String.valueOf(id) );
                if (String.valueOf(id).equals(movies_array.get(i).getId())) {
                    buffer.add(movies_array.get(i));
              //      Log.d("TAG555", "true ");
                }
            }
        }
        movies_array=buffer;
    }

    public void popularity_priority ()
    {
        movies_array = buffer;
    }

}
