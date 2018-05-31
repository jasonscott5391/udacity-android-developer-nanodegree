package com.udacity.popularmovies.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PopularMoviesUtils {

    public static final String BASE_MOVIE_DB_URL = "http://image.tmdb.org/t/p/";

    public enum PosterWidth {

        W_342_POSTER_WIDTH("w342"),
        W_500_POSTER_WIDTH("w500"),
        W_780_POSTER_WIDTH("w780");

        PosterWidth(String value) {
            this.value = value;
        }

        private String value;

        private String value() {
            return value;
        }
    }

    public static final PosterWidth DEFAULT_POSTER_WIDTH = PosterWidth.W_500_POSTER_WIDTH;


    public static void loadNetworkImageIntoView(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    public static String buildPosterImageUrl(String posterPath) {
        return String.format("%s/%s/%s", BASE_MOVIE_DB_URL, DEFAULT_POSTER_WIDTH.value(), posterPath);
    }

    public static String buildPosterImageUrl(String posterPath, PosterWidth posterWidth) {
        return String.format("%s/%s/%s", BASE_MOVIE_DB_URL, posterWidth.value(), posterPath);
    }
}
