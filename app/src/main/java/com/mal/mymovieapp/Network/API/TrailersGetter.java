package com.mal.mymovieapp.Network.API;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.mal.mymovieapp.Network.JSON.Trailers.TrailerBuilder;
import com.mal.mymovieapp.Models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public abstract class TrailersGetter extends AsyncTask<String, String, String> {

    private ArrayList<Trailer> trailers;

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieID = params[0];
        String key = params[1];
        try {
            URL url = new URL(
                    "http://api.themoviedb.org/3/movie/" +
                            movieID +
                            "/videos?api_key=" +
                            key
            );

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(String.format("%s\n", line));
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();

        } catch (IOException e) {
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("", "Error closing stream", e);
                }
            }
        }
    }


    @Override
    protected void onPostExecute(String result) {
        boolean error = true;
        if (result != null && !result.isEmpty()){
            JSONObject JSONResult;
            try {
                JSONResult = new JSONObject(result);
                JSONArray array = JSONResult.getJSONArray("results");
                trailers = new ArrayList<>();
                Pair<Boolean, Trailer> successTrailerPair;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    successTrailerPair = TrailerBuilder.build(jsonobject);
                    error = !successTrailerPair.first;
                    if (!error){
                        trailers.add(successTrailerPair.second);
                    }
                    else {
                        throw new JSONException("");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                error = true;
            }
        }
        if (error){
            onPost(new ArrayList<Trailer>());
        }
        else {
            onPost(trailers);
        }
    }
    public abstract void onPost(ArrayList<Trailer> list);
}