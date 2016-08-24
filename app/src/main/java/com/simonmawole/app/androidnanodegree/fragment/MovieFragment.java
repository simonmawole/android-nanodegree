package com.simonmawole.app.androidnanodegree.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.MovieAdapter;
import com.simonmawole.app.androidnanodegree.data.MovieContentProvider;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.end_point.MovieService;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.sync.MySyncAdapter;
import com.simonmawole.app.androidnanodegree.utility.Helpers;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
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

    /*
    * Category selected
    * 0 is popular
    * 1 is top_rated
    * 2 is favorite
    * */
    private static int categorySelected = -1;

    //Binding
    @BindView(R.id.rvMovies) RecyclerView rvMovies;
 //   @BindView(R.id.srlMovies) SwipeRefreshLayout srlMovies;
    @BindView(R.id.pbLoadingProgress) ProgressBar progressBar;
    @BindView(R.id.tvMessage) TextView tvMessage;

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
                //fetchMovies("popular");
                fetchMoviesFromDatabse("popular");
                getActivity().setTitle(R.string.most_popular);
                break;
            case R.id.menu_top_rated:
                //fetchMovies("top_rated");
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

        //adapter = new MovieAdapter(getActivity(), null);
        //rvMovies.setAdapter(adapter);

        //fetchMoviesFromDatabse("popular");

        MySyncAdapter.syncImmediately(getActivity());

        getActivity().setTitle(R.string.most_popular);

        return rootView;
    }


    public void fetchMoviesFromDatabse(final String category){
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

                        //adapter = new MovieAdapter(getActivity(), mList);
                        //rvMovies.swapAdapter(adapter, false);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);

        Uri uriMovies;
        switch(categorySelected){
            case 0:
                //uriMovies = MovieContentProvider.Movie.popularMovie(1);
                break;
            case 1:
                //uriMovies = MovieContentProvider.Movie.topRatedMovie(1);
                break;
            case 2:
                //uriMovies = MovieContentProvider.Movie.favoriteMovie(1);
                break;
            default:
                //uriMovies = MovieContentProvider.Movie.CONTENT_URI;
        }

        return new CursorLoader(getActivity(),
                MovieContentProvider.Movie.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Helpers.printLog("CURSOR", String.valueOf(data.getCount()));
        adapter = new MovieAdapter(getActivity(), data);
        rvMovies.swapAdapter(adapter, false);

        progressBar.setVisibility(View.GONE);
        rvMovies.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter = new MovieAdapter(getActivity(), null);
        rvMovies.swapAdapter(adapter, false);
    }
}

