package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.activity.MovieDetailActivity;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.utility.Helpers;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simon on 3/13/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //private List<MovieModel.MovieResult> mList;
    private Cursor mCursor;
    private Context context;
    private String urlImage = "http://image.tmdb.org/t/p/w500/";

    public MovieAdapter(Context c, Cursor cursor){
        this.context = c;
        this.mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView ivMoviePoster;
        public String movieId;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.ivMoviePoster);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_movie_poster, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.movieId = mCursor.getString(mCursor.getColumnIndex("movie_id"));

        Helpers.printLog("MOVIE_ID",holder.movieId);
        Glide.with(context)
                .load(urlImage
                        +mCursor.getString(mCursor.getColumnIndex("poster_path"))
                        +"?api_key="+Developer.MOVIES_API_KEY)
                .into(holder.ivMoviePoster);

        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Helpers.printLog("MOVIE_ID_ON",holder.movieId);

                Bundle bundle = new Bundle();
                bundle.putString("movie_id", holder.movieId);
                bundle.putString("title",
                        mCursor.getString(mCursor.getColumnIndex("original_title")));
                bundle.putString("poster",
                        mCursor.getString(mCursor.getColumnIndex("poster_path")));
                bundle.putString("overview",
                        mCursor.getString(mCursor.getColumnIndex("overview")));
                bundle.putDouble("rating",
                        mCursor.getDouble(mCursor.getColumnIndex("vote_average")));
                bundle.putString("language",
                        mCursor.getString(mCursor.getColumnIndex("original_language")));
                bundle.putString("release_date",
                        mCursor.getString(mCursor.getColumnIndex("release_date")));
                context.startActivity(
                        new Intent(context, MovieDetailActivity.class).putExtras(bundle));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mCursor != null) return mCursor.getCount();

        return 0;
    }

}
