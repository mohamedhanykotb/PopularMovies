package com.example.user.popularmovies.DATA;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


/**
 * Created by user on 10/9/2015.
 */
public class MoviesProvider extends ContentProvider {

    static final int VAVORAITS_MOVIES = 10;
    static final int VAVORAIT_MOVIE_WITH_MOVIE_ID = 11;
    static final int MOVIES_TRAILERS = 20;
    static final int TRAILERS_WITH_ID = 21;
    static final int MOVIES_RVIEWS = 30;
    static final int RVIEWS_WITH_ID = 31;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIES_WITH_MOVIE_ID = Movies_Contract.Favorites.COLUMN_MOVIE_ID + " = ? ";
    private static final String TRAILERS_WITH_MOVIE_ID = Movies_Contract.TRAILERS.COLUMN_MOVIE_ID + " = ? ";
    private static final String REVIEWS_WITH_MOVIE_ID = Movies_Contract.REVIEWS.COLUMN_MOVIE_ID + " = ? ";
    private MoviesDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VAVORAITS_MOVIES:
                return Movies_Contract.Favorites.CONTENT_TYPE;

            case VAVORAIT_MOVIE_WITH_MOVIE_ID:
                return Movies_Contract.Favorites.CONTENT_ITEM_TYPE;

            case MOVIES_TRAILERS:
                return Movies_Contract.TRAILERS.CONTENT_TYPE;

            case MOVIES_RVIEWS:
                return Movies_Contract.REVIEWS.CONTENT_TYPE;

            case TRAILERS_WITH_ID:
                return Movies_Contract.TRAILERS.CONTENT_ITEM_TYPE;

            case RVIEWS_WITH_ID:
                return Movies_Contract.REVIEWS.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VAVORAITS_MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        Movies_Contract.Favorites.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VAVORAIT_MOVIE_WITH_MOVIE_ID:
                String id = uri.getPathSegments().get(1);

                retCursor = mOpenHelper.getReadableDatabase().query(
                        Movies_Contract.Favorites.TABLE_NAME,
                        projection,
                        MOVIES_WITH_MOVIE_ID,
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );

                break;

            case TRAILERS_WITH_ID:
                String id1 = uri.getPathSegments().get(1);

                retCursor = mOpenHelper.getReadableDatabase().query(
                        Movies_Contract.TRAILERS.TABLE_NAME,
                        projection,
                        TRAILERS_WITH_MOVIE_ID,
                        new String[]{id1},
                        null,
                        null,
                        sortOrder
                );

            break;


            case RVIEWS_WITH_ID:
                String id2 = uri.getPathSegments().get(1);

                retCursor = mOpenHelper.getReadableDatabase().query(
                        Movies_Contract.REVIEWS.TABLE_NAME,
                        projection,
                        REVIEWS_WITH_MOVIE_ID,
                        new String[]{id2},
                        null,
                        null,
                        sortOrder
                );

            break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        Log.d("TAGdp", match+" ");
        switch (match) {
            case VAVORAITS_MOVIES:
                long _id = db.insert(Movies_Contract.Favorites.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = Movies_Contract.Favorites.buildFavoritesUriById(String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            case MOVIES_TRAILERS:
                long _id1 = db.insert(Movies_Contract.TRAILERS.TABLE_NAME, null, values);
                if (_id1 > 0)
                    returnUri = Movies_Contract.TRAILERS.buildFavoritesUriById(String.valueOf(_id1));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            case MOVIES_RVIEWS:
                long _id2 = db.insert(Movies_Contract.REVIEWS.TABLE_NAME, null, values);
                if (_id2 > 0)
                    returnUri = Movies_Contract.REVIEWS.buildFavoritesUriById(String.valueOf(_id2));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;


            default:
                Log.d("TAGdp", match+" ");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted =-1;

        if ( null == selection ) selection = "1";
        switch (match) {
            case VAVORAITS_MOVIES: {
                rowsDeleted = db.delete(Movies_Contract.Favorites.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case VAVORAIT_MOVIE_WITH_MOVIE_ID: {
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(Movies_Contract.Favorites.TABLE_NAME, TRAILERS_WITH_MOVIE_ID,new  String[]{id});
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = -1;

        return rowsUpdated;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Movies_Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Movies_Contract.PATH_Favorites, VAVORAITS_MOVIES);

        matcher.addURI(authority, Movies_Contract.PATH_Favorites + "/#", VAVORAIT_MOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, Movies_Contract.PATH_trailers, MOVIES_TRAILERS);

        matcher.addURI(authority, Movies_Contract.PATH_trailers + "/#", TRAILERS_WITH_ID);

        matcher.addURI(authority, Movies_Contract.PATH_reviews, MOVIES_RVIEWS);

        matcher.addURI(authority, Movies_Contract.PATH_reviews + "/#", RVIEWS_WITH_ID);

        return matcher;
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
