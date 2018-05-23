package com.udacity.popularmovies.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PopularMoviesUtils {

    public static final String BASE_MOVIE_DB_URL = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_POSTER_WIDTH = "w185";


    public static void loadNetworkImageIntoView(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    public static String buildPosterImageUrl(String posterPath) {
        return String.format("%s/%s/%s", BASE_MOVIE_DB_URL, DEFAULT_POSTER_WIDTH, posterPath);
    }
}
