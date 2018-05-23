package com.udacity.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.PopularMovieDatabase;
import com.udacity.popularmovies.viewmodel.PopularMoviesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.PopularMoviesClickHandler {

    private static final int SPAN_COUNT = 2;

    private PopularMoviesViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private PopularMoviesAdapter mPopularMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.popular_movies_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPopularMoviesAdapter = new PopularMoviesAdapter(this, this, new ArrayList<PopularMovie>());
        mRecyclerView.setAdapter(mPopularMoviesAdapter);

        mViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);

        mViewModel.getPopularMovieList().observe(MainActivity.this, new Observer<List<PopularMovie>>() {
            @Override
            public void onChanged(@Nullable List<PopularMovie> popularMovies) {
                mPopularMoviesAdapter.swapPopularMovies(popularMovies);
            }
        });

        new MockDataAsyncTask().execute();
    }

    @Override
    public void onClick(long id) {
        // TODO (jasonscott) Create intent for detailed movie Activity.
    }

    public class MockDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            PopularMovieDatabase popularMovieDatabase = PopularMovieDatabase.getInstance(getApplicationContext());
            popularMovieDatabase.clearAllTables();
            List<PopularMovie> popularMovieList = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                int id = i + 1;

                PopularMovie popularMovie = new PopularMovie();

                popularMovie.id = (long) id;
                popularMovie.originalTitle = String.format(Locale.getDefault(), "TEST_ORIGINAL_TITLE_%d", id);
                popularMovie.posterPath = String.format(Locale.getDefault(), "TEST_POSTER_PATH_%d", id);
                popularMovie.overview = String.format(Locale.getDefault(), "TEST_OVERVIEW_%d", id);
                popularMovie.voteAverage = id;
                popularMovie.releaseDate = String.format(Locale.getDefault(), "TEST_RELEASE_DATE_%d", id);

                popularMovieList.add(popularMovie);
            }

            popularMovieDatabase.popularMovie().insertAll(popularMovieList);
            return null;
        }
    }
}
