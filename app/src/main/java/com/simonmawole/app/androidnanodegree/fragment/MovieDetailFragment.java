package com.simonmawole.app.androidnanodegree.fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class MovieDetailFragment extends Fragment implements MovieTrailerAdapter.MovieTrailerListener{

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
            tvSynopsis.setText(bundle.getString("overview"));
            tvTitle.setText(bundle.getString("title"));
            tvReleaseDate.setText(Helpers.getDateUserFormat(bundle.getString("release_date")));
            tvLanguage.setText(bundle.getString("language"));

            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            tvRating.setText(df.format(bundle.getDouble("rating")) + " / 10");

            Glide.with(getActivity())
                    .load(urlImage + bundle.getString("poster") + "?api_key=" + Developer.MOVIES_API_KEY)
                    .into(ivPoster);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_action_favorite:
                /*TODO save or unsave movies as favorite*/
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
}
