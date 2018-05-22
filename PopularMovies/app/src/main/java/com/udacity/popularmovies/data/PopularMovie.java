package com.udacity.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;

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
    public String originalTitle;

    @ColumnInfo(name = COLUMN_POSTER_PATH)
    public String posterPath;

    @ColumnInfo(name = COLUMN_OVERVIEW)
    public String overview;

    @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
    public Integer voteAverage;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    public String releaseDate;

    public static PopularMovie fromContentValues(ContentValues values) {
        final PopularMovie popularMovie = new PopularMovie();

        popularMovie.id = values.containsKey(COLUMN_ID) ? values.getAsLong(COLUMN_ID) : null;
        popularMovie.originalTitle = values.containsKey(COLUMN_ORIGINAL_TITLE) ? values.getAsString(COLUMN_ORIGINAL_TITLE) : null;
        popularMovie.posterPath = values.containsKey(COLUMN_POSTER_PATH) ? values.getAsString(COLUMN_POSTER_PATH) : null;
        popularMovie.overview = values.containsKey(COLUMN_OVERVIEW) ? values.getAsString(COLUMN_OVERVIEW) : null;
        popularMovie.voteAverage = values.containsKey(COLUMN_VOTE_AVERAGE) ? values.getAsInteger(COLUMN_VOTE_AVERAGE) : null;
        popularMovie.releaseDate = values.containsKey(COLUMN_RELEASE_DATE) ? values.getAsString(COLUMN_RELEASE_DATE) : null;

        return  popularMovie;
    }
}
