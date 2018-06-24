package com.udacity.popularmovies.sync;

import android.util.Log;

import com.udacity.popularmovies.data.MovieReviewsWrapper;
import com.udacity.popularmovies.data.MovieVideosWrapper;
import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MoviesWrapper;
import com.udacity.popularmovies.entity.MovieReview;
import com.udacity.popularmovies.entity.MovieVideo;
import com.udacity.popularmovies.entity.PopularMovie;
import com.udacity.popularmovies.entity.TopRatedMovie;
import com.udacity.popularmovies.repository.MovieRepository;
import com.udacity.popularmovies.service.MovieDbService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieSyncTask {

    private static final String TAG = MovieSyncTask.class.getSimpleName();
    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final long API_REQUEST_SLEEP = 500L;
    private static final int DEFAULT_MAX_PAGES = 5;

    public static final String POPULAR_MOVIES = "popular";
    public static final String TOP_RATED_MOVIES = "top_rated";
    public static final String FAVORITE_MOVIES = "favorites";

    private static final MovieDbService sMovieDbService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieDbService.class);

    static synchronized void syncPopularMovies(MovieDao movieDao) {
        try {

            long start = System.currentTimeMillis();

            int maxPage = DEFAULT_MAX_PAGES;
            for (int i = 1; i <= maxPage; i++) {

                Log.d(TAG, String.format("syncPopularMovies - Preparing request for popular movie: page:%d, maxPage:%d", i, maxPage));
                Response response = sMovieDbService.getMovies(POPULAR_MOVIES, API_KEY, i).execute();
                Log.d(TAG, String.format("syncPopularMovies - Popular movies response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));
                MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();

                if (moviesWrapper == null) {
                    throw new IOException("Null response!");
                }

                if (maxPage > moviesWrapper.getTotalPages()) {
                    maxPage = moviesWrapper.getTotalPages();
                }

                List<PopularMovie> popularMovieList = new ArrayList<>();
                List<Movie> movieList = moviesWrapper.getMovieList();
                for (Movie movie : movieList) {
                    PopularMovie popularMovie = new PopularMovie();
                    popularMovie.id = movie.id;
                    popularMovieList.add(popularMovie);
                }

                Log.d(TAG, String.format("syncPopularMovies - Inserting movies: movieList:%s", Arrays.toString(movieList.toArray())));
                movieDao.insertMovies(movieList);

                Log.d(TAG, String.format("syncPopularMovies - Inserting popular movies: popularMovieList:%s", Arrays.toString(popularMovieList.toArray())));
                movieDao.insertPopularMovies(popularMovieList);

                MovieRepository.getMovies().postValue(movieDao.getPopularMovies());
                Thread.sleep(API_REQUEST_SLEEP);
            }

            long end = System.currentTimeMillis();

            long timeTakenMs = end - start;

            Log.d(TAG, String.format("20 batch movie insert, 20 batch popular movie insert, & maxPage of %d:(end - start) = timeTakenMs -> (%d - %d) = %d MS = %d S", maxPage, end, start, timeTakenMs, (timeTakenMs / 1000)));

        } catch (IOException | InterruptedException e) {
            MovieRepository.getMovies().postValue(null);
            Log.e(TAG, e.getMessage());
        }

    }

    static synchronized void syncTopRatedMovies(MovieDao movieDao) {
        try {

            long start = System.currentTimeMillis();

            int maxPage = DEFAULT_MAX_PAGES;
            for (int i = 1; i <= maxPage; i++) {

                Log.d(TAG, String.format("syncTopRatedMovies - Preparing request for top rated movie: page:%d, maxPage:%d", i, maxPage));
                Response response = sMovieDbService.getMovies(TOP_RATED_MOVIES, API_KEY, i).execute();
                MoviesWrapper moviesWrapper = (MoviesWrapper) response.body();
                Log.d(TAG, String.format("syncTopRatedMovies - Top rated movies response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));

                if (moviesWrapper == null) {
                    throw new IOException("Null response!");
                }

                if (maxPage > moviesWrapper.getTotalPages()) {
                    maxPage = moviesWrapper.getTotalPages();
                }

                List<TopRatedMovie> topRatedMovieList = new ArrayList<>();
                List<Movie> movieList = moviesWrapper.getMovieList();
                for (Movie movie : movieList) {
                    TopRatedMovie topRatedMovie = new TopRatedMovie();
                    topRatedMovie.id = movie.id;
                    topRatedMovieList.add(topRatedMovie);
                }

                Log.d(TAG, String.format("syncTopRatedMovies - Inserting movies: movieList:%s", Arrays.toString(movieList.toArray())));
                movieDao.insertMovies(movieList);

                Log.d(TAG, String.format("syncTopRatedMovies - Inserting top rated movies: topRatedMovieList:%s", Arrays.toString(topRatedMovieList.toArray())));
                movieDao.insertTopRatedMovies(topRatedMovieList);

                MovieRepository.getMovies().postValue(movieDao.getTopRatedMovies());
                Thread.sleep(API_REQUEST_SLEEP);
            }

            long end = System.currentTimeMillis();

            long timeTakenMs = end - start;
            Log.d(TAG, String.format("20 batch movie insert, 20 batch top_rated movie insert, & maxPage of %d:(end - start) = timeTakenMs -> (%d - %d) = %d MS = %d S", maxPage, end, start, timeTakenMs, (timeTakenMs / 1000)));

        } catch (IOException | InterruptedException e) {
            MovieRepository.getMovies().postValue(null);
            Log.e(TAG, e.getMessage());
        }
    }

    static synchronized void syncFavoriteMovies(MovieDao movieDao) {
        Log.d(TAG, "syncFavoriteMovies - Preparing request for favorite movies.");
        MovieRepository.getMovies().postValue(movieDao.getFavoriteMovies());
        Log.d(TAG, "syncFavoriteMovies - Completed request for favorite movies.");
    }

    static synchronized void syncMovieVideos(MovieDao movieDao, long movieId) {
        try {

            long start = System.currentTimeMillis();
            Log.d(TAG, String.format("syncMovieVideos - Preparing request for movie videos: %d:", movieId));
            Response response = sMovieDbService.getMovieVideos(movieId, API_KEY).execute();
            Log.d(TAG, String.format("syncMovieVideos - Movie videos response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));
            MovieVideosWrapper movieVideosWrapper = (MovieVideosWrapper) response.body();

            if (movieVideosWrapper == null) {
                throw new IOException("Null response!");
            }

            List<MovieVideo> movieVideoList = movieVideosWrapper.getMovieVideoList();

            for (MovieVideo movieVideo : movieVideoList) {
                movieVideo.movieDbid = movieId;
            }

            Log.d(TAG, String.format("syncMovieVideos - Inserting movie videos: movieVideoList:%s", Arrays.toString(movieVideoList.toArray())));
            movieDao.insertMovieVideos(movieVideoList);

            MovieRepository.getMovieVideos().postValue(movieDao.getMovieVideos(movieId));

            long end = System.currentTimeMillis();

            long timeTakenMs = end - start;

            Log.d(TAG, String.format("Movie Videos insert for movie ID %d:(end - start) = timeTakenMs -> (%d - %d) = %d MS = %d S", movieId, end, start, timeTakenMs, (timeTakenMs / 1000)));

        } catch (IOException e) {
            MovieRepository.getMovieVideos().postValue(null);
            Log.e(TAG, e.getMessage());
        }
    }

    static synchronized void syncMovieReviews(MovieDao movieDao, long movieId) {
        try {

            long start = System.currentTimeMillis();
            Log.d(TAG, String.format("syncMovieReviews - Preparing request for movie reviews: %d:", movieId));
            Response response = sMovieDbService.getMovieReviews(movieId, API_KEY).execute();
            Log.d(TAG, String.format("syncMovieReviews - Movie reviews response: code:%s, body:%s, errorBody:%s", response.code(), response.body(), response.errorBody()));
            MovieReviewsWrapper movieReviewsWrapper = (MovieReviewsWrapper) response.body();

            if (movieReviewsWrapper == null) {
                throw new IOException("Null response!");
            }

            List<MovieReview> movieReviewList = movieReviewsWrapper.getMovieReviewList();

            for (MovieReview movieReview : movieReviewList) {
                movieReview.movieDbid = movieId;
            }

            Log.d(TAG, String.format("syncMovieReviews - Inserting movie reviews: movieVideoList:%s", Arrays.toString(movieReviewList.toArray())));
            movieDao.insertMovieReviews(movieReviewList);

            MovieRepository.getMovieReviews().postValue(movieDao.getMovieReviews(movieId));

            long end = System.currentTimeMillis();

            long timeTakenMs = end - start;

            Log.d(TAG, String.format("Movie Reviews insert for movie ID %d:(end - start) = timeTakenMs -> (%d - %d) = %d MS = %d S", movieId, end, start, timeTakenMs, (timeTakenMs / 1000)));

        } catch (IOException e) {
            MovieRepository.getMovieVideos().postValue(null);
            Log.e(TAG, e.getMessage());
        }
    }


}
