package com.mal.mymovieapp.Network.JSON.Trailers;

import android.util.Pair;
import com.mal.mymovieapp.Models.Trailer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ACali on 9/10/2016.
 */
public class TrailerBuilderJSON {
    public static Pair<Boolean, JSONObject> build(Trailer trailer){

        JSONObject object = new JSONObject();
        try {
            object.put("id", trailer.getId());
            object.put("iso_639_1", trailer.getIso_639_1());
            object.put("iso_3166_1", trailer.getIso_3166_1());
            object.put("key", trailer.getKey());
            object.put("name", trailer.getName());
            object.put("site", trailer.getSite());
            object.put("size", trailer.getSize());
            object.put("type", trailer.getType());
            return new Pair<>(true, object);
        }
        catch (JSONException e){
            return new Pair<>(false, new JSONObject());
        }
    }
}
