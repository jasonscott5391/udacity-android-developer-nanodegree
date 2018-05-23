package com.udacity.popularmovies;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.popularmovies.data.PopularMovie;
import com.udacity.popularmovies.data.PopularMovieDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class PopularMoviesTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final int TEST_NUM_MOVIES = 5;

    private PopularMovieDatabase mPopularMovieDatabase;


    @Before
    public void createDatabase() {
        mPopularMovieDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                PopularMovieDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDatabase() {
        mPopularMovieDatabase.close();
    }

    @Test
    public void testInsertPopularMovies() {
        assertEquals(mPopularMovieDatabase.popularMovie().getCount(), 0);
        insertPopularMovies(TEST_NUM_MOVIES);
        assertEquals(mPopularMovieDatabase.popularMovie().getCount(), TEST_NUM_MOVIES);
    }

    @Test
    public void testSelectPopularMovieById() {
        insertPopularMovies(TEST_NUM_MOVIES);
        int random = randomInt(1, TEST_NUM_MOVIES);

        PopularMovie popularMovie = mPopularMovieDatabase.popularMovie().selectById(Integer.toUnsignedLong(random));
        assertEquals(Long.valueOf(Integer.toUnsignedLong(random)), popularMovie.id);
        assertEquals(String.format("TEST_ORIGINAL_TITLE_%d", random), popularMovie.originalTitle);
        assertEquals(String.format("TEST_POSTER_PATH_%d", random), popularMovie.posterPath);
        assertEquals(String.format("TEST_OVERVIEW_%d", random), popularMovie.overview);
        assertEquals(Integer.valueOf(random), popularMovie.voteAverage);
        assertEquals(String.format("TEST_RELEASE_DATE_%d", random), popularMovie.releaseDate);
    }

    @Test
    public void testSelectPopularMovies() throws InterruptedException {
        insertPopularMovies(TEST_NUM_MOVIES);
        List<PopularMovie> popularMovieList = getValue(mPopularMovieDatabase.popularMovie().selectAll());
        for (int i = 0; i < TEST_NUM_MOVIES; i++) {
            PopularMovie popularMovie = popularMovieList.get(i);

            int id = i + 1;
            assertEquals(Long.valueOf(Integer.toUnsignedLong(id)), popularMovie.id);
            assertEquals(String.format("TEST_POSTER_PATH_%d", id), popularMovie.posterPath);
            assertNull(popularMovie.originalTitle);
            assertNull(popularMovie.overview);
            assertNull(popularMovie.voteAverage);
            assertNull(popularMovie.releaseDate);
        }
    }

    private void insertPopularMovies(int numMovies) {
        List<PopularMovie> popularMovieList = new ArrayList<>();

        for (int i = 0; i < numMovies; i++) {
            int id = i + 1;

            PopularMovie popularMovie = new PopularMovie();

            popularMovie.id = Integer.toUnsignedLong(id);
            popularMovie.originalTitle = String.format("TEST_ORIGINAL_TITLE_%d", id);
            popularMovie.posterPath = String.format("TEST_POSTER_PATH_%d", id);
            popularMovie.overview = String.format("TEST_OVERVIEW_%d", id);
            popularMovie.voteAverage = id;
            popularMovie.releaseDate = String.format("TEST_RELEASE_DATE_%d", id);

            popularMovieList.add(popularMovie);
        }

        mPopularMovieDatabase.popularMovie().insertAll(popularMovieList);
    }

    private int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }


    private static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] value = new Object[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                value[0] = t;
                countDownLatch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        countDownLatch.await(2, TimeUnit.SECONDS);
        return (T) value[0];
    }
}
