package com.udacity.popularmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.udacity.popularmovies.entity.FavoriteMovie;
import com.udacity.popularmovies.entity.Movie;
import com.udacity.popularmovies.entity.MovieReview;
import com.udacity.popularmovies.entity.MovieVideo;
import com.udacity.popularmovies.entity.PopularMovie;
import com.udacity.popularmovies.entity.TopRatedMovie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT COUNT(*) FROM " + Movie.MOVIES_TABLE_NAME)
    int getMovieCount();

    @Query("SELECT COUNT(*) FROM " + PopularMovie.POPULAR_MOVIES_TABLE_NAME)
    int getPopularMovieCount();

    @Query("SELECT COUNT(*) FROM " + TopRatedMovie.TOP_RATED_MOVIES_TABLE_NAME)
    int getTopRatedMovieCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovies(List<Movie> movieList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPopularMovies(List<PopularMovie> popularMovieList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTopRatedMovies(List<TopRatedMovie> topRatedMovieList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovieVideos(List<MovieVideo> movieVideoList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovieReviews(List<MovieReview> movieReviewList);

    @Insert
    long insertFavoriteMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT "
            + Movie.MOVIES_TABLE_NAME + ".*, "
            + FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME + "." + FavoriteMovie.COLUMN_FAVORITE_MOVIE_ID + " as favoriteMovieId"
            + " FROM " + Movie.MOVIES_TABLE_NAME
            + " LEFT JOIN " + FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME
            + " ON (" + FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID  + " = " + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + ")"
            + " WHERE " + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " = :id")
    Movie getMovieById(long id);

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

    @Query("SELECT "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " AS id, "
            + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_POSTER_PATH + " AS posterPath"
            + " FROM " + FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME
            + " INNER JOIN " + Movie.MOVIES_TABLE_NAME
            + " ON (" + Movie.MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + " = " + FavoriteMovie.FAVORITE_MOVIES_TABLE_NAME + "." + Movie.COLUMN_MOVIE_DB_ID + ")"
            + " ORDER BY " + FavoriteMovie.COLUMN_FAVORITE_MOVIE_ID + " ASC")
    List<BaseMovie> getFavoriteMovies();


    @Query("SELECT "
            + MovieVideo.COLUMN_YOUTUBE_KEY + ", "
            + MovieVideo.COLUMN_NAME
            + " FROM " + MovieVideo.MOVIE_VIDEOS_TABLE_NAME
            + " WHERE " + Movie.COLUMN_MOVIE_DB_ID + " = :id")
    List<MovieVideo> getMovieVideos(long id);

    @Query("SELECT "
            + MovieReview.COLUMN_MOVIE_REVIEW_AUTHOR + ", "
            + MovieReview.COLUMN_MOVIE_REVIEW_CONTENT
            + " FROM " + MovieReview.MOVIE_REVIEWS_TABLE_NAME
            + " WHERE " + Movie.COLUMN_MOVIE_DB_ID + " = :id")
    List<MovieReview> getMovieReviews(long id);


    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    class BaseMovie {
        public Long id;
        public String posterPath;
    }
}
