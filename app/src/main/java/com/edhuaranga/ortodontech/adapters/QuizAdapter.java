package com.edhuaranga.ortodontech.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edhuaranga.ortodontech.Constants;
import com.edhuaranga.ortodontech.R;
import com.edhuaranga.ortodontech.activity.TopicActivity;
import com.edhuaranga.ortodontech.model.Quiz;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    Context context;
    ArrayList<Quiz> quizes;

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
    public void onBindViewHolder(@NonNull QuizViewHolder quizViewHolder, int i) {

        final Quiz quiz = quizes.get(i);

        quizViewHolder.textviewTopic.setText(quiz.getTopic());

        Glide.with(context)
                .load(Uri.parse("http://ortodontech.ehuaranga.com:8000/uploads/icons/"+quiz.getIconURL()+".png"))
                .into(quizViewHolder.imageViewTopicIcon);

        quizViewHolder.cardViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TopicActivity.class);
                intent.putExtra(Constants.EXTRAS_QUIZ, quiz);
                context.startActivity(intent);


                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Você quer começar agora?")
                        .setTitle("novo teste em "+quiz.getTopic())
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();*/
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
