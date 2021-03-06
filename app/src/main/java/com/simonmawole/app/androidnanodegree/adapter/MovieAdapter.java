package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.developer.Developer;

/**
 * Created by simon on 3/13/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context context;
    private String urlImage = "http://image.tmdb.org/t/p/w500/";
    private MovieAdapterListener mListener;

    public MovieAdapter(Context c, Cursor cursor, MovieAdapterListener listener){
        this.context = c;
        this.mCursor = cursor;
        this.mListener = listener;
    }

    public interface MovieAdapterListener{
        void onAdapterItemSelected(String id, int pos);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mCursor.moveToPosition(position);
        holder.movieId = mCursor.getString(mCursor.getColumnIndex("movie_id"));

        Glide.with(context)
                .load(urlImage
                        +mCursor.getString(mCursor.getColumnIndex("poster_path"))
                        +"?api_key="+Developer.MOVIES_API_KEY)
                .into(holder.ivMoviePoster);

        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.onAdapterItemSelected(holder.movieId, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mCursor != null) return mCursor.getCount();

        return 0;
    }

}
