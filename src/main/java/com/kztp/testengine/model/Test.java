package com.kztp.testengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String path;
    @Column(name = "pool_rating")
    private int poolRating;
    private float rating;
    private int price;
    @Column(name = "max_points")
    private int maxPoints;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;
    private boolean enabled;
    private String type;
    private boolean live;

    @JsonBackReference
    @OneToMany(mappedBy = "test")
    private List<PoolPoint> poolPoints = new ArrayList<>();

    @OneToMany(mappedBy = "test")
    @JsonBackReference
    private List<TestRating> testRatings = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "test")
    private List<UsersTest> usersTests = new ArrayList<>();

    @OneToOne(mappedBy = "test")
    @JsonBackReference
    private DeleteRequest deleteRequest = new DeleteRequest();

    public Test() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPoolRating() {
        return poolRating;
    }

    public void setPoolRating(int poolRating) {
        this.poolRating = poolRating;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public List<PoolPoint> getPoolPoints() {
        return poolPoints;
    }

    public void setPoolPoints(List<PoolPoint> poolPoints) {
        this.poolPoints = poolPoints;
    }

    public List<TestRating> getTestRatings() {
        return testRatings;
    }

    public void setTestRatings(List<TestRating> testRatings) {
        this.testRatings = testRatings;
    }

    public List<UsersTest> getUsersTests() {
        return usersTests;
    }

    public void setUsersTests(List<UsersTest> usersTests) {
        this.usersTests = usersTests;
    }
}
