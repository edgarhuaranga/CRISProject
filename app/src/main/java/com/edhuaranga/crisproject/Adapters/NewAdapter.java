package com.edhuaranga.crisproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edhuaranga.crisproject.Constants;
import com.edhuaranga.crisproject.FullpostActivity;
import com.edhuaranga.crisproject.NewsFragment;
import com.edhuaranga.crisproject.R;
import com.edhuaranga.crisproject.model.New;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewViewHolder>{
    Context context;
    ArrayList<New> news;

    public NewAdapter(Context context, ArrayList<New> news){
        this.context = context;
        this.news = news;
    }


    @NonNull
    @Override
    public NewAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_new, viewGroup, false);
        return new NewAdapter.NewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewAdapter.NewViewHolder newViewHolder, int i) {
        final New post = news.get(i);
        newViewHolder.textViewDate.setText(post.getDatePosted());
        newViewHolder.textViewTitle.setText(post.getTitle());

        Glide.with(newViewHolder.imageViewPost)
                .load(Constants.BACKEND_HOSTNAME+post.getPhotoUrl())
                .into(newViewHolder.imageViewPost);

        newViewHolder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullpostActivity.class);
                intent.putExtra(Constants.EXTRAS_NEW, post);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public class NewViewHolder extends RecyclerView.ViewHolder{
        CardView cardViewPost;
        TextView textViewTitle;
        ImageView imageViewPost;
        TextView textViewDate;

        public NewViewHolder(View itemView){
            super(itemView);
            cardViewPost = itemView.findViewById(R.id.cardview_post);
            textViewTitle = itemView.findViewById(R.id.textview_post_title);
            imageViewPost = itemView.findViewById(R.id.imageView);
            textViewDate = itemView.findViewById(R.id.textview_post_date);
        }
    }
}
