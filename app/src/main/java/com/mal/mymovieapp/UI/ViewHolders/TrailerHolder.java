package com.mal.mymovieapp.UI.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mal.mymovieapp.R;

public class TrailerHolder extends RecyclerView.ViewHolder{
    public LinearLayout layout;
    public TextView name;
    public TrailerHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout)itemView.findViewById(R.id.trailer_layout);
        name = (TextView)itemView.findViewById(R.id.trailer_name);
    }
}