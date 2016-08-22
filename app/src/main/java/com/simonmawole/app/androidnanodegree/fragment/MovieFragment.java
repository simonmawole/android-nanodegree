package com.simonmawole.app.androidnanodegree.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.MovieAdapter;
import com.simonmawole.app.androidnanodegree.data.MovieColumns;
import com.simonmawole.app.androidnanodegree.data.MovieContentProvider;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.end_point.MovieService;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.utility.Helpers;
import com.simonmawole.app.androidnanodegree.utility.NetworkInterceptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simon on 5/16/16.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MovieModel.MovieResult> mList;

    private Gson gson;
    private Retrofit retrofit;
    private MovieService movieService;
    private Call<MovieModel> call;

    //Binding
    @BindView(R.id.rvMovies) RecyclerView rvMovies;
 //   @BindView(R.id.srlMovies) SwipeRefreshLayout srlMovies;
    @BindView(R.id.pbLoadingProgress) ProgressBar progressBar;
    @BindView(R.id.tvMessage) TextView tvMessage;

    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";

    private static final int FORECAST_LOADER = 0;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menu_most_popular:
                fetchMovies("popular");
                getActivity().setTitle(R.string.most_popular);
                break;
            case R.id.menu_top_rated:
                fetchMovies("top_rated");
                getActivity().setTitle(R.string.top_rated);
                break;
            case R.id.menu_favorite:
                Helpers.showToast(getActivity(), "Favorite");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        ButterKnife.bind(this,rootView);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setAutoMeasureEnabled(true);
        rvMovies.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(getActivity(), mList);
        rvMovies.setAdapter(adapter);

        fetchMovies("popular");

        getActivity().setTitle(R.string.most_popular);

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateWeather() {
        //SyncAdapter.syncImmediately(getActivity());
    }



    @Override
    public void onResume() {
        super.onResume();
        /*if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        /*mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
                mLocation, startDate);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                null
        );*/
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /*mForecastAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // mForecastAdapter.swapCursor(null);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        /*mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }*/
    }

    public void fetchMovies(final String category){
        if(Helpers.isConnected(getActivity())){
            //Show the progress bar
            progressBar.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
            tvMessage.setVisibility(View.GONE);

            gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            movieService = retrofit.create(MovieService.class);

            call = movieService.getMovies(category, Developer.MOVIES_API_KEY);

            call.enqueue(new retrofit2.Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    try {
                        mList = response.body().results;

                        adapter = new MovieAdapter(getActivity(), mList);
                        rvMovies.swapAdapter(adapter, false);

                        //Show recycler
                        progressBar.setVisibility(View.GONE);
                        rvMovies.setVisibility(View.VISIBLE);
                        tvMessage.setVisibility(View.GONE);

                        //Add to database
                        for(int i = 0; i < mList.size(); i++){
                            MovieModel.MovieResult model = mList.get(i);

                            ContentValues values = new ContentValues();
                            values.put("movie_id", model.id);
                            values.put("overview", model.overview);
                            values.put("original_language", model.original_language);
                            values.put("original_title", model.original_title);
                            values.put("poster_path", model.poster_path);
                            values.put("release_date", model.release_date);
                            values.put("vote_average", model.vote_average);
                            if(category.equalsIgnoreCase("popular")){
                                values.put("popular", 1);
                            } else {
                                values.put("top_rated", 1);
                            }
                            getActivity().getContentResolver().insert(
                                    MovieContentProvider.Movie.CONTENT_URI, values);
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    rvMovies.setVisibility(View.GONE);

                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Fail to load movie! Please retry.");
                    t.printStackTrace();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            rvMovies.setVisibility(View.GONE);

            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText("No internet Connection!");
        }

    }

}

