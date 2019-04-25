package com.edhuaranga.crisproject.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edhuaranga.crisproject.R;
import com.edhuaranga.crisproject.model.User;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {
    Context context;
    ArrayList<JSONObject> jsonUsers;

    public RankingAdapter(Context context, ArrayList<JSONObject> jsonUsers){
        this.context = context;
        this.jsonUsers = jsonUsers;
    }

    @NonNull
    @Override
    public RankingAdapter.RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_rank_user, viewGroup, false);
        return new RankingAdapter.RankingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.RankingViewHolder rankViewHolder, int i) {

        JSONObject jsonUser = jsonUsers.get(i);
        try{
            rankViewHolder.textViewPosition.setText((i+1)+"");
            rankViewHolder.textViewName.setText(jsonUser.getString("name"));
            rankViewHolder.textViewCity.setText(jsonUser.getString("city"));
            rankViewHolder.textViewPoints.setText(jsonUser.getString("points"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonUsers.size();
    }


    public class RankingViewHolder extends RecyclerView.ViewHolder{

        TextView textViewPoints;
        TextView textViewName;
        TextView textViewCity;
        TextView textViewPosition;


        public RankingViewHolder(View itemView) {
            super(itemView);
            textViewPosition = itemView.findViewById(R.id.textView_position);
            textViewCity = itemView.findViewById(R.id.textview_city);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewPoints = itemView.findViewById(R.id.textView_points);
        }
    }
}
