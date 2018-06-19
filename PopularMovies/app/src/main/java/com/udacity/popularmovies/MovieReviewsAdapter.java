package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.entity.MovieReview;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsViewHolder> {

    private static final String TAG = MovieReviewsAdapter.class.getSimpleName();

    private final Context mContext;

    private List<MovieReview> mMovieReviewList;

    public MovieReviewsAdapter(@NonNull Context context, List<MovieReview> movieReviewList) {
        this.mContext = context;
        this.mMovieReviewList = movieReviewList;
    }

    @NonNull
    @Override
    public MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_review_list_item, parent, false);
        return new MovieReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsViewHolder holder, int position) {
        MovieReview movieReview = mMovieReviewList.get(position);
        holder.mMovieReviewAuthorTextView.setText(movieReview.author);
        holder.mMovieReviewContentTextView.setText(movieReview.content);
    }

    @Override
    public int getItemCount() {
        if (mMovieReviewList == null) {
            return 0;
        }

        return mMovieReviewList.size();
    }

    public void swapMovieReviews(List<MovieReview> movieReviewList) {
        this.mMovieReviewList = movieReviewList;
        notifyDataSetChanged();
    }

    public class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        final TextView mMovieReviewAuthorTextView;
        final TextView mMovieReviewContentTextView;

        public MovieReviewsViewHolder(View itemView) {
            super(itemView);
            mMovieReviewAuthorTextView = itemView.findViewById(R.id.movie_review_author);
            mMovieReviewContentTextView = itemView.findViewById(R.id.movie_review_content);
        }
    }
}
