package com.udacity.popularmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT COUNT(*) FROM " + Movie.MOVIES_TABLE_NAME)
    int getMovieCount();

    @Query("SELECT COUNT(*) FROM " + PopularMovie.POPULAR_MOVIES_TABLE_NAME)
    int getPopularMovieCount();

    @Query("SELECT COUNT(*) FROM " + TopRatedMovie.TOP_RATED_MOVIES_TABLE_NAME)
    int getTopRatedMovieCount();

    @Insert()
    long insertMovie(Movie movie);

    @Insert()
    long[] insertMovies(List<Movie> movieList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPopularMovies(List<PopularMovie> popularMovieList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTopRatedMovies(List<TopRatedMovie> topRatedMovieList);

    @Query("SELECT * FROM " + Movie.MOVIES_TABLE_NAME + " WHERE " + Movie.COLUMN_MOVIE_DB_ID + " = :id")
    Movie getMovieById(long id);

    @Query("SELECT COUNT(*) FROM " + Movie.MOVIES_TABLE_NAME + " WHERE " + Movie.COLUMN_MOVIE_DB_ID + " = :id")
    int getMovieCountById(long id);

    @Query("SELECT "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " AS id, "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_POSTER_PATH + " AS posterPath"
            + " FROM " + PopularMovie.POPULAR_MOVIES_TABLE_NAME
            + " INNER JOIN " + Movie.MOVIES_TABLE_NAME
            + " ON (" + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " = " + PopularMovie.POPULAR_MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + ")"
            + " ORDER BY " + PopularMovie.COLUMN_POPULAR_MOVIE_ID + " ASC")
    List<BaseMovie> getPopularMovies();

    @Query("SELECT "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " AS id, "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_POSTER_PATH + " AS posterPath"
            + " FROM " + TopRatedMovie.TOP_RATED_MOVIES_TABLE_NAME
            + " INNER JOIN " + Movie.MOVIES_TABLE_NAME
            + " ON (" + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " = " + TopRatedMovie.TOP_RATED_MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + ")"
            + " ORDER BY " + TopRatedMovie.COLUMN_TOP_RATED_MOVIE_ID + " ASC")
    List<BaseMovie> getTopRatedMovies();

    class BaseMovie {
        public Long id;
        public String posterPath;
    }
}
