package com.example.user.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 10/2/2015.
 */
public class VideosData {
    private JSONObject jsondata ;
    private JSONArray all_reviews = null;

    public VideosData (String JsonStr)
            throws JSONException {
        jsondata = new JSONObject(JsonStr);
        if(jsondata.has("results"))
            all_reviews = jsondata.getJSONArray("results");

    }

    private String get_name(JSONObject review )
            throws JSONException {
        String name ;


        if(review.has("name"))
            name = review.getString("name");
        else
            name ="";

        return name;
    }

    private String get_key(JSONObject review )
            throws JSONException {
        String key ;

        if(review.has("key"))
            key = review.getString("key");
        else
            key ="";

        return key;
    }


    public ArrayList<trailer> get_all_contents ()
            throws JSONException {
        JSONObject review_opject;
        ArrayList<trailer> output_reviews_array = new ArrayList<trailer>();
        trailer one_trailer;
        for (int i = 0; i < all_reviews.length(); i++) {
            review_opject = all_reviews.getJSONObject(i);
            one_trailer = new trailer();
            one_trailer.setName(get_name(review_opject));
            one_trailer.setKey(get_key(review_opject));
            Log.d("TAG56", one_trailer.getName());
            output_reviews_array.add(one_trailer);
        }


        return output_reviews_array;
    }
}
