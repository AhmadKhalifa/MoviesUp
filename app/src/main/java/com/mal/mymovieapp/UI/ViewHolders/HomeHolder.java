package com.mal.mymovieapp.UI.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mal.mymovieapp.R;

public class HomeHolder extends RecyclerView.ViewHolder{
    public LinearLayout layout;
    public ImageView poster;
    public HomeHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout)itemView.findViewById(R.id.grid_item);
        poster = (ImageView)itemView.findViewById(R.id.movie_poster);
    }
}