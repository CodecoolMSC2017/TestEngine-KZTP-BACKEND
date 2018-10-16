package com.kztp.testengine.model;

import java.util.List;

public class NewTest {
    private String title;
    private String description;
    private int price;
    private int maxPoints;
    private List<Question> questions;

    public NewTest(String title, String description, int price, int maxPoints, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.maxPoints = maxPoints;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
