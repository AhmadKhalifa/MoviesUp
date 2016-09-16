package com.mal.mymovieapp.UI.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mal.mymovieapp.Network.API.ReviewsGetter;
import com.mal.mymovieapp.Network.API.TrailersGetter;
import com.mal.mymovieapp.UI.Activities.Home;
import com.mal.mymovieapp.UI.Adapters.ReviewAdapter;
import com.mal.mymovieapp.UI.Adapters.TrailerAdapter;
import com.mal.mymovieapp.Network.JSON.Movies.MovieBuilder;
import com.mal.mymovieapp.Models.Movie;
import com.mal.mymovieapp.Models.Review;
import com.mal.mymovieapp.Models.Trailer;
import com.mal.mymovieapp.R;
import com.mal.mymovieapp.Utilities.DateFormatter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Details extends Fragment implements View.OnClickListener{

    private Movie movie;
    private Button addToFavourite;
    private RecyclerView trailersRecyclerView;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private TextView noTrailers;
    private TextView noReviews;
    private ImageButton back;
    private boolean activity;
    private String key;

    public Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.content_details, container, false);
        key = getString(R.string.api_key);
        try {
            Pair<Boolean, Movie> successMoviePair = MovieBuilder.build(new JSONObject(getArguments().getString("object")));
            activity = getArguments().getBoolean("fromDetailsActivity");
            if (successMoviePair.first){
                movie = successMoviePair.second;
            }
            else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.error)
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final LinearLayout innerImage = (LinearLayout)view.findViewById(R.id.inner_image);
        back = (ImageButton)view.findViewById(R.id.back);
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        TextView voters = (TextView)view.findViewById(R.id.voters);
        TextView overview = (TextView) view.findViewById(R.id.overview);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView rate = (TextView) view.findViewById(R.id.rating);
        ImageView imageView = (ImageView)view.findViewById(R.id.myImage);
        noReviews = (TextView) view.findViewById(R.id.no_reviews);
        noTrailers = (TextView) view.findViewById(R.id.no_trailers);
        addToFavourite = (Button)view.findViewById(R.id.add_to_favourite_button);
        if (Home.database.movieExists(String.valueOf(movie.getId()))){
            addToFavourite.setBackgroundColor(getResources().getColor(R.color.yellow));
            addToFavourite.setText("Remove from favourites");
        }
        addToFavourite.setOnClickListener(this);
        back.setVisibility(activity ? View.VISIBLE : View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
        try{
            title.setText(movie.getTitle());
            voters.append("" + movie.getVoteCount());
            overview.setText(movie.getOverview());
            date.setText("Date: " + DateFormatter.format(movie.getReleaseDate()));
            rate.setText(String.format("%.2f", movie.getRate() / 2));

            Picasso.
                    with(getContext()).
                    load("http://image.tmdb.org/t/p/w780/" + movie.getBackdropURL()).
                    resize(780, 439).
                    centerCrop().
                    into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            innerImage.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.d("TAG", "FAILED");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.d("TAG", "Prepare Load");
                        }
                    });

            Picasso.
                    with(getContext()).
                    load("http://image.tmdb.org/t/p/w780/" + movie.getPosterURL()).
                    into(imageView);
        }
        catch (NullPointerException e){
            Log.d("TAG", "FAILED");
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrailersGetter trailersGetter = new TrailersGetter() {
            @Override
            public void onPost(ArrayList<Trailer> list) {
                if (list.size() == 0){
                    noTrailers.setVisibility(View.VISIBLE);
                }
                trailersRecyclerView = (RecyclerView)view.findViewById(R.id.details_trailers_recycler_view);
                trailerAdapter = new TrailerAdapter(getContext(), list);
                trailersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                trailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                trailersRecyclerView.setAdapter(trailerAdapter);
            }
        };
        trailersGetter.execute(String.valueOf(movie.getId()), key);
        ReviewsGetter reviewsGetter = new ReviewsGetter() {
            @Override
            public void onPost(ArrayList<Review> list) {
                if (list.size() == 0){
                    noReviews.setVisibility(View.VISIBLE);
                }
                reviewsRecyclerView = (RecyclerView)view.findViewById(R.id.details_reviews_recycler_view);
                reviewAdapter = new ReviewAdapter(getContext(), list);
                reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                reviewsRecyclerView.setAdapter(reviewAdapter);
            }
        };
        reviewsGetter.execute(String.valueOf(movie.getId()), key);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_to_favourite_button){
            if (addToFavourite.getText().equals("Add to favourites")){
                boolean added = Home.database.addToFavourites(movie);
                Snackbar.make(v, added ? "Successfully added." : "An error occurred.",
                        Snackbar.LENGTH_LONG)
                        .show();
                if (added){
                    addToFavourite.setBackgroundColor(getResources().getColor(R.color.yellow));
                    addToFavourite.setText("Remove from favourites");
                }
            } else {
                boolean removed = Home.database.removeFromFavourites(movie);
                Snackbar.make(v, removed ? "Successfully removed." : "An error occurred.",
                        Snackbar.LENGTH_LONG)
                        .show();
                if (removed){
                    addToFavourite.setBackgroundColor(getResources().getColor(R.color.green));
                    addToFavourite.setText("Add to favourites");
                }
            }
            Home.updateFavourites();
        }
    }
}
