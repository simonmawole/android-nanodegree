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
            "com.simonmawole.app.androidnanodegree.MovieContentProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    interface Path {
        String MOVIE = "movie";
        String POPULAR = "popular";
        String TOP_RATED = "top_rated";
        String FAVORITE = "favorite";
        String REVIEW = "review";
        String TRAILER = "trailer";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.REVIEW) public static class Review {

        @MapColumns
        public static Map<String, String> mapColumns(){
            Map<String, String> map = new HashMap<>();
            map.put(MovieReviewColumns.AUTHOR, LIST_REVIEW);
            return map;
        }

        @ContentUri(
                path = Path.REVIEW,
                type = "vnd.android.cursor.dir/list",
                defaultSort = MovieReviewColumns._ID + " ASC"
        ) public static final Uri CONTENT_URI = buildUri(Path.REVIEW);

        @InexactContentUri(
                path = Path.REVIEW + "/#",
                name = "REVIEW_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = MovieReviewColumns.REVIEW_ID,
                pathSegment = 1
        ) public static Uri withId(long id){
            return buildUri(Path.REVIEW, String.valueOf(id));
        }

        static final String LIST_REVIEW = "(SELECT COUNT(*) FROM "
                + MovieDatabase.REVIEW
                + " WHERE "
                + MovieDatabase.REVIEW + "." + MovieReviewColumns.MOVIE_ID
                + "="
                + MovieDatabase.MOVIE + "." + MovieColumns.MOVIE_ID + ")";

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
         * Content Uri for return single movie
         * */
        @InexactContentUri(
                name = "MOVIE",
                path = Path.MOVIE + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.MOVIE_ID,
                pathSegment = 1
        ) public static Uri withId(long id) {
            return buildUri(Path.MOVIE, String.valueOf(id));
        }

        /**
         * Content Uri for returning popular movies
         * */
        @InexactContentUri(
                name = "POPULAR_MOVIE",
                path = Path.MOVIE + "/" + Path.POPULAR,
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.POPULAR,
                pathSegment = 2
        ) public static Uri popularMovie(long v){
            return buildUri(Path.POPULAR, String.valueOf(v));
        }

        /**
         * Content Uri for returning top rated movies
         * */
        @InexactContentUri(
                name = "TOP_RATED_MOVIES",
                path = Path.MOVIE + "/" + Path.TOP_RATED,
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.TOP_RATED,
                pathSegment = 2
        ) public static Uri topRatedMovie(long v){
            return buildUri(Path.TOP_RATED, String.valueOf(v));
        }

        /**
         * Content Uri for returning favorite movies
         * */
        @InexactContentUri(
                name = "FAVORITE_MOVIES",
                path = Path.MOVIE + "/" + Path.FAVORITE,
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieColumns.FAVORITE,
                pathSegment = 2
        ) public static Uri favoriteMovie(long v){
            return buildUri(Path.FAVORITE, String.valueOf(v));
        }

        /**
         * Content Uri for returning movie trailers
         * */
        @InexactContentUri(
                name = "MOVIE_TRAILERS",
                path = Path.MOVIE + "/" + Path.TRAILER,
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieTrailerColumns.MOVIE_ID,
                pathSegment = 2
        ) public static Uri movieTrailer(long id){
            return buildUri(Path.TRAILER, String.valueOf(id));
        }

        /**
         * Content Uri returning movie reviews
         * */
        @InexactContentUri(
                name = "MOVIE_REVIEWS",
                path = Path.MOVIE + "/" + Path.REVIEW,
                type = "vnd.android.cursor.dir/movie",
                whereColumn = MovieReviewColumns.MOVIE_ID,
                pathSegment = 2
        ) public static Uri movieReview(long id){
            return buildUri(Path.REVIEW, String.valueOf(id));
        }

        /**
         * Insert data
         * */
        @NotifyInsert(paths = Path.MOVIE)
        public static Uri[] onInsert(ContentValues values) {
            final long id = values.getAsLong(MovieColumns.MOVIE_ID);
            return new Uri[]{
                    Movie.withId(id),
                    movieReview(id),
                    movieTrailer(id),
                    favoriteMovie(id),
                    topRatedMovie(id),
                    popularMovie(id)
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
        @NotifyUpdate(paths = Path.MOVIE + "/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where,
                                     String[] whereArgs) {
            final long movieId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, new String[] {
                    MovieColumns.MOVIE_ID,
            }, null, null, null);
            c.moveToFirst();
            final long movieId2 = c.getLong(
                    c.getColumnIndex(MovieColumns.MOVIE_ID));
            c.close();

            return new Uri[]{
                    withId(movieId),
                    movieReview(movieId2),
                    movieTrailer(movieId2),
                    favoriteMovie(movieId2),
                    topRatedMovie(movieId2),
                    popularMovie(movieId2),
            };
        }

        /**
         * Delete data
         * */
        @NotifyDelete(paths = Path.MOVIE + "/#")
        public static Uri[] onDelete(Context context, Uri uri){
            final long movieId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, null,null,null,null);
            c.moveToFirst();
            final long movieId2 = c.getLong(c.getColumnIndex(MovieColumns.MOVIE_ID));
            c.close();

            return new Uri[]{
                    withId(movieId),
                    movieReview(movieId2),
                    movieTrailer(movieId2),
                    favoriteMovie(movieId2),
                    topRatedMovie(movieId2),
                    popularMovie(movieId2),
            };
        }
    }

}
