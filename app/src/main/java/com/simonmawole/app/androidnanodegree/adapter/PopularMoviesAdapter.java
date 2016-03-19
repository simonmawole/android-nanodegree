package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.activity.PopularMoviesDetailActivity;
import com.simonmawole.app.androidnanodegree.model.MoviesModel;
import com.squareup.picasso.Picasso;
import com.simonmawole.app.androidnanodegree.developer.Developer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simon on 3/13/16.
 */
public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder> {

    private ArrayList<MoviesModel> mList;
    private Context context;
    private String urlImage = "http://image.tmdb.org/t/p/w500/";

    public PopularMoviesAdapter(Context c, ArrayList<MoviesModel> l){
        this.context = c;
        this.mList = l;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivMoviePoster) ImageView ivMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_movies_poster, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MoviesModel model = mList.get(position);

        Picasso.with(context)
                .load(urlImage+model.getPoster_path()+"?api_key="+Developer.MOVIES_API_KEY)
                .into(holder.ivMoviePoster);

        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", model.getOriginal_title());
                bundle.putString("poster", model.getPoster_path());
                bundle.putString("overview", model.getOverview());
                bundle.putDouble("rating", model.getVote_average());
                bundle.putString("language", model.getOriginal_language());
                bundle.putString("release_date", model.getRelease_date());
                context.startActivity(
                        new Intent(context, PopularMoviesDetailActivity.class).putExtras(bundle));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
