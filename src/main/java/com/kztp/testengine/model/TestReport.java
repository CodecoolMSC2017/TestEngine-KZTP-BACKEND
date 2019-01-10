package com.kztp.testengine.model;

import javax.persistence.*;

@Entity
@Table(name = "Testreports")
public class TestReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @Column(name="userdescription")
    private String userDescription;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User reporter;
    @ManyToOne
    @JoinColumn(name = "test_id",referencedColumnName = "id")
    private Test reportedTest;
    private boolean solved;

    public TestReport() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Test getReportedTest() {
        return reportedTest;
    }

    public void setReportedTest(Test reportedTest) {
        this.reportedTest = reportedTest;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
