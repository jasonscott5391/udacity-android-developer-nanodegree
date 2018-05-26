package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.popularmovies.viewmodel.MoviesViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.PopularMoviesClickHandler {

    private static final int SPAN_COUNT = 2;

    private MoviesViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movies_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this, new ArrayList<>());
        mRecyclerView.setAdapter(mMoviesAdapter);
        mViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        mViewModel.getMoviesList().observe(MainActivity.this, movies -> mMoviesAdapter.swapMovies(movies));
    }

    @Override
    public void onClick(long id) {
        // TODO (jasonscott) Create intent for detailed movie Activity.
    }

}
