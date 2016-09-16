package com.mal.mymovieapp.Network.JSON.Trailers;

import android.util.Pair;

import com.mal.mymovieapp.Models.Trailer;

import org.json.JSONException;
import org.json.JSONObject;

public class TrailerBuilder {
    public static Pair<Boolean, Trailer> build(JSONObject object){

        Trailer trailer = new Trailer();

        try{
            trailer.setId(object.getString("id"));
            trailer.setIso_639_1(object.getString("iso_639_1"));
            trailer.setIso_3166_1(object.getString("iso_3166_1"));
            trailer.setKey(object.getString("key"));
            trailer.setName(object.getString("name"));
            trailer.setSite(object.getString("site"));
            trailer.setSize(Integer.parseInt(object.getString("size")));
            trailer.setType(object.getString("type"));

            return new Pair<>(true, trailer);
        }
        catch (JSONException e) {
            return new Pair<>(false, new Trailer());
        }
    }
}
