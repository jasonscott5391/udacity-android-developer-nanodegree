package com.udacity.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PopularMovieDao {

    @Query("SELECT COUNT(*) FROM " + PopularMovie.TABLE_NAME)
    int getCount();

    @Insert
    long[] insertAll(List<PopularMovie> popularMovieList);

    @Query("SELECT " + PopularMovie.COLUMN_ID + ", " + PopularMovie.COLUMN_POSTER_PATH + " FROM " + PopularMovie.TABLE_NAME)
    LiveData<List<PopularMovie>> selectAll();

    @Query("SELECT * FROM " + PopularMovie.TABLE_NAME + " WHERE " + PopularMovie.COLUMN_ID + " = :id")
    PopularMovie selectById(long id);
}
