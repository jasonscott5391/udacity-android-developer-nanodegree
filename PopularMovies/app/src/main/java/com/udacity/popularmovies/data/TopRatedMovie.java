package com.udacity.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.data.Movie.COLUMN_MOVIE_DB_ID;
import static com.udacity.popularmovies.data.TopRatedMovie.TOP_RATED_MOVIES_TABLE_NAME;

@Entity(tableName = TOP_RATED_MOVIES_TABLE_NAME)
public class TopRatedMovie {

    public static final String TOP_RATED_MOVIES_TABLE_NAME = "top_rated_movies";
    public static final String COLUMN_TOP_RATED_MOVIE_ID = "top_rated_movie_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_TOP_RATED_MOVIE_ID)
    public Long topRatedMovieId;

    @ColumnInfo(index = true, name = COLUMN_MOVIE_DB_ID)
    @SerializedName(COLUMN_MOVIE_DB_ID)
    public Long id;

}
