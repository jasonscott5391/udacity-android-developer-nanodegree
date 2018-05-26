package com.udacity.popularmovies.sync;

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

public class MovieSyncTask {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org";
    public static final String POPULAR_MOVIES = "popular";
    public static final String TOP_RATED_MOVIES = "top_rated";

    private static final MovieDbService sMmovieDbService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieDbService.class);

    public static synchronized void syncPopularMovies(MovieDao movieDao) {
        try {

            int maxPage = 5;
            for (int i = 1; i <= maxPage; i++) {

                Response response = sMmovieDbService.getMovies(POPULAR_MOVIES, API_KEY, i).execute();
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

    }

    public static synchronized void syncTopRatedMovies(MovieDao movieDao) {
        try {

            int maxPage = 5;
            for (int i = 1; i <= maxPage; i++) {

                Response response = sMmovieDbService.getMovies(TOP_RATED_MOVIES, API_KEY, i).execute();
                MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();

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
    }
}
