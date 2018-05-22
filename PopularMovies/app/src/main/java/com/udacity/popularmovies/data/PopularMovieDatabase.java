package com.udacity.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PopularMovie.class}, version = 1)
public abstract class PopularMovieDatabase extends RoomDatabase {

    public abstract PopularMovieDao popularMovie();

    private static PopularMovieDatabase sPopularMovieDatabase;

    public static synchronized PopularMovieDatabase getInstance(Context context) {

        if (sPopularMovieDatabase == null) {
            sPopularMovieDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    PopularMovieDatabase.class,
                    "popular_movies")
                    .build();
        }

        return sPopularMovieDatabase;

    }
}
