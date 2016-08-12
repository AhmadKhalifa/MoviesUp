package com.mal.mymovieapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mal.mymovieapp.Builders.MovieBuilder;
import com.mal.mymovieapp.Objects.Movie;
import com.mal.mymovieapp.R;
import com.mal.mymovieapp.Utilities.DateFormatter;
import com.mal.mymovieapp.Utilities.Global;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Movie movie = null;
        try {
            Pair<Boolean, Movie> successMoviePair = MovieBuilder.build(new JSONObject(getIntent().getExtras().getString("object")));
            if (successMoviePair.first){
                movie = successMoviePair.second;
            }
            else {
                new AlertDialog.Builder(Details.this)
                        .setTitle("Error")
                        .setMessage(
                                "Error retrieving data from server, please try again later."
                        )
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Details.this.finish();
                            }
                        })
                        .setIcon(android.R.drawable.stat_notify_error)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final LinearLayout innerImage = (LinearLayout)findViewById(R.id.inner_image);
        ImageButton back = (ImageButton)findViewById(R.id.back);
        TextView title = (TextView) findViewById(R.id.movie_title);
        TextView voters = (TextView)findViewById(R.id.voters);
        TextView overview = (TextView) findViewById(R.id.overview);
        TextView date = (TextView) findViewById(R.id.date);
        RatingBar rate = (RatingBar) findViewById(R.id.rating);
        ImageView imageView = (ImageView)findViewById(R.id.myImage);
        Button addToFavourite = (Button)findViewById(R.id.add_to_favourite_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details.this.finish();
            }
        });

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "This feature will be added soon.", Snackbar.LENGTH_SHORT).show();
            }
        });

        try{
            title.setText(movie.getTitle());
            voters.append("" + movie.getVoteCount());
            overview.setText(movie.getOverview());
            date.setText(DateFormatter.format(movie.getReleaseDate()));
            rate.setRating(movie.getRate() / 2);

            Picasso.
                    with(this).
                    load("http://image.tmdb.org/t/p/w780/" +movie.getBackdropURL()).
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
                    with(this).
                    load("http://image.tmdb.org/t/p/w780/" + movie.getPosterURL()).
                    into(imageView);
        }
        catch (NullPointerException e){
            Log.d("TAG", "FAILED");
        }
    }

    public static Intent showDetails (Context context, String JSONString){
        return new Intent(context, Details.class).putExtra("object", JSONString);
    }

}
