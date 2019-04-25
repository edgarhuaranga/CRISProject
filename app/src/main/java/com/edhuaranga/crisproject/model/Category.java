package com.edhuaranga.crisproject.model;

import java.util.ArrayList;

public class Category {
    String id;
    String category;
    ArrayList<Quiz> quizes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Quiz> getQuizes() {
        return quizes;
    }

    public void setQuizes(ArrayList<Quiz> quizes) {
        this.quizes = quizes;
    }
}
