package com.simonmawole.app.androidnanodegree.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 01-Aug-16.
 */
@ContentProvider(authority = MovieContentProvider.AUTHORITY,
    database = MovieDatabase.class,
    packageName = "com.simonmawole.app.androidnanodegree")
public final class MovieContentProvider {

    private MovieContentProvider(){
    }

    public static final String AUTHORITY =
            "com.simonmawole.app.androidnanodegree";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    interface Path {
        String MOVIE = "movie";
        String POPULAR = "popular";
        String TOP_RATED = "top_rated";
        String FAVORITE = "favorite";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIE) public static class Movie{

        /**
         * Content Uri for getting all movies
         * */
        @ContentUri(
                path = Path.MOVIE,
                type = "vnd.android.cursor.dir/movie"
        ) public static final Uri CONTENT_URI = buildUri(Path.MOVIE);

        /**
         * Content Uri for return single movie using id
         * */
        @InexactContentUri(
                name = "MOVIES",
                path = Path.MOVIE + "/*",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.MOVIE_ID,
                pathSegment = 1
        ) public static Uri withMovieId(String id) {
            return buildUri(Path.MOVIE, String.valueOf(id));
        }

        /**
         * Content Uri for returning popular movies
         * */
        @InexactContentUri(
                name = "POPULAR_MOVIES",
                path = Path.POPULAR + "/*",
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.POPULAR,
                pathSegment = 1
        ) public static Uri popularMovie(String v){
            return buildUri(Path.POPULAR, String.valueOf(v));
        }

        /**
         * Content Uri for returning top rated movies
         * */
        @InexactContentUri(
                name = "TOP_RATED_MOVIES",
                path = Path.TOP_RATED + "/*",
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.TOP_RATED,
                pathSegment = 1
        ) public static Uri topRatedMovie(String v){
            return buildUri(Path.TOP_RATED, String.valueOf(v));
        }

        /**
         * Content Uri for returning favorite movies
         * */
        @InexactContentUri(
                name = "FAVORITE_MOVIES",
                path = Path.FAVORITE + "/*",
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.FAVORITE,
                pathSegment = 1
        ) public static Uri favoriteMovie(String v){
            return buildUri(Path.FAVORITE, String.valueOf(v));
        }

        /**
         * Insert data
         * */
        @NotifyInsert(paths = Path.MOVIE)
        public static Uri[] onInsert(ContentValues values) {
            final String movieId = values.getAsString(MovieColumns.MOVIE_ID);

            return new Uri[]{
                    Movie.withMovieId(movieId),
                    favoriteMovie(movieId),
                    topRatedMovie(movieId),
                    popularMovie(movieId)
            };
        }

        /**
         * Bulk insert data
         * */
        @NotifyBulkInsert(paths = Path.MOVIE)
        public static Uri[] onBulkInsert(Context context, Uri uri,
                                         ContentValues[] values, long[] ids){
            return new Uri[]{
                    uri,
            };
        }

        /**
         * Update data
         * */
        @NotifyUpdate(paths = Path.MOVIE + "/*")
        public static Uri[] onUpdate(Context context, Uri uri, String where,
                                     String[] whereArgs) {
            Cursor c = context.getContentResolver().query(uri, new String[] {
                    MovieColumns.MOVIE_ID,
            }, where, whereArgs, null);
            c.moveToFirst();
            final String movieId2 = c.getString(
                    c.getColumnIndex(MovieColumns.MOVIE_ID));
            c.close();

            return new Uri[]{
                    withMovieId(movieId2),
                    favoriteMovie(movieId2),
                    topRatedMovie(movieId2),
                    popularMovie(movieId2),
            };
        }

        /**
         * Delete data
         * */
        @NotifyDelete(paths = Path.MOVIE + "/*")
        public static Uri[] onDelete(Context context, Uri uri){
            final long movieId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, null,null,null,null);
            c.moveToFirst();
            final String movieId2 = c.getString(c.getColumnIndex(MovieColumns.MOVIE_ID));
            c.close();

            return new Uri[]{
                    withMovieId(movieId2),
                    favoriteMovie(movieId2),
                    topRatedMovie(movieId2),
                    popularMovie(movieId2),
            };
        }
    }

}
