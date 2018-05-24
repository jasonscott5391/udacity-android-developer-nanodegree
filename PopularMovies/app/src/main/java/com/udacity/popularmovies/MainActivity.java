package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.popularmovies.viewmodel.PopularMoviesViewModel;

import java.util.ArrayList;

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

        mPopularMoviesAdapter = new PopularMoviesAdapter(this, this, new ArrayList<>());
        mRecyclerView.setAdapter(mPopularMoviesAdapter);
        mViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);

        mViewModel.getPopularMovieList().observe(MainActivity.this, popularMovies -> mPopularMoviesAdapter.swapPopularMovies(popularMovies));
    }

    @Override
    public void onClick(long id) {
        // TODO (jasonscott) Create intent for detailed movie Activity.
    }

}
