package com.udacity.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMoviesRepository {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org";

    private static PopularMoviesRepository sPopularMoviesRepository;

    private final MovieDbService movieDbService;
    private final PopularMovieDao popularMovieDao;
    private final AsyncTask asyncTask;

    public static synchronized PopularMoviesRepository getInstance(PopularMovieDao popularMovieDao) {
        if (sPopularMoviesRepository == null) {
            sPopularMoviesRepository = new PopularMoviesRepository(popularMovieDao);
        }

        return sPopularMoviesRepository;
    }

    private PopularMoviesRepository(PopularMovieDao popularMovieDao) {

        this.popularMovieDao = popularMovieDao;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieDbService = retrofit.create(MovieDbService.class);

        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Response response = movieDbService.getPopularMovies("popular", API_KEY).execute();
                    PopularMoviesWrapper popularMoviesWrapper = (PopularMoviesWrapper) response.body();
                    popularMovieDao.insertAll(popularMoviesWrapper.getPopularMovieList());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

    }

    public LiveData<List<PopularMovie>> getPopularMovies() {
        refreshPopularMovies();
        return popularMovieDao.selectAll();
    }

    private void refreshPopularMovies() {
        asyncTask.execute();
    }
}
