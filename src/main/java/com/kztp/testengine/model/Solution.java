package com.kztp.testengine.model;

public class Solution {
    private int id;
    private String solution;

    public Solution() {
    }

    public Solution(int id, String solution) {
        this.id = id;
        this.solution = solution;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
