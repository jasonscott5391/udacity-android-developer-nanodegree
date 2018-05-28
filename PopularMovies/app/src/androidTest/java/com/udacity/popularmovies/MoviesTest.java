package com.udacity.popularmovies;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.popularmovies.data.Movie;
import com.udacity.popularmovies.data.MovieDao;
import com.udacity.popularmovies.data.MovieDatabase;
import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.TopRatedMovie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MoviesTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final int TEST_NUM_MOVIES = 5;

    private MovieDatabase mMovieDatabase;


    @Before
    public void createDatabase() {
        mMovieDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                MovieDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDatabase() {
        mMovieDatabase.close();
    }

    @Test
    public void testInsertMovies() {
        assertEquals(mMovieDatabase.movies().getMovieCount(), 0);
        assertEquals(mMovieDatabase.movies().getPopularMovieCount(), 0);
        assertEquals(mMovieDatabase.movies().getTopRatedMovieCount(), 0);
        insertMovies(TEST_NUM_MOVIES);
        assertEquals(mMovieDatabase.movies().getMovieCount(), TEST_NUM_MOVIES);
        assertEquals(mMovieDatabase.movies().getPopularMovieCount(), (TEST_NUM_MOVIES / 2) + 1);
        assertEquals(mMovieDatabase.movies().getTopRatedMovieCount(), TEST_NUM_MOVIES / 2);
    }

    @Test
    public void testSelectMovieById() {
        insertMovies(TEST_NUM_MOVIES);
        int random = randomInt(1, TEST_NUM_MOVIES);

        Movie movie = mMovieDatabase.movies().getMovieById(Integer.toUnsignedLong(random));
        assertEquals(Long.valueOf(Integer.toUnsignedLong(random)), movie.id);
        assertEquals(String.format("TEST_ORIGINAL_TITLE_%d", random), movie.originalTitle);
        assertEquals(String.format("TEST_POSTER_PATH_%d", random), movie.posterPath);
        assertEquals(String.format("TEST_OVERVIEW_%d", random), movie.overview);
        assertEquals(Double.valueOf((double) random), movie.voteAverage);
        assertEquals(String.format("TEST_RELEASE_DATE_%d", random), movie.releaseDate);
    }

    @Test
    public void testSelectPopularMovies() {
        insertMovies(TEST_NUM_MOVIES);
        List<MovieDao.BaseMovie> popularMovieList = mMovieDatabase.movies().getPopularMovies();
        for (int i = 0; i < TEST_NUM_MOVIES; i++) {
            if (i % 2 == 0) {
                MovieDao.BaseMovie popularMovie = popularMovieList.remove(0);
                int id = i + 1;
                assertEquals(Long.valueOf(Integer.toUnsignedLong(id)), popularMovie.id);
                assertEquals(String.format("TEST_POSTER_PATH_%d", id), popularMovie.posterPath);
            }
        }
    }

    @Test
    public void testSelectTopRatedMovies() {
        insertMovies(TEST_NUM_MOVIES);
        List<MovieDao.BaseMovie> topRatedMovieList = mMovieDatabase.movies().getTopRatedMovies();
        for (int i = 0; i < TEST_NUM_MOVIES; i++) {
            if (i % 2 != 0) {
                MovieDao.BaseMovie topRatedMovie = topRatedMovieList.remove(0);
                int id = i + 1;
                assertEquals(Long.valueOf(Integer.toUnsignedLong(id)), topRatedMovie.id);
                assertEquals(String.format("TEST_POSTER_PATH_%d", id), topRatedMovie.posterPath);
            }
        }
    }

    private void insertMovies(int numMovies) {
        List<Movie> movieList = new ArrayList<>();
        List<PopularMovie> popularMovieList = new ArrayList<>();
        List<TopRatedMovie> topRatedMovieList = new ArrayList<>();

        for (int i = 0; i < numMovies; i++) {
            int id = i + 1;

            Movie movie = new Movie();

            movie.id = Integer.toUnsignedLong(id);
            movie.originalTitle = String.format("TEST_ORIGINAL_TITLE_%d", id);
            movie.posterPath = String.format("TEST_POSTER_PATH_%d", id);
            movie.overview = String.format("TEST_OVERVIEW_%d", id);
            movie.voteAverage = (double) id;
            movie.releaseDate = String.format("TEST_RELEASE_DATE_%d", id);

            movieList.add(movie);

            if (i % 2 == 0) {
                PopularMovie popularMovie = new PopularMovie();
                popularMovie.id = Integer.toUnsignedLong(id);
                popularMovieList.add(popularMovie);
            } else {
                TopRatedMovie topRatedMovie = new TopRatedMovie();
                topRatedMovie.id = Integer.toUnsignedLong(id);
                topRatedMovieList.add(topRatedMovie);
            }
        }

        mMovieDatabase.movies().insertMovies(movieList);
        mMovieDatabase.movies().insertPopularMovies(popularMovieList);
        mMovieDatabase.movies().insertTopRatedMovies(topRatedMovieList);
    }

    private int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

}
