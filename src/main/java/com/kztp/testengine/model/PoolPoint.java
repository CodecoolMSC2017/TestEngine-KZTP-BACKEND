package com.kztp.testengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Poolpoints")
public class PoolPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "voter_id", referencedColumnName = "id")
    private User voter;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
    private Integer vote;

    public PoolPoint() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }
}
