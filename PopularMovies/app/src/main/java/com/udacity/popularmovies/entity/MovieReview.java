package com.udacity.popularmovies.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.entity.Movie.COLUMN_MOVIE_DB_ID;
import static com.udacity.popularmovies.entity.MovieReview.COLUMN_MOVIE_REVIEW_AUTHOR;
import static com.udacity.popularmovies.entity.MovieReview.MOVIE_REVIEWS_TABLE_NAME;

@Entity(tableName = MOVIE_REVIEWS_TABLE_NAME, indices = @Index(value = {COLUMN_MOVIE_REVIEW_AUTHOR}, unique = true))
public class MovieReview {

    public static final String MOVIE_REVIEWS_TABLE_NAME = "movie_reviews";
    public static final String COLUMN_MOVIE_REVIEW_ID = "movie_review_id";
    public static final String SERIALIZED_NAME_MOVIE_DB_ID = "movie_db_id";
    public static final String COLUMN_MOVIE_REVIEW_AUTHOR = "author";
    public static final String COLUMN_MOVIE_REVIEW_CONTENT = "content";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_MOVIE_REVIEW_ID)
    public Long movieReviewId;

    @ColumnInfo(name = COLUMN_MOVIE_DB_ID)
    @SerializedName(SERIALIZED_NAME_MOVIE_DB_ID)
    public Long movieDbid;

    @ColumnInfo(name = COLUMN_MOVIE_REVIEW_AUTHOR)
    @SerializedName(COLUMN_MOVIE_REVIEW_AUTHOR)
    public String author;

    @ColumnInfo(name = COLUMN_MOVIE_REVIEW_CONTENT)
    @SerializedName(COLUMN_MOVIE_REVIEW_CONTENT)
    public String content;
}
