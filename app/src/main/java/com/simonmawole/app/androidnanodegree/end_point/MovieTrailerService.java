package com.simonmawole.app.androidnanodegree.end_point;

import com.simonmawole.app.androidnanodegree.model.MovieTrailerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by simon on 12-Aug-16.
 */
public interface MovieTrailerService {

    /**
     * Get movie trailers
     *
     * @param id path movie id as route path
     * @param k path api key as parameter
     *
     * @return MovieTrailerModel
     * */
    @GET("3/movie/{movie_id}/videos")
    Call<MovieTrailerModel> getMovieTrailer(@Path("movie_id") String id, @Query("api_key") String k);
}
