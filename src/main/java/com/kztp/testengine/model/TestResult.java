package com.kztp.testengine.model;

import java.util.List;

public class TestResult {
    private int percentage;
    private List<Solution> solutions;

    public TestResult() {
    }

    public TestResult(int percentage, List<Solution> solutions) {
        this.percentage = percentage;
        this.solutions = solutions;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
}
