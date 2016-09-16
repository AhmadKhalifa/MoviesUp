package com.mal.mymovieapp.Network.JSON.Reviews;

import android.util.Pair;

import com.mal.mymovieapp.Models.Review;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewBuilder {
    public static Pair<Boolean, Review> build(JSONObject object){

        Review review = new Review();

        try{
            review.setId(object.getString("id"));
            review.setAuthor(object.getString("author"));
            review.setContent(object.getString("content"));
            review.setUrl(object.getString("url"));
            return new Pair<>(true, review);
        }
        catch (JSONException e) {
            return new Pair<>(false, new Review());
        }
    }
}
