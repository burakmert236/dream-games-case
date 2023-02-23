package com.casestudy.repos;

import com.casestudy.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface TeamRepository extends JpaRepository<Team, Serializable> {
}
