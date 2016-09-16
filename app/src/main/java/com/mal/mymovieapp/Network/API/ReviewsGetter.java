package com.mal.mymovieapp.Network.API;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.mal.mymovieapp.Network.JSON.Reviews.ReviewBuilder;
import com.mal.mymovieapp.Models.Review;

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

public abstract class ReviewsGetter extends AsyncTask<String, String, String> {

    private ArrayList<Review> reviews;

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
                    "/reviews?api_key=" +
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
                reviews = new ArrayList<>();
                Pair<Boolean, Review> successReviewPair;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    successReviewPair = ReviewBuilder.build(jsonobject);
                    error = !successReviewPair.first;
                    if (!error){
                        reviews.add(successReviewPair.second);
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
            onPost(new ArrayList<Review>());
        }
        else {
            onPost(reviews);
        }
    }
    public abstract void onPost(ArrayList<Review> list);
}