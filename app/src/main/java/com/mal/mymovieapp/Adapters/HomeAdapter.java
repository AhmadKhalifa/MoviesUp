package com.mal.mymovieapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mal.mymovieapp.Activities.Details;
import com.mal.mymovieapp.Builders.MovieJSONBuilder;
import com.mal.mymovieapp.Holders.HomeHolder;
import com.mal.mymovieapp.Objects.Movie;
import com.mal.mymovieapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {

    private Context context;
    private ArrayList<Movie> movies;

    public HomeAdapter(Context context, ArrayList<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    @Override
    public HomeHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_movie, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "" + parent.indexOfChild(itemView), Toast.LENGTH_SHORT).show();

            }
        });
        return new HomeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, final int position) {
        final Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.rate.setRating(movie.getRate() / 2);
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
                    v.getContext().startActivity(Details.showDetails(v.getContext(),
                      successJSONobectPair.second.toString()));
                }
                else {
                    new AlertDialog.Builder(context)
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
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
