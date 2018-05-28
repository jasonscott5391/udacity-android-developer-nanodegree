package com.udacity.popularmovies.sync;

import android.util.Log;

import com.udacity.popularmovies.data.Movie;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MoviesWrapper;
import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.TopRatedMovie;
import com.udacity.popularmovies.repository.MovieRepository;
import com.udacity.popularmovies.service.MovieDbService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MovieSyncTask {

    private static final String TAG = MovieSyncTask.class.getSimpleName();
    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final long API_REQUEST_SLEEP = 500L;
    private static final int DEFAULT_MAX_PAGES = 5;

    static final String POPULAR_MOVIES = "popular";
    static final String TOP_RATED_MOVIES = "top_rated";

    private static final MovieDbService sMmovieDbService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieDbService.class);

    static synchronized void syncPopularMovies(MovieDao movieDao) {
        try {

            int maxPage = DEFAULT_MAX_PAGES;
            for (int i = 1; i <= maxPage; i++) {

                Log.d(TAG, String.format("syncPopularMovies - Preparing request for popular movie: page:%d, maxPage:%d", i, maxPage));
                Response response = sMmovieDbService.getMovies(POPULAR_MOVIES, API_KEY, i).execute();
                Log.d(TAG, String.format("syncPopularMovies - Popular movies response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));
                MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();

                if (moviesWrapper == null) {
                    return;
                }

                if (maxPage > moviesWrapper.getTotalPages()) {
                    maxPage = moviesWrapper.getTotalPages();
                }

                List<PopularMovie> popularMovieList = new ArrayList<>();
                List<Movie> movieList = moviesWrapper.getMovieList();
                for (Movie movie : movieList) {
                    if (movieDao.getMovieCountById(movie.id) == 0) {
                        Log.d(TAG, String.format("syncPopularMovies - Inserting movie: id:%d", movie.id));
                        movieDao.insertMovie(movie);
                    }
                    // Insert popular movie
                    PopularMovie popularMovie = new PopularMovie();
                    popularMovie.id = movie.id;
                    popularMovieList.add(popularMovie);
                }

                Log.d(TAG, String.format("syncPopularMovies - Inserting popular movies: popularMovieList:%s", Arrays.toString(popularMovieList.toArray())));
                movieDao.insertPopularMovies(popularMovieList);

                MovieRepository.getMovies().postValue(movieDao.getPopularMovies());
                Thread.sleep(API_REQUEST_SLEEP);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    static synchronized void syncTopRatedMovies(MovieDao movieDao) {
        try {

            int maxPage = DEFAULT_MAX_PAGES;
            for (int i = 1; i <= maxPage; i++) {

                Log.d(TAG, String.format("syncTopRatedMovies - Preparing request for top rated movie: page:%d, maxPage:%d", i, maxPage));
                Response response = sMmovieDbService.getMovies(TOP_RATED_MOVIES, API_KEY, i).execute();
                MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();
                Log.d(TAG, String.format("syncTopRatedMovies - Top rated movies response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));

                if (moviesWrapper == null) {
                    return;
                }

                if (maxPage > moviesWrapper.getTotalPages()) {
                    maxPage = moviesWrapper.getTotalPages();
                }

                List<TopRatedMovie> topRatedMovieList = new ArrayList<>();
                List<Movie> movieList = moviesWrapper.getMovieList();
                for (Movie movie : movieList) {
                    if (movieDao.getMovieCountById(movie.id) == 0) {
                        Log.d(TAG, String.format("syncTopRatedMovies - Inserting movie: id:%d", movie.id));
                        movieDao.insertMovie(movie);
                    }
                    // Insert popular movie
                    TopRatedMovie topRatedMovie = new TopRatedMovie();
                    topRatedMovie.id = movie.id;
                    topRatedMovieList.add(topRatedMovie);
                }

                Log.d(TAG, String.format("syncTopRatedMovies - Inserting top rated movies: topRatedMovieList:%s", Arrays.toString(topRatedMovieList.toArray())));
                movieDao.insertTopRatedMovies(topRatedMovieList);

                MovieRepository.getMovies().postValue(movieDao.getTopRatedMovies());
                Thread.sleep(API_REQUEST_SLEEP);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
