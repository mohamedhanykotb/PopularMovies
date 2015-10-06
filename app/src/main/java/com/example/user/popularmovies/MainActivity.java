package com.example.user.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        inflater = LayoutInflater.from(this);
        View item_view = inflater.inflate(R.layout.grid_item , null);
        grid_image = (ImageView)item_view.findViewById(R.id.imageView);
        grid_image.setImageResource(R.drawable.icon);

        my_grid = (GridView)findViewById(R.id.gridview);

        this_con = this;

        current_Priority = sharedPreferences.getString("Priority",getString(R.string.popularity));
        WeatherDataTask  WeatherData = new WeatherDataTask();
        WeatherData.execute(build_uri(current_Priority));

        Log.d("onCreate", current_Priority + "  " +build_uri(current_Priority));

        intent = new Intent(this , detail.class);
        final Context this_activity = this;

        my_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = movies_data.movies_array.get(position).getTitle();
                String movie_id = movies_data.movies_array.get(position).getId();
                String date = movies_data.movies_array.get(position).getDate();
                String image_path = movies_data.movies_array.get(position).getPoster_path();
                double vote = (movies_data.movies_array.get(position).getVote_avrage());
                double pop = (movies_data.movies_array.get(position).getPopularity());
                int duration = Toast.LENGTH_SHORT;


                Bundle args = new Bundle();
                args.putString("title", movies_data.movies_array.get(position).getTitle());
                args.putString("date",date);
                args.putString("image_path",image_path);
                args.putDouble("vote", vote);
                args.putDouble("pop",pop);
                args.putString("id",movie_id);


                if(mTwoPane)
                {
                    detail_fragment fragment = new detail_fragment();
                    fragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                            .commit();
                }
                else {
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
        Log.d("tagq", "start");
        if (current_Priority != null)
            if(!current_Priority.equals(sharedPreferences.getString("Priority",getString(R.string.popularity)))) {
                Log.d("onstart",current_Priority);
                current_Priority=sharedPreferences.getString("Priority", getString(R.string.popularity));
                Log.d("onstart",current_Priority);
                WeatherDataTask WeatherData = new WeatherDataTask();
                WeatherData.execute(build_uri(current_Priority));
                Log.d("onCreate", build_uri(current_Priority));
            }
    }

    private String build_uri(String piriorty)
    {

        final String FORECAST_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
        final String sort = "sort_by";
        final String key = "api_key";

        Uri builtUri;

        Log.d("uri", piriorty + "  " + getString(R.string.favorites));

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

    public class WeatherDataTask extends AsyncTask< String , Void ,String > {

        private String format = "json";
        private String units = "metric";
        private int numDays = 7;
        String forecastJsonStr = null;

        @Override
        protected String doInBackground (String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.

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
