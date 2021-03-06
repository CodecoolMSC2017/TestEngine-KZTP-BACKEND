package com.kztp.testengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @JsonBackReference
    private String password;
    private String email;
    private String rank;
    private boolean enabled;
    @OneToMany(mappedBy = "creator")
    @JsonBackReference
    private List<Test> tests = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username")
    )
    @Column(name = "authority")
    private List<String> authorities;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<UsersTest> usersTest = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<News> news = new ArrayList<>();

    @OneToMany(mappedBy = "reporter")
    @JsonBackReference
    private List<TestReport> reports = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private Usertoken userToken = new Usertoken();

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private PasswordToken passwordToken = new PasswordToken();



    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<UsersTest> getUsersTest() {
        return usersTest;
    }

    public void setUsersTest(List<UsersTest> usersTest) {
        this.usersTest = usersTest;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<TestReport> getReports() {
        return reports;
    }

    public void setReports(List<TestReport> reports) {
        this.reports = reports;
    }

    public Usertoken getUserToken() {
        return userToken;
    }

    public void setUserToken(Usertoken userToken) {
        this.userToken = userToken;
    }

    public PasswordToken getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(PasswordToken passwordToken) {
        this.passwordToken = passwordToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
