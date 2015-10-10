package com.example.user.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.user.popularmovies.DATA.Movies_Contract;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class detail_fragment extends Fragment {

    String mtitle=" ";
    Bundle arguments;
    ListView reviews_listView;
    ListView videos_listView;
    ListView details_list ;
    ArrayList<trailer> trailers = new ArrayList<trailer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();
        if (getArguments() != null) {
            mtitle = arguments.getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        details_list= (ListView) rootView.findViewById(R.id.details_list);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String current_Priority = sharedPreferences.getString("Priority",getString(R.string.popularity));
        if(current_Priority.equals(getString(R.string.favorites)) && arguments!=null)
        {
            Uri trailer_uri = Movies_Contract.TRAILERS.buildFavoritesUriById(arguments.getString("id"));
            Cursor cr= getActivity().getContentResolver().query(trailer_uri, null , null , null ,null);

            trailer one_trailer;
            while (cr.moveToNext()) {
                one_trailer = new trailer();
                Log.d("get1", cr.getString(1) +" "+ cr.getString(2)+" " + cr.getString(3));
                one_trailer.setKey(cr.getString(Movies_Contract.TRAILERS.COLUMN_KEY_INDEX));
                one_trailer.setName(cr.getString(Movies_Contract.TRAILERS.CCOLUMN_NAME_INDEX));

                trailers.add(one_trailer);
            }

            Uri review_uri = Movies_Contract.REVIEWS.buildFavoritesUriById(arguments.getString("id"));
            cr= getActivity().getContentResolver().query(review_uri, null , null , null ,null);

            ArrayList<review> reviews = new ArrayList<review>();
            review one_review;
            while (cr.moveToNext()) {
                one_review = new review();
                Log.d("get2", cr.getString(1) +" " + cr.getString(2) +" " + cr.getString(3));
                one_review.setAuthor(cr.getString(Movies_Contract.REVIEWS.CCOLUMN_AUTHOR_INDEX));
                one_review.setContent(cr.getString(Movies_Contract.REVIEWS.COLUMN_CONTENT_INDEX));

                reviews.add(one_review);
            }

            Movie this_movie = new Movie();

            this_movie.setPopularity(arguments.getDouble("pop"));
            this_movie.setDate(arguments.getString("date"));
            this_movie.setPoster_path(arguments.getString("image_path"));
            this_movie.setVote_avrage(arguments.getDouble("vote"));
            this_movie.setId(arguments.getString("id"));
            this_movie.setTitle(arguments.getString("title"));
            this_movie.setOverview(arguments.getString("overview"));

            Trailers_Adapter trailer_adapt = new Trailers_Adapter(getActivity(), this_movie , trailers ,reviews);

            details_list.setAdapter(trailer_adapt);

        }
        else if (arguments != null) {
            WeatherDataTask Weather = new WeatherDataTask();
            Weather.execute("ca");
        }

        details_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  trailer_key = trailers.get(position-1).getKey();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailer_key));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.d("unfounded URL", "unfounded URL");
                }
            }
        });
        return rootView;
    }


    public class WeatherDataTask extends AsyncTask< String, Void ,String[] > {

        String forecastJsonStr = null;

        @Override
        protected String[] doInBackground (String... params){
            URL Review_url;
            URL video_url;

            String ReviewUri = "http://api.themoviedb.org/3/movie/" + arguments.getString("id") + "/reviews?api_key=9282cb2176529336ddc37c73848772b5";
            String VideoUri = "http://api.themoviedb.org/3/movie/" + arguments.getString("id") + "/videos?api_key=9282cb2176529336ddc37c73848772b5";

            // Will contain the raw JSON response as a string.
            try {
                Review_url = new URL(ReviewUri);
                video_url = new URL(VideoUri);

            } catch (IOException e) {

                // If the code didn't successfully get data, there's no point in attemping
                // to parse it.
                return null;
            }

            String[] out = new String[2];
            out[0]=GetJsonStr(Review_url);
            out[1]=GetJsonStr(video_url);
            return out;
        }
private String GetJsonStr(URL url)
{
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    try {
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        forecastJsonStr = buffer.toString();

    } catch (IOException e) {
        Log.e("PlaceholderFragment", "Error ", e);
        // If the code didn't successfully get the weather data, there's no point in attemping
        // to parse it.
        return null;
    } finally{
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e("PlaceholderFragment", "Error closing stream", e);
            }
        }
    }

    return forecastJsonStr;
}

        @Override
        protected void onPostExecute (String[] result)
        {
            if(result != null)
            {
                Log.d("TAG6", "result");

                try {
                     Reviews_Data r_d = new Reviews_Data(result[0]);
                    ArrayList<review> reviews_content =r_d.get_all_contents();

                    VideosData trailer_data = new VideosData(result[1]);

                    trailers=trailer_data.get_all_contents();

                     Movie this_movie = new Movie();

                    this_movie.setPopularity(arguments.getDouble("pop"));
                    this_movie.setDate(arguments.getString("date"));
                    this_movie.setPoster_path(arguments.getString("image_path"));
                    this_movie.setVote_avrage(arguments.getDouble("vote"));
                    this_movie.setId(arguments.getString("id"));
                    this_movie.setTitle(arguments.getString("title"));
                    this_movie.setOverview(arguments.getString("overview"));

                    Trailers_Adapter trailer_adapt = new Trailers_Adapter(getActivity(), this_movie , trailers ,reviews_content);

                    details_list.setAdapter(trailer_adapt);

                      }
                catch (JSONException e) {
                       e.printStackTrace();
                }
            }
        }

    }

}