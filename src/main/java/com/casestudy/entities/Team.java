package com.casestudy.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "Team", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    private int capacity;
    private int member_count;

    public Team(@JsonProperty String name) {
        this.name = name;
        this.capacity = 20;
        this.member_count = 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMemberCount() {
        return member_count;
    }

    public void setMemberCount(int member_count) {
        this.member_count = member_count;
    }

}
