package com.casestudy.services;

import com.casestudy.entities.Team;
import com.casestudy.entities.User;
import com.casestudy.exceptions.team.TeamIsFullException;
import com.casestudy.exceptions.team.TeamNotFoundException;
import com.casestudy.exceptions.user.*;
import com.casestudy.repos.TeamRepository;
import com.casestudy.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team saveOneTeam(Team newTeam) {
        return teamRepository.save(newTeam);
    }

    public Team getOneTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElse(null);
    }

    public Team createTeam(Long userId, String teamName) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // user has to have at least 1000 coins to create a team
        if (user.getCoin() < 1000) {
            throw new InsufficientCoinException();
        }

        Team team = new Team(teamName);
        team.setMemberCount(1);
        teamRepository.save(team);

        // if user is already a member of a team throw an exception
        if(user.getTeam() != null) {
            throw new UserIsMemberOfAnotherTeamException();
        }

        // set team info of user
        // deduct 1000 coins from user
        user.setTeam(team);
        user.setCoin(user.getCoin() - 1000);
        userRepository.save(user);

        return team;

    }

    public Team joinTeam(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId).orElse(null);

        if (team == null) {
            throw new TeamNotFoundException();
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // If user is already a member of another team, throw an exception
        if (user.getTeam() != null) {
            // If user wants to join his/her current team, throw an exception
            if(user.getTeam().getId().equals(team.getId())) {
                throw new UserIsAlreadyMemberOfTheTeamException();
            }

            // If user has a current team, throw an exception
            throw new UserIsMemberOfAnotherTeamException();
        }

        // If requested team is full, throw an exception
        if (team.getMemberCount() >= team.getCapacity()) {
            throw new TeamIsFullException();
        }

        user.setTeam(team);
        team.setMemberCount(team.getMemberCount() + 1);

        userRepository.save(user);
        teamRepository.save(team);

        return team;
    }

    public List<Team> getAvailableTeams () {
        // get all teams which has at least one empty place to join
        List<Team> allAvailableTeams = teamRepository.findAll()
                .stream()
                .filter(t -> t.getMemberCount() < t.getCapacity())
                .toList();

        if (allAvailableTeams.size() <= 10) {
            return allAvailableTeams;
        }

        ArrayList<Team> randomAvailableTeams = new ArrayList<>(allAvailableTeams);
        // shuffle array list to ger random available teams
        Collections.shuffle(randomAvailableTeams);
        return randomAvailableTeams.stream().toList().subList(0, 10);
    }
}
