package com.edhuaranga.crisproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edhuaranga.crisproject.Adapters.QuizAdapter;
import com.edhuaranga.crisproject.Adapters.RankingAdapter;
import com.edhuaranga.crisproject.model.Question;
import com.edhuaranga.crisproject.model.Quiz;
import com.edhuaranga.crisproject.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class RankingFragment extends Fragment implements
        Response.ErrorListener, Response.Listener<String> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    RankingAdapter rankingAdapter;
    ArrayList<JSONObject> jsonObjectsRanking;
    RecyclerView recyclerViewRanking;

    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        recyclerViewRanking = view.findViewById(R.id.recycler_rank_users);
        recyclerViewRanking.setLayoutManager(new LinearLayoutManager(getContext()));
        jsonObjectsRanking = new ArrayList<>();

        rankingAdapter = new RankingAdapter(getContext(), jsonObjectsRanking);
        recyclerViewRanking.setAdapter(rankingAdapter);

        requestRanking();

        return view;
    }

    private void requestRanking() {
        String url = Constants.BACKEND_URL+"ranking";

        RESTManager.getInstance(getActivity().getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_RANKING,
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
        ArrayList<JSONObject> rankedUsers = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArrayRankedUsers = jsonObject.getJSONArray("data");

            for(int i=0; i<jsonArrayRankedUsers.length(); i++){
                JSONObject jsonObjectUSer = jsonArrayRankedUsers.getJSONObject(i);
                rankedUsers.add(jsonObjectUSer);
            }
            updateRecycler(rankedUsers);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateRecycler(ArrayList<JSONObject> serverQuizes) {
        jsonObjectsRanking.clear();
        Log.d("HomeFragment", serverQuizes.size()+" added");
        jsonObjectsRanking.addAll(serverQuizes);
        rankingAdapter.notifyDataSetChanged();
    }
}
