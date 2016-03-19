package com.simonmawole.app.androidnanodegree.activity;

import android.content.Context;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.model.MoviesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by simon on 3/19/16.
 */
public interface MoviesDb {
    @GET("3/movie/{category}?api_key={key}")
    Call<List<MoviesModel>> listMoviesDB(@Path("category") String category,
                                         @Path("key") String key);
}
