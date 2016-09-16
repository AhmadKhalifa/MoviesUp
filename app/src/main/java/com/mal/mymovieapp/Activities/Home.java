package com.mal.mymovieapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mal.mymovieapp.APICallers.MoviesGetter;
import com.mal.mymovieapp.Adapters.HomeAdapter;
import com.mal.mymovieapp.Databases.FavouritesDatabase;
import com.mal.mymovieapp.Models.Movie;
import com.mal.mymovieapp.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ArrayList<Movie> movies = new ArrayList<>();
    private HomeAdapter adapter;
    private RecyclerView recyclerView;
    private MoviesGetter moviesGetter;
    private Snackbar snackbar;
    public static FavouritesDatabase database;
    private LinearLayout home;
    private boolean tabletView = false;
    private int screenWidth = 0;
    private boolean doubleBackToExitPressedOnce = false;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){
        tabletView = findViewById(R.id.movie_details_container) != null;
        home = (LinearLayout)findViewById(R.id.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Most popular");
        key = getString(R.string.api_key);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        database = new FavouritesDatabase(this);
        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
        setContentType(MoviesGetter.MOST_POPULAR);
    }

    private void setContentType(String sortType){
        snackbar = Snackbar.make(home, "Loading...", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        moviesGetter = new MoviesGetter() {
            @Override
            public void onPost(ArrayList<Movie> list) {
                if (list == null){
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.home), "Error retrieving data from server, please try again later.", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            })
                            .setActionTextColor(Color.GREEN);
                    snackbar.show();
                }
                else {
                    Home.this.movies = list;
                    setRecyclerViewContents();
                }
            }
        };
        moviesGetter.execute(sortType, key);
    }

    private void setRecyclerViewContents(){

        adapter = new HomeAdapter(Home.this, movies, tabletView);
        recyclerView.setLayoutManager(new GridLayoutManager(Home.this, getItemsPerRow()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (movies.isEmpty()){
            snackbar = Snackbar.make(home, "Nothing to show.", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
        else {
            snackbar.dismiss();
            if (tabletView){
                Bundle arguments = new Bundle();
                arguments.putString("object", adapter.getFirstMovieJSON());
                arguments.putBoolean("fromDetailsActivity", false);
                com.mal.mymovieapp.Fragments.Details details
                        = new com.mal.mymovieapp.Fragments.Details();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
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
