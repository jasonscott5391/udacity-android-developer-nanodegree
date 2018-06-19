package com.udacity.popularmovies.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static com.udacity.popularmovies.entity.Movie.COLUMN_MOVIE_DB_ID;
import static com.udacity.popularmovies.entity.MovieVideo.COLUMN_NAME;
import static com.udacity.popularmovies.entity.MovieVideo.MOVIE_VIDEOS_TABLE_NAME;

@Entity(tableName = MOVIE_VIDEOS_TABLE_NAME, indices = @Index(value = {COLUMN_NAME}, unique = true))
public class MovieVideo {

    public static final String MOVIE_VIDEOS_TABLE_NAME = "movie_videos";
    public static final String MOVIE_VIDEOS_ID = "movie_video_id";
    public static final String COLUMN_YOUTUBE_KEY = "youtube_key";
    public static final String SERIALIZED_NAME_KEY = "key";
    public static final String SERIALIZED_NAME_MOVIE_DB_ID = "movie_db_id";
    public static final String COLUMN_NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = MOVIE_VIDEOS_ID)
    public Long movieVideoId;

    @ColumnInfo(name = COLUMN_MOVIE_DB_ID)
    @SerializedName(SERIALIZED_NAME_MOVIE_DB_ID)
    public Long movieDbid;

    @ColumnInfo(name = COLUMN_YOUTUBE_KEY)
    @SerializedName(SERIALIZED_NAME_KEY)
    public String youtubeKey;

    @ColumnInfo(name = COLUMN_NAME)
    @SerializedName(COLUMN_NAME)
    public String name;

}
