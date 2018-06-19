package com.udacity.popularmovies.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.entity.Movie.COLUMN_MOVIE_DB_ID;
import static com.udacity.popularmovies.entity.Movie.COLUMN_POSTER_PATH;
import static com.udacity.popularmovies.entity.Movie.MOVIES_TABLE_NAME;

@Entity(tableName = MOVIES_TABLE_NAME, indices = {@Index(value = {COLUMN_POSTER_PATH}),
        @Index(value = {COLUMN_MOVIE_DB_ID}, unique = true)})
public class Movie {

    public static final String MOVIES_TABLE_NAME = "movies";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_DB_ID = "id";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_RELEASE_DATE = "release_date";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_MOVIE_ID)
    public Long movieId;

    @ColumnInfo(name = COLUMN_MOVIE_DB_ID)
    @SerializedName(COLUMN_MOVIE_DB_ID)
    public Long id;

    @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
    @SerializedName(COLUMN_ORIGINAL_TITLE)
    public String originalTitle;

    @ColumnInfo(name = COLUMN_POSTER_PATH)
    @SerializedName(COLUMN_POSTER_PATH)
    public String posterPath;

    @ColumnInfo(name = COLUMN_OVERVIEW)
    @SerializedName(COLUMN_OVERVIEW)
    public String overview;

    @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
    @SerializedName(COLUMN_VOTE_AVERAGE)
    public Double voteAverage;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    @SerializedName(COLUMN_RELEASE_DATE)
    public String releaseDate;
}
