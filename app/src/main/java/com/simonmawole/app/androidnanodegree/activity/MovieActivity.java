package com.simonmawole.app.androidnanodegree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.fragment.MovieDetailFragment;
import com.simonmawole.app.androidnanodegree.fragment.MovieFragment;
import com.simonmawole.app.androidnanodegree.sync.MySyncAdapter;
import com.simonmawole.app.androidnanodegree.utility.Helpers;

/**
 * Created by simon on 3/13/16.
 */
public class MovieActivity extends AppCompatActivity implements MovieFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            Helpers.printLog("TWOPANE","true");
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment())
                        .commit();
                Helpers.printLog("TWOPANE","fragment created");
            }
        } else {
            mTwoPane = false;
            Helpers.printLog("TWOPANE","false");
        }

        MovieFragment movieFragment =  ((MovieFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies));
       // movieFragment.setUseTodayLayout(!mTwoPane);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString("movie_id", id);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
            Helpers.printLog("TWOPANE","detail fragment");
        } else {
            Helpers.printLog("TWOPANE","false activity detail");
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .putExtra("movie_id", id);
            startActivity(intent);
        }
    }
}