package com.simonmawole.app.androidnanodegree.fragment;

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

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.MovieAdapter;
import com.simonmawole.app.androidnanodegree.data.MovieContentProvider;
import com.simonmawole.app.androidnanodegree.sync.MySyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simon on 5/16/16.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.MovieAdapterListener{

    private MovieAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    /*
    * Category selected
    * 0 is popular
    * 1 is top_rated
    * 2 is favorite
    * */
    private int categorySelected = 0;
    private int mPosition = -1;

    //Binding
    @BindView(R.id.rvMovies) RecyclerView rvMovies;
    //   @BindView(R.id.srlMovies) SwipeRefreshLayout srlMovies;
    @BindView(R.id.pbLoadingProgress) ProgressBar progressBar;
    @BindView(R.id.tvMessage) TextView tvMessage;

    public interface Callback {
        void onItemSelected(String id);
    }

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Initialize sync adapter
        MySyncAdapter.initializeSyncAdapter(getActivity());
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
                categorySelected = 0;
                getLoaderManager().restartLoader(0, null, this);
                getActivity().setTitle(R.string.most_popular);
                break;
            case R.id.menu_top_rated:
                categorySelected = 1;
                getLoaderManager().restartLoader(0, null, this);
                getActivity().setTitle(R.string.top_rated);
                break;
            case R.id.menu_favorite:
                categorySelected = 2;
                getLoaderManager().restartLoader(0, null, this);
                getActivity().setTitle(R.string.favorite);
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

        if (savedInstanceState != null && savedInstanceState.containsKey("list_position") &&
                 savedInstanceState.containsKey("category_selected")) {
            mPosition = savedInstanceState.getInt("list_position");
            categorySelected = savedInstanceState.getInt("category_selected");
            switch(categorySelected){
                case 0:
                    getActivity().setTitle(R.string.most_popular);
                    break;
                case 1:
                    getActivity().setTitle(R.string.top_rated);
                    break;
                case 2:
                    getActivity().setTitle(R.string.favorite);
                    break;
            }
        } else {
            getActivity().setTitle(R.string.most_popular);
        }

        return rootView;
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
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != -1) {
            outState.putInt("list_position", mPosition);
        }

        outState.putInt("category_selected", categorySelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);

        return new CursorLoader(getActivity(),
                getUrlFromCategorySelected(),
                null, null, null, null);
    }

    public Uri getUrlFromCategorySelected(){
        Uri mUri = null;
        switch(categorySelected){
            case 0:
                mUri = MovieContentProvider.Movie.popularMovie("1");
                break;
            case 1:
                mUri = MovieContentProvider.Movie.topRatedMovie("1");
                break;
            case 2:
                mUri = MovieContentProvider.Movie.favoriteMovie("1");
                break;
        }

        return mUri;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null && data.getCount() != 0) {
            adapter = new MovieAdapter(getActivity(), data, MovieFragment.this);
            rvMovies.swapAdapter(adapter, false);

            if(mPosition != -1) {
                rvMovies.smoothScrollToPosition(mPosition);
            }

            progressBar.setVisibility(View.GONE);
            rvMovies.setVisibility(View.VISIBLE);
            tvMessage.setVisibility(View.GONE);
        } else {
            MySyncAdapter.syncImmediately(getActivity());

            progressBar.setVisibility(View.GONE);
            rvMovies.setVisibility(View.GONE);

            tvMessage.setText("There is no movie");
            tvMessage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter = new MovieAdapter(getActivity(), null, MovieFragment.this);
        rvMovies.swapAdapter(adapter, false);
    }

    @Override
    public void onAdapterItemSelected(String id, int pos) {
        ((Callback)getActivity())
                .onItemSelected(id);
        mPosition = pos;
    }
}

