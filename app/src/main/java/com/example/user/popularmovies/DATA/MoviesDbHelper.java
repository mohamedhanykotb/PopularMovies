package com.example.user.popularmovies.DATA;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 10/8/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;

    static final String  DATABASE_NAME = "thepopularmovies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + Movies_Contract.Favorites.TABLE_NAME + " (" +
                Movies_Contract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Movies_Contract.Favorites.COLUMN_TITLE + " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_DATE + " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_VOTE_AVERAGE+ " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_LENGTH + " TEXT NOT NULL, " +
                Movies_Contract.Favorites.COLUMN_POPULARITY + " REAL NOT NULL " +
                " );";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + Movies_Contract.TRAILERS.TABLE_NAME + " (" +
                Movies_Contract.TRAILERS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Movies_Contract.TRAILERS.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                Movies_Contract.TRAILERS.COLUMN_NAME + " TEXT NOT NULL, " +
                Movies_Contract.TRAILERS.COLUMN_KEY + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + Movies_Contract.REVIEWS.TABLE_NAME + " (" +
                Movies_Contract.REVIEWS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Movies_Contract.REVIEWS.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                Movies_Contract.REVIEWS.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                Movies_Contract.REVIEWS.COLUMN_CONTENT + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Movies_Contract.Favorites.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Movies_Contract.TRAILERS.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Movies_Contract.REVIEWS.TABLE_NAME);
        onCreate(sqLiteDatabase);

        ContentValues v ;
    }
}
