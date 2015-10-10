package com.example.user.popularmovies;

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

    private String get_author(JSONObject review )
            throws JSONException {
        String author ;

        if(review.has("author"))
            author = review.getString("author");
        else
            author ="";

        return author;
    }

    public ArrayList<review> get_all_contents ()
            throws JSONException {
        JSONObject review_opject;
        ArrayList<review> output_reviews_array = new ArrayList<review>();
        review one_review;
        for (int i = 0; i < all_reviews.length(); i++) {
            review_opject = all_reviews.getJSONObject(i);
            one_review = new review();
            one_review.setContent(get_content(review_opject));
            one_review.setAuthor(get_author(review_opject));

            output_reviews_array.add(one_review);
        }


        return output_reviews_array;
    }

}
