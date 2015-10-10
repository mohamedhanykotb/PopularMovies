package com.example.user.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.popularmovies.DATA.Movies_Contract;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView grid_image;
    GridView my_grid;
    LayoutInflater inflater;
    Intent intent;
    Context this_con ;
    MoviesData movies_data;
    ArrayList<Movie> movies_data_arr;
    ArrayList<String> posters_arr;
    String current_Priority;
    SharedPreferences sharedPreferences ;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static boolean mTwoPane =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new detail_fragment(), DETAILFRAGMENT_TAG).commit();
            }
        } else {
            mTwoPane = false;
        }

        inflater = LayoutInflater.from(this);
        View item_view = inflater.inflate(R.layout.grid_item , null);
        grid_image = (ImageView)item_view.findViewById(R.id.imageView);
        grid_image.setImageResource(R.drawable.icon);

        my_grid = (GridView)findViewById(R.id.gridview);

        this_con = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        current_Priority = sharedPreferences.getString("Priority",getString(R.string.popularity));
        if(current_Priority.equals(getString(R.string.favorites)))
        {
            Uri favoraits_uri = Movies_Contract.Favorites.CONTENT_URI;
            Cursor cr= this.getContentResolver().query(favoraits_uri, null , null , null ,null);
            getFromDpToView(cr);
        }
        else
        {
            WeatherDataTask WeatherData = new WeatherDataTask();
            WeatherData.execute(build_uri(current_Priority));
        }

        intent = new Intent(this , detail.class);
        final Context this_activity = this;

        my_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = movies_data_arr.get(position).getTitle();
                String movie_id = movies_data_arr.get(position).getId();
                String date = movies_data_arr.get(position).getDate();
                String image_path =movies_data_arr.get(position).getPoster_path();
                String overview = movies_data_arr.get(position).getOverview();
                double vote = (movies_data_arr.get(position).getVote_avrage());
                double pop = (movies_data_arr.get(position).getPopularity());

                Bundle args = new Bundle();
                args.putString("title", title);
                args.putString("date",date);
                args.putString("image_path",image_path);
                args.putDouble("vote", vote);
                args.putDouble("pop",pop);
                args.putString("id",movie_id);
                args.putString("overview",overview);

                if(mTwoPane)
                {
                    detail_fragment fragment = new detail_fragment();
                    fragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                            .commit();
                }
                else {
                    int duration = Toast.LENGTH_SHORT ;
                    Toast toast = Toast.makeText(this_activity, title, duration);
                    toast.show();
                    Intent intent = new Intent(this_activity, detail.class).putExtras(args);
                    startActivity(intent);
                }

            }
        });
        Log.d("tagq", "create");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this , settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (current_Priority != null)
            if(!current_Priority.equals(sharedPreferences.getString("Priority",getString(R.string.popularity)))) {
                Log.d("onstart",current_Priority);
                current_Priority=sharedPreferences.getString("Priority", getString(R.string.popularity));
                Log.d("onstart",current_Priority);
                if(current_Priority.equals(getString(R.string.favorites)))
                {
                    Uri favoraits_uri = Movies_Contract.Favorites.CONTENT_URI;
                    Cursor cr= this.getContentResolver().query(favoraits_uri, null , null , null ,null);
                    getFromDpToView(cr);
                }
                else
                {
                    WeatherDataTask WeatherData = new WeatherDataTask();
                    WeatherData.execute(build_uri(current_Priority));
                }
            }
    }

    private String build_uri(String piriorty)
    {
        final String FORECAST_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
        final String sort = "sort_by";
        final String key = "api_key";
        Uri builtUri;

        if(piriorty.equals(getString(R.string.favorites)) ) {
            builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(key, "9282cb2176529336ddc37c73848772b5").build();
        }
        else
        {
            Log.d("TAG2",piriorty );
            builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(sort, piriorty + ".desc")
                    .appendQueryParameter(key, "9282cb2176529336ddc37c73848772b5").build();
        }

        return builtUri.toString();
    }

    private void getFromDpToView (Cursor cr)
    {
        Movie one_movie;
        posters_arr = new ArrayList<String>();
        movies_data_arr = new ArrayList<Movie>();

        while (cr.moveToNext()) {
            one_movie = new Movie();
            Log.d("get" , cr.getString(0)+cr.getString(1)+cr.getString(2)+cr.getString(3)+cr.getString(4)+cr.getString(5)+cr.getString(6)  + cr.getString(7));
            one_movie.setOverview(cr.getString(Movies_Contract.Favorites.COLUMN_OVERVIEW_INDEX));
            one_movie.setVote_avrage(cr.getDouble(Movies_Contract.Favorites.COLUMN_VOTE_AVERAGE_INDEX));
            one_movie.setPoster_path(cr.getString(Movies_Contract.Favorites.COLUMN_POSTER_PATH_INDEX));
            one_movie.setPopularity(cr.getDouble(Movies_Contract.Favorites.COLUMN_POPULARITY_INDEX));
            one_movie.setDate(cr.getString(Movies_Contract.Favorites.COLUMN_DATE_INDEX));
            one_movie.setTitle(cr.getString(Movies_Contract.Favorites.COLUMN_TITLE_INDEX));
            one_movie.setId(cr.getString(Movies_Contract.Favorites.COLUMN_MOVIE_ID_INDEX));
            one_movie.setPoster_path(cr.getString(Movies_Contract.Favorites.COLUMN_POSTER_PATH_INDEX));

            posters_arr.add(cr.getString(Movies_Contract.Favorites.COLUMN_POSTER_PATH_INDEX));

            movies_data_arr.add(one_movie);
        }
        GridAddapter myadapter = new GridAddapter(this , posters_arr );
        my_grid.setAdapter(myadapter);
    }

    public class WeatherDataTask extends AsyncTask< String , Void ,String > {
        String forecastJsonStr = null;

        @Override
        protected String doInBackground (String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);

                Log.d("tagq", "load");

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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing) h
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
        protected void onPostExecute (String result)
        {
            Log.d("TAG", "is");
            if(result != null)
            {
                Log.d("TAG", "is");
                try {
                    movies_data = new MoviesData(result);

                    Log.d("TAG1", current_Priority);
                    if(current_Priority.equals(getString(R.string.favorites))) {
                        Log.d("TAG2", current_Priority);
                        movies_data.favorites_priority(sharedPreferences.getString("favorites", "1,"));
                        SharedPreferences fv_sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Log.d("TAG23" , fv_sh.getString("favorites", "1,"));
                    }

                    movies_data_arr = movies_data.get_all_movies();
                    ArrayList<String> listdata = movies_data.get_all_posters();
                    Log.d("TAG2", listdata.size() + " ");
                    GridAddapter myadapter = new GridAddapter(this_con , listdata );
                    my_grid.setAdapter(myadapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


    }
}
