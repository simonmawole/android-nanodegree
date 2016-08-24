package com.simonmawole.app.androidnanodegree.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.fragment.MovieFragment;
import com.simonmawole.app.androidnanodegree.sync.MySyncAdapter;

/**
 * Created by simon on 3/13/16.
 */
public class MovieActivity extends AppCompatActivity  {

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
        MySyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


}