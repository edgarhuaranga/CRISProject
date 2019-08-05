package com.edhuaranga.ortodontech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edhuaranga.ortodontech.Constants;
import com.edhuaranga.ortodontech.R;
import com.edhuaranga.ortodontech.model.New;

public class FullpostActivity extends AppCompatActivity {

    New post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpost);

        post = (New) getIntent().getSerializableExtra(Constants.EXTRAS_NEW);

        Log.d("FullPost", post.toString());
        TextView textViewTitle = findViewById(R.id.textView_post_title);
        TextView textViewContent = findViewById(R.id.textview_post_content);

        Glide.with(this)
                .load(Constants.BACKEND_HOSTNAME+post.getPhotoUrl())
                .into((ImageView)findViewById(R.id.imageview_post));

        textViewTitle.setText(post.getTitle());
        textViewContent.setText(post.getContent());

    }
}
