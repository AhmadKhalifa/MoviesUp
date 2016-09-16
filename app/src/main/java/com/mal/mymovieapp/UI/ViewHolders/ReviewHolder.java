package com.mal.mymovieapp.UI.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mal.mymovieapp.R;

public class ReviewHolder extends RecyclerView.ViewHolder{
    public LinearLayout layout;
    public TextView author;
    public TextView content;
    public ReviewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout)itemView.findViewById(R.id.review_layout);
        author = (TextView)itemView.findViewById(R.id.review_author);
        content = (TextView)itemView.findViewById(R.id.review_content);
    }
}