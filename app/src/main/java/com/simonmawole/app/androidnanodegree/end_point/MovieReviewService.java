package com.simonmawole.app.androidnanodegree.end_point;

import com.simonmawole.app.androidnanodegree.model.MovieReviewModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by simon on 12-Aug-16.
 */
public interface MovieReviewService {

    /**
     * Get movie reviews
     *
     * @param id path the movie id as route path
     * @param k path api key as parameter
     *
     * @return MovieReviewModel
     * */
    @GET("3/movie/{movie_id}/reviews")
    Call<MovieReviewModel> getMovieReview(@Path("movie_id") String id, @Query("api_key") String k);
}
