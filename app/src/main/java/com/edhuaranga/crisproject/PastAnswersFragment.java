package com.edhuaranga.crisproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edhuaranga.crisproject.Adapters.AnswerAdapter;
import com.edhuaranga.crisproject.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PastAnswersFragment extends Fragment implements
        Response.ErrorListener, Response.Listener<String>{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerViewPastAnswers;
    AnswerAdapter answerAdapter;
    ArrayList<JSONObject> jsonArrayAnswers;
    User user;

    public PastAnswersFragment() {
        // Required empty public constructor
    }

    public static PastAnswersFragment newInstance(String param1, String param2) {
        PastAnswersFragment fragment = new PastAnswersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_answers, container, false);

        user = ((OrtodentechApplication)this.getActivity().getApplication()).getUsuario();

        jsonArrayAnswers = new ArrayList<>();

        recyclerViewPastAnswers = view.findViewById(R.id.recycler_past_answers);
        recyclerViewPastAnswers.setLayoutManager(new LinearLayoutManager(getContext()));
        answerAdapter = new AnswerAdapter(getContext(), jsonArrayAnswers);
        recyclerViewPastAnswers.setAdapter(answerAdapter);


        requestAnswers();

        return view;
    }

    private void requestAnswers() {
        String url = Constants.BACKEND_URL+user.getId()+"/answers";

        RESTManager.getInstance(getActivity().getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_PAST_ANSWERS,
                        url,
                        this,
                        this,
                        null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("RankingFragment", error.toString());
    }

    @Override
    public void onResponse(String response) {
        try{
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArrayRankedUsers = jsonObject.getJSONArray("data");


            for(int i=0; i<jsonArrayRankedUsers.length(); i++)
                jsonObjects.add(jsonArrayRankedUsers.getJSONObject(i));
            updateRecycler(jsonObjects);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateRecycler(ArrayList<JSONObject> pastAnswers) {
        Log.d("PastAnswers", pastAnswers.toString());
        jsonArrayAnswers.clear();
        jsonArrayAnswers.addAll(pastAnswers);
        answerAdapter.notifyDataSetChanged();
    }

}
