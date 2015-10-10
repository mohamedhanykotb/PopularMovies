package com.example.user.popularmovies.DATA;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 10/8/2015.
 */
public class Movies_Contract {

    public static final String CONTENT_AUTHORITY = "com.example.user.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_Favorites = "favorites";
    public static final String PATH_trailers = "trailers";
    public static final String PATH_reviews = "reviews";


    public static final class  Favorites implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_Favorites).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Favorites;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Favorites;

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_MOVIE_ID= "movie_id";
        public static final String COLUMN_OVERVIEW= "overview";
        public static final String COLUMN_DATE= "date";
        public static final String COLUMN_POSTER_PATH= "poster_path";
        public static final String COLUMN_VOTE_AVERAGE= "vote_average";
        public static final String COLUMN_LENGTH= "length";
        public static final String COLUMN_POPULARITY = "popularity";

        public static final int COLUMN_TITLE_INDEX= 1;
        public static final int COLUMN_MOVIE_ID_INDEX= 2;
        public static final int COLUMN_OVERVIEW_INDEX= 3;
        public static final int COLUMN_DATE_INDEX= 4;
        public static final int COLUMN_POSTER_PATH_INDEX= 5;
        public static final int COLUMN_VOTE_AVERAGE_INDEX= 6;
        public static final int COLUMN_LENGTH_INDEX= 7;
        public static final int COLUMN_POPULARITY_INDEX = 8;

        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesUriById(String m_id) {
            return CONTENT_URI.buildUpon().appendPath(m_id).build();
        }

    }

    public static final class  TRAILERS implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_trailers).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_trailers;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_trailers;

        public static final String TABLE_NAME = "trailers" +
                "";

        public static final String COLUMN_MOVIE_ID= "movie_id";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_KEY= "key";

        public static final int COLUMN_MOVIE_ID_INDEX= 1;
        public static final int CCOLUMN_NAME_INDEX= 2;
        public static final int COLUMN_KEY_INDEX= 3;


        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesUriById(String m_id) {
            return CONTENT_URI.buildUpon().appendPath(m_id).build();
        }

    }

    public static final class  REVIEWS implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_reviews).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_reviews;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_reviews;

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID= "movie_id";
        public static final String COLUMN_AUTHOR= "author";
        public static final String COLUMN_CONTENT= "content";

        public static final int COLUMN_MOVIE_ID_INDEX= 1;
        public static final int CCOLUMN_AUTHOR_INDEX= 2;
        public static final int COLUMN_CONTENT_INDEX= 3;


        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesUriById(String m_id) {
            return CONTENT_URI.buildUpon().appendPath(m_id).build();
        }

    }

}
