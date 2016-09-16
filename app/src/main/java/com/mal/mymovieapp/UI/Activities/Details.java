package com.mal.mymovieapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import com.mal.mymovieapp.R;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startFragment();
    }

    public void startFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("object", getIntent().getExtras().getString("object"));
        arguments.putBoolean("fromDetailsActivity", true);
        com.mal.mymovieapp.UI.Fragments.Details details
                = new com.mal.mymovieapp.UI.Fragments.Details();
        details.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.current_movie_details_container, details)
                .commit();
    }

    public static Intent showDetails (Context context, String JSONString){
        return new Intent(context, Details.class).putExtra("object", JSONString);
    }
}
