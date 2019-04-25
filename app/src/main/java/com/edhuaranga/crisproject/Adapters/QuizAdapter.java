package com.edhuaranga.crisproject.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edhuaranga.crisproject.Constants;
import com.edhuaranga.crisproject.QuizActivity;
import com.edhuaranga.crisproject.R;
import com.edhuaranga.crisproject.model.Quiz;

import java.util.ArrayList;
import java.util.Random;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    Context context;
    ArrayList<Quiz> quizes;
    int[] colors = {R.color.rosado, R.color.rojo, R.color.sangre,
            R.color.blue_lighter, R.color.blue, R.color.blue_darker,
            R.color.yellow_lighter, R.color.yellow, R.color.yellow_darker,
            R.color.green_lighter, R.color.green, R.color.green_darker,
            R.color.gray_lighter, R.color.gray, R.color.gray_darker,
            R.color.purple_lighter, R.color.purple, R.color.purple_darker,
            R.color.orange_lighter, R.color.orange, R.color.orange_darker,
    };



    public QuizAdapter(Context context, ArrayList<Quiz> quizes){
        this.context = context;
        this.quizes = quizes;
    }

    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_quiz, viewGroup, false);
        return new QuizAdapter.QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.QuizViewHolder quizViewHolder, int i) {

        final Quiz quiz = quizes.get(i);

        int indexColor = (int)(Math.random()* colors.length);
        quizViewHolder.textviewTopic.setText(quiz.getTopic());
        quizViewHolder.imageViewTopicIcon.setBackgroundColor(context.getColor(colors[indexColor]));
        String[] icons = context.getResources().getStringArray(R.array.icons);

        indexColor = (int)(Math.random()*icons.length);

        Glide.with(context)
                .load(Uri.parse("http://ortodentech.ehuaranga.com/uploads/"+icons[indexColor]))
                .into(quizViewHolder.imageViewTopicIcon);

        quizViewHolder.cardViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Você quer começar agora?")
                        .setTitle("novo teste em "+quiz.getTopic())
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(context, QuizActivity.class);
                                intent.putExtra(Constants.EXTRAS_QUIZ, quiz);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return quizes.size();
    }


    public class QuizViewHolder extends RecyclerView.ViewHolder{

        TextView textviewTopic;
        ImageView imageViewTopicIcon;
        CardView cardViewQuiz;


        public QuizViewHolder(View itemView) {
            super(itemView);
            cardViewQuiz = itemView.findViewById(R.id.cardview_quiz);
            imageViewTopicIcon = itemView.findViewById(R.id.imageview_topic_icon);
            textviewTopic = itemView.findViewById(R.id.textview_topic);

        }
    }
}
