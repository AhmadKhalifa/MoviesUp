package com.mal.mymovieapp.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mal.mymovieapp.UI.ViewHolders.TrailerHolder;
import com.mal.mymovieapp.Models.Trailer;
import com.mal.mymovieapp.R;

import java.util.ArrayList;

/**
 * Created by ACali on 9/10/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerHolder>{
    private Context context;
    private ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers){
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailerHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrailerHolder holder, final int position) {
        final Trailer trailer = trailers.get(position);
        holder.name.setText(trailer.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
