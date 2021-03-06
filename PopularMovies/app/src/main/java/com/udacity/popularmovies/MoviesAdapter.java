package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.utils.PopularMoviesUtils;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PopularMoviesViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final Context mContext;
    private final PopularMoviesClickHandler mClickHandler;

    private List<MovieDao.BaseMovie> mMovieList;

    public MoviesAdapter(@NonNull Context context, PopularMoviesClickHandler clickHandler, List<MovieDao.BaseMovie> movieList) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mMovieList = movieList;
    }

    @NonNull
    @Override
    public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, parent, false);

        return new PopularMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {
        MovieDao.BaseMovie baseMovie = mMovieList.get(position);
        PopularMoviesUtils.loadNetworkImageIntoView(
                PopularMoviesUtils.buildPosterImageUrl(
                        baseMovie.posterPath),
                holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        }
        return mMovieList.size();
    }

    public interface PopularMoviesClickHandler {
        void onClick(long id);
    }

    public void swapMovies(List<MovieDao.BaseMovie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView;

        public PopularMoviesViewHolder(@NonNull View view) {
            super(view);

            mImageView = view.findViewById(R.id.movie_image_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mMovieList.get(getAdapterPosition()).id);
        }
    }
}
