package com.mal.mymovieapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ArrayList<Movie> movies = new ArrayList<>();
    private HomeAdapter adapter;
    private RecyclerView recyclerView;
    private MoviesGetter moviesGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Most popular");
        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
        setContentType(MoviesGetter.MOST_POPULAR);
    }

    private void setContentType(int sortType){
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
        moviesGetter.execute(sortType);
    }

    private void setRecyclerViewContents(){
        adapter = new HomeAdapter(Home.this, movies);
        if(Home.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(Home.this, 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(Home.this, 3));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
                break;
            case R.id.action_top_rated:
                setContentType(MoviesGetter.TOP_RATED);
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
