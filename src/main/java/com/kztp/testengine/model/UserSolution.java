package com.kztp.testengine.model;

import java.util.List;

public class UserSolution {

    private int testId;
    private List<String> solutions;

    public UserSolution(int testId, List< String> solutions) {
        this.testId = testId;
        this.solutions = solutions;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public List<String> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<String> solutions) {
        this.solutions = solutions;
    }
}
