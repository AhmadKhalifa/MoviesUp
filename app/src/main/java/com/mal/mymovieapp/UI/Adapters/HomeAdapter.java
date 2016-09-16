package com.mal.mymovieapp.UI.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mal.mymovieapp.UI.Activities.Details;
import com.mal.mymovieapp.Network.JSON.Movies.MovieJSONBuilder;
import com.mal.mymovieapp.UI.ViewHolders.HomeHolder;
import com.mal.mymovieapp.Models.Movie;
import com.mal.mymovieapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private boolean tabletView;
    public HomeAdapter(Context context, ArrayList<Movie> movies, boolean tabletView){
        this.context = context;
        this.movies = movies;
        this.tabletView = tabletView;
    }

    @Override
    public HomeHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_movie, parent, false);
        return new HomeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, final int position) {
        final Movie movie = movies.get(position);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w780/" + movie.getPosterURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.black_loading)
                .error(R.drawable.error)
                .into(holder.poster, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load("http://image.tmdb.org/t/p/w780/" + movie.getPosterURL())
                                .into(holder.poster);
                    }
                });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<Boolean, JSONObject> successJSONobectPair =
                        MovieJSONBuilder.build(movies.get(position));
                if (successJSONobectPair.first){
                    if (tabletView){
                        Bundle arguments = new Bundle();
                        arguments.putString("object", successJSONobectPair.second.toString());
                        com.mal.mymovieapp.UI.Fragments.Details details
                                 = new com.mal.mymovieapp.UI.Fragments.Details();
                        details.setArguments(arguments);
                        ((FragmentActivity)context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.movie_details_container, details)
                                .commit();
                    }
                    else {
                        v.getContext().startActivity(Details.showDetails(v.getContext(),
                                successJSONobectPair.second.toString()));
                    }
                }
                else {
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage(
                                    "Error retrieving data from server, please try again later."
                            )
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.stat_notify_error)
                            .show();
                }
            }
        });

    }

    public String getFirstMovieJSON(){
        return MovieJSONBuilder.build(movies.get(0)).second.toString();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
