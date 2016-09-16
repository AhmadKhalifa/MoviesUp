package com.mal.mymovieapp.Network.JSON.Movies;

import android.util.Pair;

import com.mal.mymovieapp.Models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieBuilder {
    public static Pair<Boolean, Movie> build(JSONObject object){

            Movie movie = new Movie();

            try{
                movie.setTitle(object.getString("title"));
                movie.setOriginalTitle(object.getString("original_title"));
                movie.setOverview(object.getString("overview"));
                movie.setLanguage(object.getString("original_language"));
                movie.setReleaseDate(object.getString("release_date"));

                movie.setPosterURL(object.getString("poster_path"));
                movie.setBackdropURL(object.getString("backdrop_path"));

                movie.setId(Long.parseLong(object.getString("id")));
                movie.setVoteCount(Integer.parseInt(object.getString("vote_count")));
                movie.setPopularity(Float.parseFloat(object.getString("popularity")));
                movie.setRate(Float.parseFloat(object.getString("vote_average")));

                movie.setAdult(object.getString("adult").equals("true"));
                movie.setVideo(object.getString("video").equals("true"));
                return new Pair<>(true, movie);
            }
            catch (JSONException e) {
                return new Pair<>(false, new Movie());
            }
    }
}
























