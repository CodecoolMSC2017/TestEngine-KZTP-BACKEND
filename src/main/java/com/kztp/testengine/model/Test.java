package com.kztp.testengine.model;

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
    private String path;
    private int price;
    @Column(name = "max_points")
    private int maxPoints;
    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;
    private boolean enabled;
    private String type;

    @OneToMany(mappedBy = "test")
    private List<PoolPoints> poolPoints = new ArrayList<>();

    public Test() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public List<PoolPoints> getPoolPoints() {
        return poolPoints;
    }

    public void setPoolPoints(List<PoolPoints> poolPoints) {
        this.poolPoints = poolPoints;
    }
}
