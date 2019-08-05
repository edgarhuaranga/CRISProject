package com.edhuaranga.ortodontech;


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
import com.edhuaranga.ortodontech.adapters.NewAdapter;
import com.edhuaranga.ortodontech.model.New;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements Response.ErrorListener, Response.Listener<String>{

    NewAdapter newsAdapter;
    RecyclerView recyclerViewNews;
    ArrayList<New> posts;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
        recyclerViewNews = view.findViewById(R.id.recycler_news);

        //recyclerViewNews.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        posts = new ArrayList<>();

        newsAdapter = new NewAdapter(getContext(), posts);
        recyclerViewNews.setAdapter(newsAdapter);

        requestPosts();
        return view;
    }

    private void requestPosts() {
        String url = Constants.BACKEND_URL+"posts";

        RESTManager.getInstance(getActivity().getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_POSTS,
                        url,
                        this,
                        this,
                        null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("PostActivity", error.toString());
    }

    @Override
    public void onResponse(String response) {
        ArrayList<New> serverPosts= new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
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
            updateRecycler(serverPosts);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateRecycler(ArrayList<New> serverPosts) {
        posts.clear();
        posts.addAll(serverPosts);
        newsAdapter.notifyDataSetChanged();
    }

}
