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

/**
 * Created by simon on 3/13/16.
 */
public class PopularMoviesDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvReleaseDate, tvLanguage, tvRating, tvSynopsis;
    ImageView ivPoster;
    private String imageUrl = "http://image.tmdb.org/t/p/w500/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPoster = (ImageView) findViewById(R.id.ivMoviePoster);
        tvLanguage = (TextView) findViewById(R.id.tvLanguage);
        tvRating = (TextView) findViewById(R.id.tvVoteAverage);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);

        Bundle bundle = getIntent().getExtras();

        tvSynopsis.setText(bundle.getString("overview"));
        tvTitle.setText(bundle.getString("title"));
        tvReleaseDate.setText(bundle.getString("release_date"));
        tvLanguage.setText(bundle.getString("language"));
        tvRating.setText(String.valueOf(bundle.getDouble("rating"))+" / 10");
        Picasso.with(this)
                .load(imageUrl+bundle.getString("poster")+"?api_key="+ Developer.MOVIES_API_KEY)
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
