package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simon on 3/13/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieModel.MovieResult> mList;
    private Context context;
    private String urlImage = "http://image.tmdb.org/t/p/w500/";

    public MovieAdapter(Context c, List<MovieModel.MovieResult> l){
        this.context = c;
        this.mList = l;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView ivMoviePoster;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieModel.MovieResult model = mList.get(position);

        Glide.with(context)
                .load(urlImage+model.poster_path+"?api_key="+Developer.MOVIES_API_KEY)
                .into(holder.ivMoviePoster);

        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("movie_id", String.valueOf(model.id));
                bundle.putString("title", model.original_title);
                bundle.putString("poster", model.poster_path);
                bundle.putString("overview", model.overview);
                bundle.putDouble("rating", model.vote_average);
                bundle.putString("language", model.original_language);
                bundle.putString("release_date", model.release_date);
                context.startActivity(
                        new Intent(context, MovieDetailActivity.class).putExtras(bundle));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList != null) return mList.size();

        return 0;
    }

}
