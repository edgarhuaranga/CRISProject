package com.edhuaranga.ortodontech;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edhuaranga.ortodontech.adapters.RankingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RankingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String rankingDetailResponse;
    private JSONArray jsonArrayRankingDetail;

    RankingAdapter rankingAdapter;
    ArrayList<JSONObject> jsonObjectsRanking;
    RecyclerView recyclerViewRanking;

    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(JSONArray rankingDetailJSON) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, rankingDetailJSON.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rankingDetailResponse = getArguments().getString(ARG_PARAM1);
            try {
                jsonArrayRankingDetail = new JSONArray(rankingDetailResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        for(int i=0; i<jsonArrayRankingDetail.length(); i++) {
            try {
                jsonObjectsRanking.add(jsonArrayRankingDetail.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rankingAdapter = new RankingAdapter(getContext(), jsonObjectsRanking);
        recyclerViewRanking.setAdapter(rankingAdapter);
        return view;
    }

}
