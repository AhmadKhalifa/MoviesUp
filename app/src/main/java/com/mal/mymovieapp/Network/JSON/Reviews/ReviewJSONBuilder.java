package com.mal.mymovieapp.Network.JSON.Reviews;

import android.util.Pair;


import com.mal.mymovieapp.Models.Review;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ACali on 9/10/2016.
 */
public class ReviewJSONBuilder {
    public static Pair<Boolean, JSONObject> build(Review review){

        JSONObject object = new JSONObject();
        try {
            object.put("id", review.getId());
            object.put("author", review.getAuthor());
            object.put("content", review.getContent());
            object.put("url", review.getUrl());
            return new Pair<>(true, object);
        }
        catch (JSONException e){
            return new Pair<>(false, new JSONObject());
        }
    }
}
