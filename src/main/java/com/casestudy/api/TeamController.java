package com.casestudy.api;

import com.casestudy.entities.Team;
import com.casestudy.exceptions.team.*;
import com.casestudy.exceptions.user.*;
import com.casestudy.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/team")
public class TeamController {

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Validated
    public Team createTeam(@RequestBody(required = false) Map<String, String> teamInfo) {

        String userId = teamInfo.get("userId");
        String teamName = teamInfo.get("teamName");

        return teamService.createTeam(Long.parseLong(userId), teamName);
    }

    @PutMapping("/{id}/join")
    @Validated
    public Team joinTeam(@PathVariable("id") Long teamId, @RequestBody Map<String, String> userInfo) {

        String userId = userInfo.get("userId");

        return teamService.joinTeam(teamId, Long.parseLong(userId));
    }

    @GetMapping("/available")
    public List<Team> getAvailableTeams() {

        return teamService.getAvailableTeams();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleUserNotFoundException() {
        return "User not found!";
    }

    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleTeamNotFoundException() {
        return "Team not found!";
    }

    @ExceptionHandler(InsufficientCoinException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private String handleInsufficientCoinException() {
        return "User has to have at least 1000 coins to create a team!";
    }

    @ExceptionHandler(UserIsMemberOfAnotherTeamException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private String handleUserIsAlreadyInATeamException() {
        return "User is already a member of another team, leave your team before join to a new team!";
    }

    @ExceptionHandler(UserIsAlreadyMemberOfTheTeamException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private String handleUserIsAlreadyMemberOfTheTeamException() {
        return "User is already a member of the requested team!";
    }

    @ExceptionHandler(TeamIsFullException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private String handleTeamIsFullException() {
        return "Team is full!";
    }

    @ExceptionHandler(TeamWithGivenNameAlreadyExists.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private String handleTeamWithGivenNameAlreadyExistsException() {
        return "Given team name is already taken!";
    }

}
