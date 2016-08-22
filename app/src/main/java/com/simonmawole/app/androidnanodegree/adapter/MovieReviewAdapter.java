package com.simonmawole.app.androidnanodegree.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.model.MovieReviewModel;
import com.simonmawole.app.androidnanodegree.model.MovieTrailerModel;

import java.util.List;

/**
 * Created by simon on 3/13/16.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private List<MovieReviewModel.ReviewResult> mList;
    private Context context;

    public MovieReviewAdapter(Context c, List<MovieReviewModel.ReviewResult> l){
        this.context = c;
        this.mList = l;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvAuthor, tvContent;

        public ViewHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvReviewAuthor);
            tvContent = (TextView) itemView.findViewById(R.id.tvReviewContent);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_movie_review, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieReviewModel.ReviewResult model = mList.get(position);

        holder.tvAuthor.setText(model.author);
        holder.tvContent.setText(model.content);

    }

    @Override
    public int getItemCount() {
        if(mList != null) return mList.size();

        return 0;
    }

}
