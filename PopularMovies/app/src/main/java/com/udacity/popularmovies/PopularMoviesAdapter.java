package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.utils.PopularMoviesUtils;

import java.util.List;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder> {

    private final Context mContext;
    private final PopularMoviesClickHandler mClickHandler;

    private List<PopularMovie> mPopularMovieList;

    public PopularMoviesAdapter(@NonNull Context mContext, PopularMoviesClickHandler clickHandler, List<PopularMovie> popularMovieList) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
        this.mPopularMovieList = popularMovieList;
    }

    @NonNull
    @Override
    public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.popular_movie_grid_item, parent, false);

        return new PopularMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {
        PopularMovie popularMovie = mPopularMovieList.get(position);
        PopularMoviesUtils.loadNetworkImageIntoView(
                PopularMoviesUtils.buildPosterImageUrl(
                        popularMovie.posterPath),
                holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return  mPopularMovieList.size();
    }

    public interface PopularMoviesClickHandler {
        void onClick(long id);
    }

    public void swapPopularMovies(List<PopularMovie> popularMovies) {
        this.mPopularMovieList = popularMovies;
        notifyDataSetChanged();
    }

    public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView;

        public PopularMoviesViewHolder(@NonNull View view) {
            super(view);

            mImageView = view.findViewById(R.id.popular_movies_image_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mPopularMovieList.get(getAdapterPosition()).id);
        }
    }
}
