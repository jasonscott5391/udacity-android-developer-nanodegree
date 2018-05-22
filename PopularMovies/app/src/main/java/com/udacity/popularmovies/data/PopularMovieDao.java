package com.udacity.popularmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface PopularMovieDao {

    @Query("SELECT COUNT(*) FROM " + PopularMovie.TABLE_NAME)
    int getCount();

    @Insert
    long[] insertAll(List<PopularMovie> popularMovieList);

    @Query("SELECT " + PopularMovie.COLUMN_ID + ", " + PopularMovie.COLUMN_POSTER_PATH + " FROM " + PopularMovie.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + PopularMovie.TABLE_NAME + " WHERE " + PopularMovie.COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
