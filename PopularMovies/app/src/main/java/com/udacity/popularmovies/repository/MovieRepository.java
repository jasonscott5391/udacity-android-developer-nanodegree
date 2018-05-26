package com.udacity.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.udacity.popularmovies.data.Movie;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MoviesWrapper;
import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.TopRatedMovie;
import com.udacity.popularmovies.service.MovieDbService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String POPULAR_MOVIES = "popular";
    private static final String TOP_RATED_MOVIES = "top_rated";

    private static MovieRepository sMovieRepository;

    private final MovieDbService movieDbService;
    private final MovieDao movieDao;
    private final PopularMovieDbAsyncTask popularMovieDbAsyncTask;
    private final TopRatedMovieDbAsyncTask topRatedMovieDbAsyncTask;

    public static synchronized MovieRepository getInstance(MovieDao movieDao) {
        if (sMovieRepository == null) {
            sMovieRepository = new MovieRepository(movieDao);
        }

        return sMovieRepository;
    }

    private MovieRepository(MovieDao movieDao) {

        this.movieDao = movieDao;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieDbService = retrofit.create(MovieDbService.class);

        popularMovieDbAsyncTask = new PopularMovieDbAsyncTask();
        topRatedMovieDbAsyncTask = new TopRatedMovieDbAsyncTask();

    }

    public LiveData<List<MovieDao.BaseMovie>> getMovies(String preference) {

        switch (preference) {
            case POPULAR_MOVIES:
                popularMovieDbAsyncTask.execute(preference);
                return movieDao.getPopularMovies();

            case TOP_RATED_MOVIES:
                topRatedMovieDbAsyncTask.execute(preference);
                return movieDao.getTopRatedMovies();

            default:
                throw new UnsupportedOperationException(String.format("Operation %s is unsupported!", preference));
        }
    }


    public class PopularMovieDbAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... preferences) {
            try {

                int maxPage = 5;
                for (int i = 1; i <= maxPage; i++) {

                    Response response = movieDbService.getMovies(preferences[0], API_KEY, i).execute();
                    MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();

                    if (maxPage > moviesWrapper.getTotalPages()) {
                        maxPage = moviesWrapper.getTotalPages();
                    }

                    if (moviesWrapper == null) {
                        System.out.println();
                    }

                    List<PopularMovie> popularMovieList = new ArrayList<>();
                    List<Movie> movieList = moviesWrapper.getMovieList();
                    for (Movie movie : movieList) {
                        if (movieDao.getMovieCountById(movie.id) == 0) {
                            movieDao.insertMovie(movie);
                        }
                        // Insert popular movie
                        PopularMovie popularMovie = new PopularMovie();
                        popularMovie.id = movie.id;
                        popularMovieList.add(popularMovie);
                    }

                    movieDao.insertPopularMovies(popularMovieList);
                    Thread.sleep(500);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class TopRatedMovieDbAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... preferences) {
            try {

                int maxPage = 5;
                for (int i = 1; i <= maxPage; i++) {

                    Response response = movieDbService.getMovies(preferences[0], API_KEY, i).execute();
                    MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();

                    if (maxPage > moviesWrapper.getTotalPages()) {
                        maxPage = moviesWrapper.getTotalPages();
                    }

                    List<TopRatedMovie> topRatedMovieList = new ArrayList<>();
                    List<Movie> movieList = moviesWrapper.getMovieList();
                    for (Movie movie : movieList) {
                        if (movieDao.getMovieCountById(movie.id) == 0) {
                            movieDao.insertMovie(movie);
                        }
                        // Insert popular movie
                        TopRatedMovie topRatedMovie = new TopRatedMovie();
                        topRatedMovie.id = movie.id;
                        topRatedMovieList.add(topRatedMovie);
                    }

                    movieDao.insertTopRatedMovies(topRatedMovieList);
                    Thread.sleep(500);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
