package com.mal.mymovieapp.Network.JSON.Movies;

import android.util.Pair;

import com.mal.mymovieapp.Models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieJSONBuilder {
    public static Pair<Boolean, JSONObject> build(Movie movie){

        JSONObject object = new JSONObject();
        try {
            object.put("title", movie.getTitle());
            object.put("original_title", movie.getOriginalTitle());
            object.put("overview", movie.getOverview());
            object.put("original_language", movie.getLanguage());
            object.put("release_date", movie.getReleaseDate());
            object.put("poster_path", movie.getPosterURL());
            object.put("backdrop_path", movie.getBackdropURL());
            object.put("id", String.valueOf(movie.getId()));
            object.put("vote_count", String.valueOf(movie.getVoteCount()));
            object.put("popularity", String.valueOf(movie.getPopularity()));
            object.put("vote_average", String.valueOf(movie.getRate()));
            object.put("adult", movie.isAdult() ? "true" : "false");
            object.put("video", movie.isVideo() ? "true" : "false");
            return new Pair<>(true, object);
        }
        catch (JSONException e){
            return new Pair<>(false, new JSONObject());
        }
    }
}
