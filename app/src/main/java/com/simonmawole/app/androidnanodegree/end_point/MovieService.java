package com.simonmawole.app.androidnanodegree.end_point;

import com.simonmawole.app.androidnanodegree.model.MovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by simon on 31-Jul-16.
 */
public interface MovieService {

    /**
     * Get movies
     *
     * @param c path the movie category as route path
     * @param k path api key as parameter
     *
     * @return MovieModel
     * */
    @GET("3/movie/{category}")
    Call<MovieModel> getMovies(@Path("category") String c, @Query("api_key") String k);

}
