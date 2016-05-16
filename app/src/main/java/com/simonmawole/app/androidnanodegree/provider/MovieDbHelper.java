package com.simonmawole.app.androidnanodegree.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by simon on 5/16/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + MovieContract.PopularMoviesEntry.TABLE_NAME + " (" +
                MovieContract.PopularMoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.PopularMoviesEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_RELEASE_DATE+ " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_REVIEWS + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_VIDEOS + " TEXT, " +
                MovieContract.PopularMoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT);";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + MovieContract.TopRatedEntry.TABLE_NAME + " (" +
                MovieContract.TopRatedEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.TopRatedEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.TopRatedEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_RELEASE_DATE+ " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_REVIEWS + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_VIDEOS + " TEXT, " +
                MovieContract.TopRatedEntry.COLUMN_VOTE_AVERAGE + " TEXT);";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.FavoriteEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE+ " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_REVIEWS + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_VIDEOS + " TEXT, " +
                MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PopularMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TopRatedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
