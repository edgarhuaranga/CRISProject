package com.edhuaranga.ortodontech.utils;

import android.util.Log;

import com.edhuaranga.ortodontech.model.Category;
import com.edhuaranga.ortodontech.model.Question;
import com.edhuaranga.ortodontech.model.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObjectBuilderUtil {

    public static Category buildCategoryFromJSON(JSONObject jsonObject){
        Category category = new Category();
        try{
            category.setId(jsonObject.getString("id"));
            category.setCategory(jsonObject.getString("name"));

            ArrayList<Quiz> categoryQuizzes = new ArrayList<>();
            JSONArray jsonArrayQuizzes = jsonObject.getJSONArray("quizzes");
            for(int i=0; i<jsonArrayQuizzes.length(); i++){
                Quiz quiz = buildQuizFromJSON(jsonArrayQuizzes.getJSONObject(i));
                categoryQuizzes.add(quiz);
            }
            category.setQuizes(categoryQuizzes);
            Log.d("CategoryBuilder", category.toString());
        }catch (Exception e){
            Log.d("CategoryBuilder", e.toString());
        }
        return category;
    }

    public static Quiz buildQuizFromJSON(JSONObject jsonObject){
        Quiz quiz = new Quiz();
        try {
            quiz.setId(jsonObject.getString("id"));
            quiz.setTopic(jsonObject.getString("topic"));
            quiz.setIconURL(jsonObject.getString("image_url"));
        } catch (JSONException e) {
            Log.d("ObjectBuilderUtil", "Quiz::"+e.getMessage());
            e.printStackTrace();
        }

        return quiz;
    }

    public static Question buildQuestionFromJSON(JSONObject jsonObject){
        Question question = new Question();
        Log.d("ObjectBuilderUtil", jsonObject.toString());
        try{
            question.setId(jsonObject.getInt("id"));
            question.setImageURL(jsonObject.getString("filename"));
            question.setStatement(jsonObject.getString("statement"));
            question.setOption1(jsonObject.getString("option1"));
            question.setOption2(jsonObject.getString("option2"));
            question.setOption3(jsonObject.getString("option3"));
            question.setOption4(jsonObject.getString("option4"));
            question.setAnswer(jsonObject.getInt("answer"));

        }catch (JSONException e){
            Log.d("ObjectBuilderUtil", "Question::"+e.getMessage());
            e.printStackTrace();
        }

        return question;
    }
}
