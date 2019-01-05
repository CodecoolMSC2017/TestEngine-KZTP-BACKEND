package com.kztp.testengine.model;

public class TestDetails {

    private int percentage;
    private int timesTaken;
    private int income;

    public TestDetails(int percentage, int timesTaken, int income) {
        this.percentage = percentage;
        this.timesTaken = timesTaken;
        this.income = income;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getTimesTaken() {
        return timesTaken;
    }

    public void setTimesTaken(int timesTaken) {
        this.timesTaken = timesTaken;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }
}
