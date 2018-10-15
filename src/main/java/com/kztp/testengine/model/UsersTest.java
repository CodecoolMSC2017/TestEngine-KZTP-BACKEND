package com.kztp.testengine.model;

import javax.persistence.*;

@Entity
@Table(name = "Userstests")
public class UsersTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
    @Column(name = "max_points")
    private Integer maxPoints;
    @Column(name = "actual_points")
    private Integer actualPoints;
    private Integer percentage;

    public UsersTest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Integer getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(Integer actualPoints) {
        this.actualPoints = actualPoints;
    }

    public Integer getPercetage() {
        return percentage;
    }

    public void setPercetage(Integer percentage) {
        this.percentage = percentage;
    }
}
