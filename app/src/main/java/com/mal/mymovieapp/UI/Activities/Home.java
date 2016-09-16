package com.mal.mymovieapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mal.mymovieapp.Network.API.MoviesGetter;
import com.mal.mymovieapp.UI.Adapters.HomeAdapter;
import com.mal.mymovieapp.Storage.Database.FavouritesDatabase;
import com.mal.mymovieapp.Models.Movie;
import com.mal.mymovieapp.R;
import com.mal.mymovieapp.UI.Fragments.Details;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ArrayList<Movie> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    public static FavouritesDatabase database;
    private LinearLayout home;
    private boolean tabletView = false;
    private int screenWidth = 0;
    private boolean doubleBackToExitPressedOnce = false;
    private String key;
    private static Home homeActivity;
    private final Snackbar[] snackBar = new Snackbar[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    public static void updateFavourites(){
        if (Home.homeActivity.getTitle().equals("My favourites")) {
            Home.homeActivity.movies = database.getAllMovies();
            Home.homeActivity.setRecyclerViewContents();
        }
    }

    private void init(){
        homeActivity = this;
        tabletView = findViewById(R.id.movie_details_container) != null;
        home = (LinearLayout)findViewById(R.id.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Most popular");
        key = getString(R.string.api_key);
        snackBar[0] = Snackbar.make(home, "sd...", Snackbar.LENGTH_INDEFINITE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        database = new FavouritesDatabase(this);
        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
        setContentType(MoviesGetter.MOST_POPULAR);
    }

    private boolean isNetworkAvailable(){
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private void setContentType(String sortType){
        final Snackbar[] snackBar = new Snackbar[1];
        MoviesGetter moviesGetter;
        if (isNetworkAvailable()){
            snackBar[0] = Snackbar.make(home, "Loading...", Snackbar.LENGTH_LONG);
            snackBar[0].show();
            moviesGetter = new MoviesGetter() {
                @Override
                public void onPost(ArrayList<Movie> list) {
                    if (list == null){
                        snackBar[0] = Snackbar.make(home,
                                "Error retrieving data from server, please try again later.",
                                Snackbar.LENGTH_INDEFINITE);
                        snackBar[0].setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackBar[0].dismiss();
                            }
                        })
                                .setActionTextColor(Color.GREEN);
                        snackBar[0].show();
                    }
                    else {
                        Home.this.movies = list;
                        setRecyclerViewContents();
                    }
                }
            };
            moviesGetter.execute(sortType, key);
        }
        else {
            snackBar[0] = Snackbar.make(home,
                    "Please check your internet connection.",
                    Snackbar.LENGTH_INDEFINITE);
            snackBar[0].setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar[0].dismiss();
                }
            }).setActionTextColor(Color.GREEN);
            snackBar[0].show();
        }
    }

    private void setRecyclerViewContents(){
        HomeAdapter adapter = new HomeAdapter(Home.this, movies, tabletView);
        recyclerView.setLayoutManager(new GridLayoutManager(Home.this, getItemsPerRow()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (movies.isEmpty()){
            snackBar[0] = Snackbar.make(home, "Nothing to show.", Snackbar.LENGTH_INDEFINITE);
            snackBar[0].show();
        }
        else {
            snackBar[0].dismiss();
            if (tabletView){
                Bundle arguments = new Bundle();
                arguments.putString("object", adapter.getFirstMovieJSON());
                arguments.putBoolean("fromDetailsActivity", false);
                com.mal.mymovieapp.UI.Fragments.Details details
                        = new Details();
                details.setArguments(arguments);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movie_details_container, details)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_most_popular:
                setContentType(MoviesGetter.MOST_POPULAR);
                setTitle("Most popular");

                break;
            case R.id.action_top_rated:
                setContentType(MoviesGetter.TOP_RATED);
                setTitle("Top rated");
                break;
            case R.id.action_my_favourites:
                setTitle("My favourites");
                movies = database.getAllMovies();
                setRecyclerViewContents();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getItemsPerRow(){
        if (tabletView){
            return (screenWidth - 480) / 160;
        }
        else {
            return screenWidth / 160;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static Intent goingHome(Context context){
        return new Intent(context, Home.class);
    }
}
