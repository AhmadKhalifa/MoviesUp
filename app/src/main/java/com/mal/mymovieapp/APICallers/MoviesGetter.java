package com.mal.mymovieapp.APICallers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.mal.mymovieapp.Activities.Home;
import com.mal.mymovieapp.Adapters.HomeAdapter;
import com.mal.mymovieapp.Objects.Movie;
import com.mal.mymovieapp.Utilities.Global;
import com.mal.mymovieapp.Builders.MovieBuilder;

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

public class MoviesGetter extends AsyncTask<Integer, String, String> {

    public static int MOST_POPULAR = 0;
    public static int TOP_RATED = 1;

    private int queryCode;
    private ProgressDialog progressDialog;
    private String[] queryType = {"popular", "top_rated"};


    @Override
    protected String doInBackground(Integer... params) {
        queryCode = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String data = null;
        try {
            URL url = new URL(
                    "http://api.themoviedb.org/3/movie/" +
                            queryType[queryCode] +
                            "?api_key=be1f8a35b0e7e4b3be7c1ce2b1e8cab8"
            );

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
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
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
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
                Home.movies = new ArrayList<>();
                Pair<Boolean, Movie> successMoviePair;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    successMoviePair = MovieBuilder.build(jsonobject);
                    error = !successMoviePair.first;
                    if (!error){
                        Home.movies.add(successMoviePair.second);
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
            new AlertDialog.Builder(Global.homeContext)
                    .setTitle("Error")
                    .setMessage(
                            "Error retrieving data from server, please try again later."
                    )
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.stat_notify_error)
                    .show();
        }
        else {
            Home.adapter = new HomeAdapter(Global.homeContext, Home.movies);
            //RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(Global.homeContext, 2);

            if(Global.homeContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                Home.recyclerView.setLayoutManager(new GridLayoutManager(Global.homeContext, 2));
            }
            else{
                Home.recyclerView.setLayoutManager(new GridLayoutManager(Global.homeContext, 3));
            }


            //Home.recyclerView.setLayoutManager(gridLayoutManager);
            Home.recyclerView.setItemAnimator(new DefaultItemAnimator());
            Home.recyclerView.setAdapter(Home.adapter);
        }
    }
}