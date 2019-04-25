package com.edhuaranga.crisproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edhuaranga.crisproject.Adapters.AnswerAdapter;
import com.edhuaranga.crisproject.Adapters.CategoryAdapter;
import com.edhuaranga.crisproject.Adapters.NewAdapter;
import com.edhuaranga.crisproject.Adapters.QuizAdapter;
import com.edhuaranga.crisproject.fragments.ProfileFragment;
import com.edhuaranga.crisproject.model.Category;
import com.edhuaranga.crisproject.model.New;
import com.edhuaranga.crisproject.model.Question;
import com.edhuaranga.crisproject.model.Quiz;
import com.edhuaranga.crisproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class OrtodentechActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String>{

    QuizAdapter quizAdapter;
    CategoryAdapter categoryAdapter;
    ArrayList<Quiz> quizes;
    ArrayList<Category> serverCategories;
    RecyclerView recyclerViewQuestions;
    User user;

    RecyclerView recyclerViewPastAnswers;
    AnswerAdapter answerAdapter;
    ArrayList<JSONObject> jsonArrayAnswers;

    NewAdapter newsAdapter;
    RecyclerView recyclerViewNews;
    ArrayList<New> posts;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d("Mainactivity", "Show questions");
                    recyclerViewQuestions.setVisibility(View.VISIBLE);
                    recyclerViewPastAnswers.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    Log.d("Mainactivity", "Show posts");
                    recyclerViewQuestions.setVisibility(View.GONE);
                    recyclerViewPastAnswers.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_stats:
                    Log.d("Mainactivity", "Show Ranking");
                    recyclerViewQuestions.setVisibility(View.GONE);
                    recyclerViewPastAnswers.setVisibility(View.VISIBLE);
                    recyclerViewNews.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_profile:
                    Log.d("Mainactivity", "Show profile");
                    recyclerViewQuestions.setVisibility(View.GONE);
                    recyclerViewPastAnswers.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.GONE);
                    return true;
            }


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ortodentech);

        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        user = ((OrtodentechApplication)this.getApplicationContext()).getUsuario();

        recyclerViewQuestions = findViewById(R.id.recycler_questions);
        //recyclerViewQuestions.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        quizes = new ArrayList<>();
        quizAdapter = new QuizAdapter(this, quizes);

        serverCategories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, serverCategories);

        //recyclerViewQuestions.setAdapter(quizAdapter);
        recyclerViewQuestions.setAdapter(categoryAdapter);


        recyclerViewPastAnswers = findViewById(R.id.recycler_past_answers);
        recyclerViewPastAnswers.setLayoutManager(new LinearLayoutManager(this));
        jsonArrayAnswers = new ArrayList<>();
        answerAdapter = new AnswerAdapter(this, jsonArrayAnswers);
        recyclerViewPastAnswers.setAdapter(answerAdapter);

        recyclerViewNews = findViewById(R.id.recycler_news);
        //recyclerViewNews.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        posts = new ArrayList<>();
        newsAdapter = new NewAdapter(this, posts);
        recyclerViewNews.setAdapter(newsAdapter);


        requestAnswers();
        requestQuestionsBy();
        requestPosts();
    }

    private void requestQuestionsBy(){
        String url = Constants.BACKEND_URL+"topics?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_QUESTIONS_GROUPEDBY_TOPIC,
                        url,
                        this,
                        this,
                        null);
    }

    private void requestAnswers() {
        String url = Constants.BACKEND_URL+user.getId()+"/answers?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_PAST_ANSWERS,
                        url,
                        this,
                        this,
                        null);
    }

    private void requestPosts() {
        String url = Constants.BACKEND_URL+"posts?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_POSTS,
                        url,
                        this,
                        this,
                        null);
    }

    private void updateRecycler(ArrayList<Quiz> serverQuizes) {
        quizes.clear();
        Log.d("HomeFragment", serverQuizes.size()+" added");
        quizes.addAll(serverQuizes);
        quizAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("CheckActivity", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("Response", response);
        try{
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_QUESTIONS_GROUPEDBY_TOPIC)){
                //handleQuestionsResponse(jsonObject);
                handleCategoriesResponse(jsonObject);
            }

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_PAST_ANSWERS)){
                handleAnswersResponse(jsonObject);
            }

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_POSTS)){
                handlePostsResponse(jsonObject);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handlePostsResponse(JSONObject jsonObject) {
        ArrayList<New> serverPosts= new ArrayList<>();
        try{
            JSONArray jsonArrayPosts = jsonObject.getJSONArray("data");

            for(int i=0; i<jsonArrayPosts.length(); i++){
                JSONObject jsonObjectPost = jsonArrayPosts.getJSONObject(i);
                New post = new New();
                post.setTitle(jsonObjectPost.getString("title"));
                post.setContent(jsonObjectPost.getString("content"));
                post.setPhotoUrl(jsonObjectPost.getString("url_photo"));
                post.setDatePosted(jsonObjectPost.getJSONObject("updated_at").getString("date"));

                serverPosts.add(post);
            }
            posts.clear();
            posts.addAll(serverPosts);
            newsAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }
    }

    private void handleAnswersResponse(JSONObject jsonObject) {
        try{
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();
            JSONArray jsonArrayRankedUsers = jsonObject.getJSONArray("data");


            for(int i=0; i<jsonArrayRankedUsers.length(); i++)
                jsonObjects.add(jsonArrayRankedUsers.getJSONObject(i));
            //updateRecycler(jsonObjects);
            jsonArrayAnswers.clear();
            jsonArrayAnswers.addAll(jsonObjects);
            answerAdapter.notifyDataSetChanged();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleQuestionsResponse(JSONObject jsonObject) {
        ArrayList<Quiz> serverQuizes = new ArrayList<>();
        Iterator<String> topics = null;
        try {
            topics = jsonObject.getJSONObject("data").keys();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        while(topics.hasNext()) {
            String topic = topics.next();
            Log.d("HomeFragment", topic);

            Quiz quiz = new Quiz();
            ArrayList<Question> questions = new ArrayList<>();

            quiz.setTopic(topic);

            try{
                JSONArray arrayQuestions = jsonObject.getJSONObject("data").getJSONArray(topic);
                Log.d("HomeFragment", arrayQuestions.length()+" questions");
                for(int i=0; i<arrayQuestions.length(); i++){
                    Question question = new Question();
                    question.setQuestionId(arrayQuestions.getJSONObject(i).getInt("id"));
                    question.setStatement(arrayQuestions.getJSONObject(i).getString("statement"));
                    question.setOption1(arrayQuestions.getJSONObject(i).getString("option1"));
                    question.setOption2(arrayQuestions.getJSONObject(i).getString("option2"));
                    question.setOption3(arrayQuestions.getJSONObject(i).getString("option3"));
                    question.setOption4(arrayQuestions.getJSONObject(i).getString("option4"));
                    question.setTopic(arrayQuestions.getJSONObject(i).getString("topic"));
                    question.setAnswer(arrayQuestions.getJSONObject(i).getInt("answer"));
                    questions.add(question);
                }

                quiz.setQuestions(questions);
                serverQuizes.add(quiz);
            }catch (Exception e){

            }

        }
        updateRecycler(serverQuizes);
    }

    private void handleCategoriesResponse(JSONObject jsonObjectCategories){
        try {
            ArrayList<Category> categories = new ArrayList<>();
            Iterator<String> categoriesKeys = null;
            JSONObject jsonCategories = jsonObjectCategories.getJSONObject("data");
            categoriesKeys = jsonCategories .keys();

            while (categoriesKeys.hasNext()){
                String categoryLabel = categoriesKeys.next();
                Category category = new Category();
                ArrayList<Quiz> quizzes = new ArrayList<>();
                category.setCategory(categoryLabel);
                JSONObject jsonTopics = jsonCategories.getJSONObject(categoryLabel);
                Iterator<String> topicKeys = jsonTopics.keys();

                while(topicKeys.hasNext()){
                    Quiz quiz = new Quiz();
                    String topicLabel = topicKeys.next();
                    quiz.setTopic(topicLabel);
                    JSONArray jsonArrayQuestions = jsonTopics.getJSONArray(topicLabel);
                    ArrayList<Question> questions = new ArrayList<>();
                    for(int i=0; i<jsonArrayQuestions.length(); i++){
                        Question question = new Question();
                        question.setQuestionId(jsonArrayQuestions.getJSONObject(i).getInt("id"));
                        question.setStatement(jsonArrayQuestions.getJSONObject(i).getString("statement"));
                        question.setOption1(jsonArrayQuestions.getJSONObject(i).getString("option1"));
                        question.setOption2(jsonArrayQuestions.getJSONObject(i).getString("option2"));
                        question.setOption3(jsonArrayQuestions.getJSONObject(i).getString("option3"));
                        question.setOption4(jsonArrayQuestions.getJSONObject(i).getString("option4"));
                        question.setTopic(jsonArrayQuestions.getJSONObject(i).getString("topic"));
                        question.setAnswer(jsonArrayQuestions.getJSONObject(i).getInt("answer"));
                        questions.add(question);
                    }
                    quiz.setQuestions(questions);
                    quizzes.add(quiz);
                }

                category.setQuizes(quizzes);
                categories.add(category);
                updateRecyclerCategories(categories);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void updateRecyclerCategories(ArrayList<Category> categories) {
        serverCategories.clear();
        serverCategories.addAll(categories);
        categoryAdapter.notifyDataSetChanged();
    }
}
