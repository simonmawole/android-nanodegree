package com.simonmawole.app.androidnanodegree.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.load.model.Headers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.MovieAdapter;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.end_point.MovieService;
import com.simonmawole.app.androidnanodegree.fragment.MovieDetailFragment;
import com.simonmawole.app.androidnanodegree.fragment.MovieFragment;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.sync.SyncAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simon on 3/13/16.
 */
public class MovieActivity extends AppCompatActivity implements MovieFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        //Initialize sync adapter
        SyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String date) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            MovieDetailFragment fragment = new MovieDetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movies, fragment)
                    .commit();
        } else {

        }
    }


}