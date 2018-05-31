package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.udacity.popularmovies.settings.SettingsActivity;
import com.udacity.popularmovies.sync.MovieSyncTask;
import com.udacity.popularmovies.viewmodel.MovieListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.PopularMoviesClickHandler {

    private static final int SPAN_COUNT = 2;
    protected static final String INTENT_KEY_MOVIE_DB = "INTENT_KEY_MOVIE_DB";

    private MovieListViewModel mViewModel;
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
        mViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        mViewModel.getMoviesList().observe(MainActivity.this, movies -> mMoviesAdapter.swapMovies(movies));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortKey = getString(R.string.pref_movie_sort_key);
        String defaultSort = getString(R.string.pref_movie_sort_popular);
        String sortPreference = sharedPreferences.getString(sortKey, defaultSort);

        switch (sortPreference) {
            case MovieSyncTask.POPULAR_MOVIES:
                setTitle(R.string.popular_movies);
                break;

            case MovieSyncTask.TOP_RATED_MOVIES:
                setTitle(R.string.top_rated_movies);
                break;

            default:
                setTitle(R.string.app_name);
                break;
        }
    }

    @Override
    public void onClick(long id) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(INTENT_KEY_MOVIE_DB, id);
        startActivity(movieDetailIntent);
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
