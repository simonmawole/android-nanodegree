package com.simonmawole.app.androidnanodegree.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simon on 3/13/16.
 */
public class PopularMoviesDetailActivity extends AppCompatActivity {

    String urlImage = "http://image.tmdb.org/t/p/w500/";

    //Binding
    @Bind(R.id.ivMoviePoster) ImageView ivPoster;
    @Bind(R.id.tvMovieTitle) TextView tvTitle;
    @Bind(R.id.tvLanguage) TextView tvLanguage;
    @Bind(R.id.tvReleaseDate) TextView tvReleaseDate;
    @Bind(R.id.tvVoteAverage) TextView tvRating;
    @Bind(R.id.tvSynopsis) TextView tvSynopsis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        tvSynopsis.setText(bundle.getString("overview"));
        tvTitle.setText(bundle.getString("title"));
        tvReleaseDate.setText(bundle.getString("release_date"));
        tvLanguage.setText(bundle.getString("language"));
        tvRating.setText(String.valueOf(bundle.getDouble("rating"))+" / 10");
        Picasso.with(this)
                .load(urlImage+bundle.getString("poster")+"?api_key="+ Developer.MOVIES_API_KEY)
                .into(ivPoster);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
