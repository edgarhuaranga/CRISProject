package com.edhuaranga.ortodontech.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edhuaranga.ortodontech.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class AnswerAdapter  extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>{
    Context context;
    ArrayList<JSONObject> jsonArrayAnswers;


    public AnswerAdapter(Context context, ArrayList<JSONObject> jsonArrayAnswers){
        this.context = context;
        this.jsonArrayAnswers = jsonArrayAnswers;
    }

    @NonNull
    @Override
    public AnswerAdapter.AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_past_answer, viewGroup, false);
        return new AnswerAdapter.AnswerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.AnswerViewHolder answerViewHolder, int i) {
        try{
            JSONObject jsonAnswer = jsonArrayAnswers.get(i);
            Log.d("AnswerAdapter", jsonAnswer.toString());
            answerViewHolder.textViewQuestionStatement.setText(jsonAnswer.getString("question_statement"));
            answerViewHolder.textViewUserAnswer.setText(jsonAnswer.getString("user_answer"));
            answerViewHolder.textViewAnswerState.setText(jsonAnswer.getString("answer_state"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArrayAnswers.size();
    }


    public class AnswerViewHolder extends RecyclerView.ViewHolder{
        TextView textViewQuestionStatement;
        TextView textViewUserAnswer;
        TextView textViewAnswerState;

        public AnswerViewHolder(View itemView){
            super(itemView);
            textViewQuestionStatement = itemView.findViewById(R.id.textview_pastquestion_statement);
            textViewUserAnswer = itemView.findViewById(R.id.textView_user_answer);
            textViewAnswerState = itemView.findViewById(R.id.textView_question_status);
        }
    }
}
