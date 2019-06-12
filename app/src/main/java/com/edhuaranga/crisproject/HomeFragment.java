package com.edhuaranga.crisproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edhuaranga.crisproject.Adapters.QuizAdapter;
import com.edhuaranga.crisproject.model.Question;
import com.edhuaranga.crisproject.model.Quiz;
import com.edhuaranga.crisproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    QuizAdapter quizAdapter;
    ArrayList<Quiz> quizes;
    RecyclerView recyclerViewQuestions;
    User user;

    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d("HomeFragment", "Helloooo");
        user = ((OrtodentechApplication)getContext().getApplicationContext()).getUsuario();

        recyclerViewQuestions = view.findViewById(R.id.recycler_questions);
        recyclerViewQuestions.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        quizes = new ArrayList<>();

        quizAdapter = new QuizAdapter(getContext(), quizes);
        recyclerViewQuestions.setAdapter(quizAdapter);

        requestQuestionsBy();

        return view;
    }



    private void requestQuestionsBy(){
        String url = Constants.BACKEND_URL+"topics";

        RESTManager.getInstance(getActivity().getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_QUESTIONS_GROUPEDBY_TOPIC,
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
        ArrayList<Quiz> serverQuizes = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            Iterator<String> topics = jsonObject.keys();

            while(topics.hasNext()) {
                String topic = topics.next();
                Log.d("HomeFragment", topic);

                Quiz quiz = new Quiz();
                ArrayList<Question> questions = new ArrayList<>();

                quiz.setTopic(topic);

                JSONArray arrayQuestions = jsonObject.getJSONArray(topic);
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
            }
            updateRecycler(serverQuizes);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
