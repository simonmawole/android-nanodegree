package com.simonmawole.app.androidnanodegree.fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.adapter.MovieAdapter;
import com.simonmawole.app.androidnanodegree.adapter.MovieReviewAdapter;
import com.simonmawole.app.androidnanodegree.adapter.MovieTrailerAdapter;
import com.simonmawole.app.androidnanodegree.data.MovieContentProvider;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.end_point.MovieReviewService;
import com.simonmawole.app.androidnanodegree.end_point.MovieService;
import com.simonmawole.app.androidnanodegree.end_point.MovieTrailerService;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.model.MovieReviewModel;
import com.simonmawole.app.androidnanodegree.model.MovieTrailerModel;
import com.simonmawole.app.androidnanodegree.utility.Helpers;
import com.simonmawole.app.androidnanodegree.utility.NetworkInterceptor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simon on 5/16/16.
 */
public class MovieDetailFragment extends Fragment implements
        MovieTrailerAdapter.MovieTrailerListener, LoaderManager.LoaderCallbacks<Cursor>{

    private String urlImage = "http://image.tmdb.org/t/p/w500/";
    private Bundle bundle;

    //Binding
    @BindView(R.id.ivMoviePoster) ImageView ivPoster;
    @BindView(R.id.tvMovieTitle) TextView tvTitle;
    @BindView(R.id.tvLanguage) TextView tvLanguage;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvVoteAverage) TextView tvRating;
    @BindView(R.id.tvSynopsis) TextView tvSynopsis;
    @BindView(R.id.rvMovieTrailers) RecyclerView rvMovieTrailer;
    @BindView(R.id.rvMovieReviews) RecyclerView rvMovieReview;

    private MovieReviewAdapter reviewAdapter;
    private RecyclerView.LayoutManager reviewLayoutManager;

    private MovieTrailerAdapter trailerAdapter;
    private RecyclerView.LayoutManager trailerLayoutManager;

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private Retrofit retrofit;
    private MovieTrailerService movieTrailerService;
    private MovieReviewService movieReviewService;
    private Call<MovieTrailerModel> callTrailer;
    private Call<MovieReviewModel> callReview;
    private Gson gson;
    private List<MovieTrailerModel.TrailerResult> mTrailerList;
    private List<MovieReviewModel.ReviewResult> mReviewList;

    private String mDataToShare;
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_movie_detail,container,false);

        ButterKnife.bind(this, rootView);

        bundle = getActivity().getIntent().getExtras();

        if(bundle != null) {
           /* tvSynopsis.setText(bundle.getString("overview"));
            tvTitle.setText(bundle.getString("title"));
            tvReleaseDate.setText(Helpers.getDateUserFormat(bundle.getString("release_date")));
            tvLanguage.setText(bundle.getString("language"));

            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            tvRating.setText(df.format(bundle.getDouble("rating")) + " / 10");

            Glide.with(getActivity())
                    .load(urlImage + bundle.getString("poster") + "?api_key=" + Developer.MOVIES_API_KEY)
                    .into(ivPoster);*/

            setTrailer(bundle.getString("movie_id"));
            setReview(bundle.getString("movie_id"));

        }

        return rootView;
    }

    public void setTrailer(String movieId){
        trailerLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        rvMovieTrailer.setLayoutManager(trailerLayoutManager);

        trailerAdapter = new MovieTrailerAdapter(getActivity(), null, this);
        rvMovieTrailer.setAdapter(trailerAdapter);

        fetchMovieTrailers(movieId);
    }

    public void setReview(String movieId){
        reviewLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvMovieReview.setLayoutManager(reviewLayoutManager);

        reviewAdapter = new MovieReviewAdapter(getActivity(), null);
        rvMovieReview.setAdapter(reviewAdapter);

        fetchMovieReview(movieId);
    }

    private void startYoutubePlayer(String video){
        int startTimeMillis = 0; //start the video from the beginning
        boolean autoplay = true; //Make the player auto play the video
        boolean lightboxMode = false; //make the player lite

        Intent intent = null;
        intent = YouTubeStandalonePlayer.createVideoIntent(
                    getActivity(), Developer.YOUTUBE_API_KEY, video, startTimeMillis, autoplay, lightboxMode);

        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
            } else {
                // Could not resolve the intent - must need to install or update the YouTube API service.
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putString("m")
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.menu_action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mDataToShare != null) {
            mShareActionProvider.setShareIntent(createShareIntent("", ""));
        }
    }

    private Intent createShareIntent(String title, String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, title +"\n"+ url);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_action_favorite:
                item.setIcon(R.mipmap.ic_action_important);
                Helpers.showToast(getActivity(), "Favorite");
                break;
        }

        return super.onOptionsItemSelected(item);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != getActivity().RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                Helpers.showToast(getActivity(), getString(R.string.error_player));
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo =
                getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    public void fetchMovieTrailers(String movieId){
        if(Helpers.isConnected(getActivity())){
            gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            movieTrailerService = retrofit.create(MovieTrailerService.class);

            callTrailer = movieTrailerService.getMovieTrailer(movieId, Developer.MOVIES_API_KEY);

            callTrailer.enqueue(new retrofit2.Callback<MovieTrailerModel>() {
                @Override
                public void onResponse(Call<MovieTrailerModel> call,
                                       Response<MovieTrailerModel> response) {
                    try {
                        MovieTrailerModel model = response.body();
                        mTrailerList = model.results;

                        trailerAdapter = new MovieTrailerAdapter(getActivity(),
                                mTrailerList, MovieDetailFragment.this);
                        rvMovieTrailer.swapAdapter(trailerAdapter, false);

                        // If onCreateOptionsMenu has already happened,
                        // we need to update the share intent now.
                        if (mShareActionProvider != null) {
                            mShareActionProvider.setShareIntent(createShareIntent(
                                    mTrailerList.get(0).name,
                                    "https://www.youtube.com/watch?v="+mTrailerList.get(0).key));
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MovieTrailerModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            //
        }
    }

    public void fetchMovieReview(String movieId){
        if(Helpers.isConnected(getActivity())){
            gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            movieReviewService = retrofit.create(MovieReviewService.class);

            callReview = movieReviewService.getMovieReview(movieId, Developer.MOVIES_API_KEY);

            callReview.enqueue(new retrofit2.Callback<MovieReviewModel>() {
                @Override
                public void onResponse(Call<MovieReviewModel> call,
                                       Response<MovieReviewModel> response) {
                    try {
                        MovieReviewModel model = response.body();
                        mReviewList = model.results;

                        reviewAdapter = new MovieReviewAdapter(getActivity(), mReviewList);
                        rvMovieReview.swapAdapter(reviewAdapter, false);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MovieReviewModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            //
        }
    }

    /*
    *Triggered when user click one of the trailers
    * */
    @Override
    public void onTrailerClick(String videoId) {
        if(videoId != null
                && videoId.length() != 0) {
            startYoutubePlayer(videoId);
        } else {
            Helpers.showToast(getActivity(), "No trailer");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri mUri = MovieContentProvider.Movie.withMovieId(bundle.getString("movie_id"));

        return new CursorLoader(getActivity(),mUri,null,"movie_id="+bundle.getString("movie_id"),
                null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                tvSynopsis.setText(data.getString(data.getColumnIndex("overview")));
                tvTitle.setText(data.getString(data.getColumnIndex("original_title")));
                tvReleaseDate.setText(Helpers.getDateUserFormat(data.getString(
                        data.getColumnIndex("release_date"))));
                tvLanguage.setText(data.getString(data.getColumnIndex("original_language")));

                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);
                tvRating.setText(df.format(Double.parseDouble(data.getString(
                        data.getColumnIndex("vote_average")))) + " / 10");

                Glide.with(getActivity())
                        .load(urlImage + data.getString(data.getColumnIndex("poster_path"))
                                + "?api_key=" + Developer.MOVIES_API_KEY)
                        .into(ivPoster);

            }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
