package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.activity.MovieDetailActivity;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.model.MovieTrailerModel;
import android.support.v7.widget.CardView;

import java.util.List;

/**
 * Created by simon on 3/13/16.
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private List<MovieTrailerModel.TrailerResult> mList;
    private Context context;
    private String urlThumbnail = "http://img.youtube.com/vi/";
    private MovieTrailerListener mListener;

    public interface MovieTrailerListener{
        void onTrailerClick(String videoId);
    }

    public MovieTrailerAdapter(Context c, List<MovieTrailerModel.TrailerResult> l,
                               MovieTrailerListener listener){
        this.context = c;
        this.mList = l;
        this.mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView ivThumbnail;
        public final TextView tvTitle;
        public final CardView cvTrailer;

        public ViewHolder(View itemView) {
            super(itemView);

            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivTrailerThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTrailerTitle);
            cvTrailer = (CardView) itemView.findViewById(R.id.cvItemMovieTrailer);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_movie_trailer, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieTrailerModel.TrailerResult model = mList.get(position);

        holder.tvTitle.setText(model.name);
        Glide.with(context)
                .load(urlThumbnail+model.key+"/0.jpg")
                .into(holder.ivThumbnail);

        holder.cvTrailer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Open youtube player
                mListener.onTrailerClick(model.key);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList != null) return mList.size();

        return 0;
    }

}
