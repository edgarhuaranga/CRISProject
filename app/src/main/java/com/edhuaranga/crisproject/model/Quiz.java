package com.edhuaranga.crisproject.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    String id;
    String topic;
    ArrayList<Question> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
