/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Team;
import com.demo.RolesExtension.beans.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author JRadagast
 */
@Service
public class RetrieveDataWebserverService {
    
    String userApiUrl = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
    String usersApiUrl = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users";
    String teamApiUrl = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/7676a4bf-adfe-415c-941b-1739af07039b";
    String teamsApiUrl = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams";
    
    RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    UserService userService;
    
    @Autowired
    TeamService teamService;
    
    @Autowired
    MembershipService membershipService;
    
    @Autowired
    RoleService roleService;
    
    public ResponseEntity<Boolean> retrieveDataFromWebserver(){
        
        /* Insert the default roles into the database. */
        insertStarterRoles();
        
        /* Call each url and adds its responses into the database */
        try{
            retrieveSingleUser();
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
            
            /* If any of the url calls end up with an error, return false. */
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            retrieveAllUsers();
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
            
            /* If any of the url calls end up with an error, return false. */
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        try{
            retrieveSingleTeam();
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
            
            /* If any of the url calls end up with an error, return false. */
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        try{
            retrieveAllTeams();
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
            
            /* If any of the url calls end up with an error, return false. */
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
            
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
    
    private void retrieveSingleUser() throws JsonProcessingException{
        /* Get response from URL */
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(userApiUrl, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        
        if (responseEntity.getBody() != null){
            /* Uses jackson to transform result into class user. */
            User user = mapper.readValue( responseEntity.getBody(), User.class );

            /* Add new user */
            if (user != null){
                userService.save(user);
            }
        }
    }
    
    private void retrieveAllUsers() throws JsonProcessingException{
        /* Get response from URL */
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(usersApiUrl, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        
        if (responseEntity.getBody() != null){
            /* Uses jackson to transform result into class user. */
            List<User> list = mapper.readValue( responseEntity.getBody(), new TypeReference<List<User>>(){} );

            /* Iterates through the entire list saving each user. */
            if (list != null && list.size() > 0){

                for( User user : list ){
                    userService.save(user);
                }
            }
        }
    }
    
    private void retrieveSingleTeam() throws JsonProcessingException{
        /* Get response from URL */
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(teamApiUrl, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        
        if (responseEntity.getBody() != null){
            /* Uses jackson to transform result into class user. */
            Team team = mapper.readValue( responseEntity.getBody(), Team.class );

            if (team != null){
                teamService.save(team);

                /* Also saves memberships for the team */
                membershipService.saveAll(team.getTeamMemberIds(), team.getIdteam());
            }
        }
    }
    
    private void retrieveAllTeams() throws JsonProcessingException{
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(teamsApiUrl, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        
        if (responseEntity.getBody() != null){
            /* Uses jackson to transform result into class user. */
            List<Team> list = mapper.readValue( responseEntity.getBody(), new TypeReference<List<Team>>(){} );
            
            if (list != null && list.size() > 0){
                /* Iterates through the entire list saving each team. */
                for( Team team : list ){
                    teamService.save(team);
                    
                    /* Also saves memberships for the teams */
                    membershipService.saveAll(team.getTeamMemberIds(), team.getIdteam());
                }
            }
        }
    }
    
    private void insertStarterRoles(){
        roleService.insertStarterRoles();
    }
    
}
