package com.casestudy.api;

import com.casestudy.entities.Team;
import com.casestudy.entities.User;
import com.casestudy.services.TeamService;
import com.casestudy.services.UserService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TeamControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private WebApplicationContext context;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void createTeam() throws Exception {
        User user = new User();
        User savedUser = userService.saveOneUser(user);

        String randomTeamName = UUID.randomUUID().toString();

        String JSONBody = String.format("{ 'userId': '%s', 'teamName': '%s'}", savedUser.getId().toString(), randomTeamName);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(JSONBody);

        RequestBuilder request = post("/api/v1/team")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json.toString());

        // checks values of returned team
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(randomTeamName)))
                .andExpect(jsonPath("$.memberCount", is(1)))
                .andExpect(jsonPath("$.capacity", is(20)))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void joinTeam() throws Exception {
        User userToCreateTeam = new User();
        User userToJoinTeam = new User();

        User savedUserToCreateTeam = userService.saveOneUser(userToCreateTeam);
        User savedUserToJoinTeam = userService.saveOneUser(userToJoinTeam);

        String randomTeamName = UUID.randomUUID().toString();

        // creates a team by another user to test joining a team
        Team teamToJoin = teamService.createTeam(savedUserToCreateTeam.getId(), randomTeamName);

        String JSONBody = String.format("{ 'userId': '%s' }", savedUserToJoinTeam.getId().toString());

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(JSONBody);

        RequestBuilder request = put("/api/v1/team/" + teamToJoin.getId().toString() + "/join")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json.toString());

        // checks level and coin values of returned user
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teamToJoin.getId().intValue())))
                .andExpect(jsonPath("$.name", is(randomTeamName)))
                .andExpect(jsonPath("$.capacity", is(teamToJoin.getCapacity())))
                .andExpect(jsonPath("$.memberCount", is(teamToJoin.getMemberCount() + 1)))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getAvailableTeams() throws Exception {

        List<User> users = new ArrayList<>();
        // creates 15 user to create teams
        for(int i=0; i<15; i++) {
            User user = new User();
            userService.saveOneUser(user);
            users.add(user);
        }

        List<Team> teams = new ArrayList<>();
        // creates 15 teams
        for(int i=0; i<15; i++) {
            String randomTeamName = UUID.randomUUID().toString();
            Team team = teamService.createTeam(users.get(i).getId(), randomTeamName);
            teams.add(team);
        }

        // fill 5 teams
        teams.get(3).setMemberCount(20);
        teams.get(5).setMemberCount(20);
        teams.get(11).setMemberCount(20);
        teams.get(7).setMemberCount(20);
        teams.get(14).setMemberCount(20);

        // save changed teams
        teamService.saveOneTeam(teams.get(3));
        teamService.saveOneTeam(teams.get(5));
        teamService.saveOneTeam(teams.get(11));
        teamService.saveOneTeam(teams.get(7));
        teamService.saveOneTeam(teams.get(14));

        int[] idList = new int[5];
        idList[0] = teams.get(3).getId().intValue();
        idList[1] = teams.get(5).getId().intValue();
        idList[2] = teams.get(11).getId().intValue();
        idList[3] = teams.get(7).getId().intValue();
        idList[4] = teams.get(14).getId().intValue();

        RequestBuilder request = get("/api/v1/team/available");

        // check if there is a full team among the returned teams
        MvcResult response = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(response.getResponse().getContentAsString());

        for(int i=0; i<json.size(); i++) {
            JSONObject jsonObject = (JSONObject) json.get(i);
            System.out.println((int)jsonObject.get("id"));
            assert((int)jsonObject.get("id") != teams.get(3).getId().intValue());
            assert((int)jsonObject.get("id") != teams.get(5).getId().intValue());
            assert((int)jsonObject.get("id") != teams.get(11).getId().intValue());
            assert((int)jsonObject.get("id") != teams.get(7).getId().intValue());
            assert((int)jsonObject.get("id") != teams.get(14).getId().intValue());
        }

    }
}