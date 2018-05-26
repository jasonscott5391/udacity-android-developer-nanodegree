package com.udacity.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Movie.class, PopularMovie.class, TopRatedMovie.class}, version = 1)
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
