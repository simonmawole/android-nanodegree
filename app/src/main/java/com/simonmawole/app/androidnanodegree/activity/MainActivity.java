package com.simonmawole.app.androidnanodegree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.utility.Utility;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by simon on 3/13/16.
 */
public class MainActivity extends AppCompatActivity {

    //Bind strings
    @Nullable @BindString(R.string.built_it_bigger) String builtItBigger;
    @Nullable @BindString(R.string.library_app) String libraryApp;
    @Nullable @BindString(R.string.xyz_reader) String xyzReader;
    @Nullable @BindString(R.string.spotify_streamer) String spotifyStreamer;
    @Nullable @BindString(R.string.scores_app) String scoreApp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init ButterKnife
        ButterKnife.bind(this);

    }

    @OnClick({R.id.buttonPopularMovies,R.id.buttonBuildItBigger,R.id.buttonLibrary,
    R.id.buttonScores,R.id.buttonSpotifyStreamer,R.id.buttonXYZReader})
    public void onButtonClick(Button button){
        switch (button.getId()){
            case R.id.buttonPopularMovies:
                startActivity(new Intent(this, MovieActivity.class));
                break;
            case R.id.buttonBuildItBigger:
                Utility.showToast(this, builtItBigger);
                break;
            case R.id.buttonLibrary:
                Utility.showToast(this,libraryApp);
                break;
            case R.id.buttonScores:
                Utility.showToast(this,scoreApp);
                break;
            case R.id.buttonSpotifyStreamer:
                Utility.showToast(this,spotifyStreamer);
                break;
            case R.id.buttonXYZReader:
                Utility.showToast(this,xyzReader);
                break;
        }
    }

}
