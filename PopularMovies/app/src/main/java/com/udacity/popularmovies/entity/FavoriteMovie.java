package com.udacity.popularmovies.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.entity.FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME;
import static com.udacity.popularmovies.entity.Movie.COLUMN_MOVIE_DB_ID;

@Entity(tableName = FAVORITE_MOVIES_TABLE_NAME, indices = @Index(value = {COLUMN_MOVIE_DB_ID}, unique = true))
public class FavoriteMovie {

    public static final String FAVORITE_MOVIES_TABLE_NAME = "favorite_movies";
    public static final String COLUMN_FAVORITE_MOVIE_ID = "favorite_movie_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_FAVORITE_MOVIE_ID)
    public Long favoriteMovieId;

    @ColumnInfo(name = COLUMN_MOVIE_DB_ID)
    @SerializedName(COLUMN_MOVIE_DB_ID)
    public Long id;
}
