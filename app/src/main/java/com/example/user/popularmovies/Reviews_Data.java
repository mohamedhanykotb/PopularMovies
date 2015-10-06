package com.example.user.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 10/2/2015.
 */
public class Reviews_Data {
    private JSONObject jsondata ;
    private JSONArray all_reviews = null;

    public Reviews_Data (String JsonStr)
            throws JSONException {
        jsondata = new JSONObject(JsonStr);
        if(jsondata.has("results"))
            all_reviews = jsondata.getJSONArray("results");

    }

    private String get_content(JSONObject review )
            throws JSONException {
        String content ;

        if(review.has("content"))
            content = review.getString("content");
        else
            content ="";

        return content;
    }

    public ArrayList<String> get_all_contents ()
    throws JSONException {
        JSONObject review_opject;
        ArrayList<String> output_reviews_array = new ArrayList<String>();

        for (int i = 0; i < all_reviews.length(); i++) {
            review_opject = all_reviews.getJSONObject(i);

            output_reviews_array.add(get_content(review_opject));
            Log.d("TAG56", output_reviews_array.get(i));

        }
        return output_reviews_array;
    }

}
