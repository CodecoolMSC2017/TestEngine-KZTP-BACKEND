package com.kztp.testengine.model;

import java.util.Map;

public class UserSolution {

    private int testId;
    private Map<Integer,String> solutions;

    public UserSolution() {
    }

    public UserSolution(int testId, Map<Integer,String> solutions) {
        this.testId = testId;
        this.solutions = solutions;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Map<Integer,String> getSolutions() {
        return solutions;
    }

    public void setSolutions(Map<Integer,String> solutions) {
        this.solutions = solutions;
    }
}
