package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.udacity.popularmovies.settings.SettingsActivity;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
