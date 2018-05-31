package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmovies.data.Movie;
import com.udacity.popularmovies.utils.PopularMoviesUtils;
import com.udacity.popularmovies.viewmodel.MovieViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.udacity.popularmovies.MainActivity.INTENT_KEY_MOVIE_DB;

public class DetailActivity extends AppCompatActivity {

    private static final DateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final DateFormat toDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    private MovieViewModel mMovieViewModel;
    private ImageView mPosterImageView;
    private TextView mOriginalTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.image_view_poster);
        mOriginalTitleTextView = findViewById(R.id.text_view_original_title);
        mReleaseDateTextView = findViewById(R.id.text_view_release_date);
        mVoteAverageTextView = findViewById(R.id.text_view_vote_average);
        mOverviewTextView = findViewById(R.id.text_view_overview);

        long id = getIntent().getLongExtra(INTENT_KEY_MOVIE_DB, -1L);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getMovie(id).observe(DetailActivity.this, this::bindMovie);
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

}
