package com.simonmawole.app.androidnanodegree.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.PopularMoviesAdapter;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.model.MoviesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by simon on 3/13/16.
 */
public class PopularMoviesActivity extends AppCompatActivity {

    OkHttpClient okHttpClient;
    private String popularMoviesUrl = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private String topRatedMoviesUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    RecyclerView rvMovies;
    PopularMoviesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<MoviesModel> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setAutoMeasureEnabled(true);
        rvMovies.setLayoutManager(layoutManager);

        adapter = new PopularMoviesAdapter(this, new ArrayList<MoviesModel>(0));
        rvMovies.setAdapter(adapter);

        new FetchMoviesAsyncTask().execute(popularMoviesUrl+ Developer.MOVIES_API_KEY);
    }

    private class FetchMoviesAsyncTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(params[0])
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.length() != 0){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("results");
                    mList = new ArrayList<>(results.length());
                    for(int i = 0; i < results.length(); i++){
                        MoviesModel model = new MoviesModel();
                        JSONObject jo =results.getJSONObject(i);
                        model.setId(jo.getInt("id"));
                        model.setOriginal_title(jo.getString("original_title"));
                        model.setOverview(jo.getString("overview"));
                        model.setOriginal_language(jo.getString("original_language"));
                        model.setPoster_path(jo.getString("poster_path"));
                        model.setVote_average(jo.getDouble("vote_average"));
                        model.setRelease_date(jo.getString("release_date"));
                        mList.add(model);
                    }

                    adapter = new PopularMoviesAdapter(PopularMoviesActivity.this,mList);
                    rvMovies.swapAdapter(adapter, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}