package com.kztp.testengine.model;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Choice> choices;
    private Choice answer;

    public Question() {
    }

    public Question(int id, String text, List<Choice> choices, Choice answer) {
        this.id = id;
        this.text = text;
        this.choices = choices;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Choice getAnswer() {
        return answer;
    }

    public void setAnswer(Choice answer) {
        this.answer = answer;
    }
}
