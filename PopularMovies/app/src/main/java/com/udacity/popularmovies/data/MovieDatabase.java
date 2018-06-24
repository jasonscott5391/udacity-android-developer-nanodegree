package com.udacity.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.udacity.popularmovies.entity.FavoriteMovie;
import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.entity.MovieReview;
import com.udacity.popularmovies.entity.MovieVideo;
import com.udacity.popularmovies.entity.PopularMovie;
import com.udacity.popularmovies.entity.TopRatedMovie;

@Database(entities = {Movie.class,
        PopularMovie.class,
        TopRatedMovie.class,
        MovieVideo.class,
        MovieReview.class,
        FavoriteMovie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movies();

    private static final String MOVIE_DATABASE_NAME = "movies";

    private static MovieDatabase sMovieDatabase;

    public static synchronized MovieDatabase getInstance(Context context) {

        if (sMovieDatabase == null) {
            sMovieDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class,
                    MOVIE_DATABASE_NAME)
                    .build();
        }

        return sMovieDatabase;

    }
}
