package com.kztp.testengine.model;

import javax.persistence.*;

/*REATE TABLE Testreports (
        id serial NOT NULL,
        description TEXT NOT NULL,
        reporting_user integer NOT NULL,
        test_id integer NOT NULL,
        CONSTRAINT Testreports_pk PRIMARY KEY (id)
        ) WITH (
        OIDS=FALSE
        );*/
@Entity
@Table(name = "Testreports")
public class TestReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    @JoinColumn(name="reporting_user")
    private User reporter;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test reportedTest;

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
}
