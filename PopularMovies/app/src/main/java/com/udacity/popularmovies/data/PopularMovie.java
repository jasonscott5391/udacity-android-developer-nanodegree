package com.udacity.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.data.Movie.COLUMN_MOVIE_DB_ID;
import static com.udacity.popularmovies.data.PopularMovie.POPULAR_MOVIES_TABLE_NAME;

@Entity(tableName = POPULAR_MOVIES_TABLE_NAME, indices = @Index(value = {COLUMN_MOVIE_DB_ID}, unique = true))
public class PopularMovie {

    public static final String POPULAR_MOVIES_TABLE_NAME = "popular_movies";
    public static final String COLUMN_POPULAR_MOVIE_ID = "popular_movie_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_POPULAR_MOVIE_ID)
    public Long popularMovieId;

    @ColumnInfo(name = COLUMN_MOVIE_DB_ID)
    @SerializedName(COLUMN_MOVIE_DB_ID)
    public Long id;

}
