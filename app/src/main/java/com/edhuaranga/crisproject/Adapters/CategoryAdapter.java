package com.edhuaranga.crisproject.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edhuaranga.crisproject.R;
import com.edhuaranga.crisproject.model.Category;
import com.edhuaranga.crisproject.model.Quiz;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_category, viewGroup, false);
        return new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryTitle.setText(category.getCategory());


        QuizAdapter quizAdapter;
        RecyclerView recyclerViewQuestions;

        recyclerViewQuestions = holder.recyclerViewQuizes;
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        quizAdapter = new QuizAdapter(context, category.getQuizes());
        recyclerViewQuestions.setAdapter(quizAdapter);

        holder.buttonChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button button = (Button) v;
                if(button.getText().toString().equalsIgnoreCase("MORE")){
                    ((Button) v).setText("Less");
                    GridLayoutManager lmGrid = new GridLayoutManager(context, 4);
                    holder.recyclerViewQuizes.setLayoutManager(lmGrid);
                }
                else{
                    ((Button) v).setText("More");
                        holder.recyclerViewQuizes.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCategoryTitle;
        Button buttonChangeLayout;
        RecyclerView recyclerViewQuizes;

        public CategoryViewHolder(View itemView){
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.textview_category);
            buttonChangeLayout = itemView.findViewById(R.id.button_change_layout);
            recyclerViewQuizes = itemView.findViewById(R.id.recyclerview_quizes);
        }
    }
}
