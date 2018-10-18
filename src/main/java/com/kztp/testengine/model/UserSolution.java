package com.kztp.testengine.model;

import java.util.List;

public class UserSolution {

    private int testId;
    private List<Solution> solutions;

    public UserSolution() {
    }

    public UserSolution(int testId, List<Solution> solutions) {
        this.testId = testId;
        this.solutions = solutions;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
}
