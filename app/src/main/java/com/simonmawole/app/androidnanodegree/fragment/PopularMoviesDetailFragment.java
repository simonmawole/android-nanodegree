package com.simonmawole.app.androidnanodegree.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.PopularMoviesAdapter;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.model.MoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simon on 5/16/16.
 */
public class PopularMoviesDetailFragment extends Fragment {

    private String urlImage = "http://image.tmdb.org/t/p/w500/";
    private Bundle bundle;

    //Binding
    @Bind(R.id.ivMoviePoster)
    ImageView ivPoster;
    @Bind(R.id.tvMovieTitle)
    TextView tvTitle;
    @Bind(R.id.tvLanguage) TextView tvLanguage;
    @Bind(R.id.tvReleaseDate) TextView tvReleaseDate;
    @Bind(R.id.tvVoteAverage) TextView tvRating;
    @Bind(R.id.tvSynopsis) TextView tvSynopsis;
    private ShareActionProvider mShareActionProvider;

    private static final String LOG_TAG = PopularMoviesDetailFragment.class.getSimpleName();

    private static final String POPULAR_MOVIES_SHARE_HASHTAG = " #PopularMovies";

    private static final String POPULAR_MOVIES_KEY = "movies";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_popular_movies_detail,container,false);

        ButterKnife.bind(getActivity());

        bundle = getArguments();

        tvSynopsis.setText(bundle.getString("overview"));
        tvTitle.setText(bundle.getString("title"));
        tvReleaseDate.setText(bundle.getString("release_date"));
        tvLanguage.setText(bundle.getString("language"));
        tvRating.setText(String.valueOf(bundle.getDouble("rating"))+" / 10");
        Picasso.with(getActivity())
                .load(urlImage+bundle.getString("poster")+"?api_key="+ Developer.MOVIES_API_KEY)
                .into(ivPoster);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(DetailActivity.DATE_KEY) &&
                mLocation != null &&
                !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_popular_movies_detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(DetailActivity.DATE_KEY)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";

        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                mLocation, mDateStr);

        Log.e("LOG LOG",weatherForLocationUri.toString());
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("LOG LOG",String.valueOf(data.getCount()));
        if (data != null && data.moveToFirst()) {
            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
