package com.mal.mymovieapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.mal.mymovieapp.APICallers.MoviesGetter;
import com.mal.mymovieapp.Adapters.HomeAdapter;
import com.mal.mymovieapp.Objects.Movie;
import com.mal.mymovieapp.R;
import com.mal.mymovieapp.Utilities.Global;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    public static ArrayList<Movie> movies = new ArrayList<>();
    public static HomeAdapter adapter;
    public static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Most popular");
        Global.homeContext = Home.this;
        new MoviesGetter().execute(MoviesGetter.MOST_POPULAR);
        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
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
                new MoviesGetter().execute(MoviesGetter.MOST_POPULAR);
                setTitle("Most popular");
                break;
            case R.id.action_top_rated:
                new MoviesGetter().execute(MoviesGetter.TOP_RATED);
                setTitle("Top rated");
                break;
            case R.id.action_settings:
                startActivity(SettingsActivity.showSettings(this));
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Home.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent goingHome(Context context){
        return new Intent(context, Home.class);
    }
}
