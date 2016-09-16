package com.mal.mymovieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mal.mymovieapp.Holders.ReviewHolder;
import com.mal.mymovieapp.Models.Review;
import com.mal.mymovieapp.R;

import java.util.ArrayList;

/**
 * Created by ACali on 9/10/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{
    private Context context;
    private ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReviewHolder holder, final int position) {
        final Review review = reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
