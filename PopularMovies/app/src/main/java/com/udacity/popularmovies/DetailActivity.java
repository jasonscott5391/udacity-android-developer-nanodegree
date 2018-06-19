package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.utils.PopularMoviesUtils;
import com.udacity.popularmovies.viewmodel.MovieReviewListViewModel;
import com.udacity.popularmovies.viewmodel.MovieReviewListViewModelFactory;
import com.udacity.popularmovies.viewmodel.MovieVideoListViewModel;
import com.udacity.popularmovies.viewmodel.MovieVideoListViewModelFactory;
import com.udacity.popularmovies.viewmodel.MovieViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.udacity.popularmovies.MainActivity.INTENT_KEY_MOVIE_DB;

public class DetailActivity extends AppCompatActivity implements MovieVideosAdapter.MovieVideosClickHandler {

    private static final DateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final DateFormat toDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    private MovieViewModel mMovieViewModel;
    private ImageView mPosterImageView;
    private TextView mOriginalTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private TextView mTrailersTextView;
    private TextView mReviewsTextView;

    private RecyclerView mMovieVideosRecyclerView;
    private LinearLayoutManager mMovieVideosLinearLayoutManager;
    private MovieVideosAdapter mMovieVideosAdapter;
    private MovieVideoListViewModel mMovieVideoListViewModel;

    private RecyclerView mMovieReviewsRecyclerView;
    private LinearLayoutManager mMovieReviewsLinearLayoutManager;
    private MovieReviewsAdapter mMovieReviewsAdapter;
    private MovieReviewListViewModel mMovieReviewListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.image_view_poster);
        mOriginalTitleTextView = findViewById(R.id.text_view_original_title);
        mReleaseDateTextView = findViewById(R.id.text_view_release_date);
        mVoteAverageTextView = findViewById(R.id.text_view_vote_average);
        mOverviewTextView = findViewById(R.id.text_view_overview);
        mTrailersTextView = findViewById(R.id.movie_trailers_label);
        mReviewsTextView = findViewById(R.id.movie_reviews_label);

        mMovieVideosRecyclerView = findViewById(R.id.movie_videos_recycler_view);
        mMovieVideosLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mMovieVideosRecyclerView.setLayoutManager(mMovieVideosLinearLayoutManager);
        mMovieVideosRecyclerView.setHasFixedSize(true);

        mMovieVideosAdapter = new MovieVideosAdapter(this, this, new ArrayList<>());
        mMovieVideosRecyclerView.setAdapter(mMovieVideosAdapter);

        mMovieReviewsRecyclerView = findViewById(R.id.movie_reviews_recycler_view);
        mMovieReviewsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mMovieReviewsRecyclerView.setLayoutManager(mMovieReviewsLinearLayoutManager);
        mMovieReviewsRecyclerView.setHasFixedSize(true);

        mMovieReviewsAdapter = new MovieReviewsAdapter(this, new ArrayList<>());
        mMovieReviewsRecyclerView.setAdapter(mMovieReviewsAdapter);

        long id = getIntent().getLongExtra(INTENT_KEY_MOVIE_DB, -1L);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getMovie(id).observe(DetailActivity.this, this::bindMovie);

        MovieVideoListViewModelFactory movieVideoListViewModelFactory = new MovieVideoListViewModelFactory(this, id);
        mMovieVideoListViewModel = ViewModelProviders.of(this, movieVideoListViewModelFactory).get(MovieVideoListViewModel.class);
        mMovieVideoListViewModel.getMovieVideoList().observe(this, movieVideoList -> {
            mMovieVideosAdapter.swapMovieVideos(movieVideoList);
            if (movieVideoList != null
                    && !movieVideoList.isEmpty()) {
                mTrailersTextView.setVisibility(View.VISIBLE);
            } else {
                mTrailersTextView.setVisibility(View.INVISIBLE);
            }
        });

        MovieReviewListViewModelFactory movieReviewListViewModelFactory = new MovieReviewListViewModelFactory(this, id);
        mMovieReviewListViewModel = ViewModelProviders.of(this, movieReviewListViewModelFactory).get(MovieReviewListViewModel.class);
        mMovieReviewListViewModel.getMovieReviewList().observe(this, movieReviewList -> {
            mMovieReviewsAdapter.swapMovieReviews(movieReviewList);
            if (movieReviewList != null
                    && !movieReviewList.isEmpty()) {
                mReviewsTextView.setVisibility(View.VISIBLE);
            } else {
                mReviewsTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void bindMovie(Movie movie) {
        if (movie != null) {
            setTitle(movie.originalTitle);
            PopularMoviesUtils.loadNetworkImageIntoView(
                    PopularMoviesUtils.buildPosterImageUrl(movie.posterPath, PopularMoviesUtils.PosterWidth.W_780_POSTER_WIDTH),
                    mPosterImageView);

            mOriginalTitleTextView.setText(movie.originalTitle);

            try {
                mReleaseDateTextView.setText(toDateFormat.format(fromDateFormat.parse(movie.releaseDate)));
            } catch (ParseException e) {
                mReleaseDateTextView.setText(movie.releaseDate);
            }
            mVoteAverageTextView.setText(String.format(Locale.getDefault(), "%d/10", movie.voteAverage.intValue()));
            mOverviewTextView.setText(movie.overview);
        }

    }

    @Override
    public void onClick(String key) {
        Intent movieVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(PopularMoviesUtils.buildYouTubeUrl(key)));
        startActivity(movieVideoIntent);
    }
}
