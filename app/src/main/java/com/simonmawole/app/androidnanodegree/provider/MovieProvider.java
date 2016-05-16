package com.simonmawole.app.androidnanodegree.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by simon on 5/16/16.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mOpenHelper;

    private static final int POPULAR = 100;
    private static final int POPULAR_MOVIE_ID = 101;
    private static final int TOP_RATED = 102;
    private static final int TOP_RATED_MOVIE_ID = 103;
    private static final int FAVORITE = 104;
    private static final int FAVORITE_MOVIE_ID = 105;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE + "/#", FAVORITE_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_POPULAR, POPULAR);
        matcher.addURI(authority, MovieContract.PATH_POPULAR + "/#", POPULAR_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_TOP_RATED, TOP_RATED);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED + "/#", TOP_RATED_MOVIE_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POPULAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "popular/*"
            case POPULAR_MOVIE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        MovieContract.PopularMoviesEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case TOP_RATED: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOP_RATED_MOVIE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        MovieContract.TopRatedEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case FAVORITE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_MOVIE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavoriteEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR:
                return MovieContract.PopularMoviesEntry.CONTENT_TYPE;
            case POPULAR_MOVIE_ID:
                return MovieContract.PopularMoviesEntry.CONTENT_ITEM_TYPE;
            case TOP_RATED:
                return MovieContract.TopRatedEntry.CONTENT_TYPE;
            case TOP_RATED_MOVIE_ID:
                return MovieContract.TopRatedEntry.CONTENT_ITEM_TYPE;
            case FAVORITE:
                return MovieContract.FavoriteEntry.CONTENT_TYPE;
            case FAVORITE_MOVIE_ID:
                return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case POPULAR: {
                long _id = db.insert(MovieContract.PopularMoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.PopularMoviesEntry.buildPopularMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOP_RATED: {
                long _id = db.insert(MovieContract.TopRatedEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TopRatedEntry.buildTopRatedUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE: {
                long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case POPULAR:
                rowsDeleted = db.delete(
                        MovieContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED:
                rowsDeleted = db.delete(
                        MovieContract.TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(
                        MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPULAR:
                rowsUpdated = db.update(MovieContract.PopularMoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOP_RATED:
                rowsUpdated = db.update(MovieContract.TopRatedEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(MovieContract.FavoriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
