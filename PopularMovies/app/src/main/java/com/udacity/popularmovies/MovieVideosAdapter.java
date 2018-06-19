package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.entity.MovieVideo;

import java.util.List;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideosViewHolder> {

    private static final String TAG = MovieVideosAdapter.class.getSimpleName();

    private final Context mContext;
    private final MovieVideosClickHandler mMovieVideosClickHandler;

    private List<MovieVideo> mMovieVideoList;

    public MovieVideosAdapter(@NonNull Context context, MovieVideosClickHandler clickHandler, List<MovieVideo> movieVideoList) {
        this.mContext = context;
        this.mMovieVideosClickHandler = clickHandler;
        this.mMovieVideoList = movieVideoList;
    }

    @NonNull
    @Override
    public MovieVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_video_list_item, parent, false);

        return new MovieVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVideosViewHolder holder, int position) {
        MovieVideo movieVideo = mMovieVideoList.get(position);
        holder.mMovieVideoNameTextView.setText(movieVideo.name);
    }

    @Override
    public int getItemCount() {
        if (mMovieVideoList == null) {
            return 0;
        }
        return mMovieVideoList.size();
    }

    public void swapMovieVideos(List<MovieVideo> movieVideoList) {
        this.mMovieVideoList = movieVideoList;
        notifyDataSetChanged();
    }

    public class MovieVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mMovieVideoNameTextView;

        public MovieVideosViewHolder(View itemView) {
            super(itemView);
            mMovieVideoNameTextView = itemView.findViewById(R.id.movie_video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMovieVideosClickHandler.onClick(mMovieVideoList.get(getAdapterPosition()).youtubeKey);
        }
    }

    public interface MovieVideosClickHandler {
        void onClick(String youtubeKey);
    }
}
