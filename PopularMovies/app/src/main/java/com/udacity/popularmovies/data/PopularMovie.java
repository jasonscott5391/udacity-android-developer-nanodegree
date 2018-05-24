package com.udacity.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.data.PopularMovie.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class PopularMovie {

    public static final String TABLE_NAME = "popular_movies";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_RELEASE_DATE = "release_date";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
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
