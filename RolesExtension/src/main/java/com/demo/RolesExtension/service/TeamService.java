package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Team;
import com.demo.RolesExtension.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TeamService {

    @Autowired
    private TeamRepository repository;

    public ResponseEntity<Team>  getById(String idTeam){
        try {
            Team t = repository.getById(idTeam);

            ResponseEntity<Team> response = new ResponseEntity<Team>(t, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Team> response = new ResponseEntity<Team>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

    /**
     * Tries to add team, if team already exist, update it.
     * @param team
     * @return 
     */
    public ResponseEntity<Team> save(Team team){
        try {
            Team t = repository.getById(team.getIdteam());
            if (t != null){
                t= repository.update(team);
            } else {
                t = repository.save(team);
            }

            ResponseEntity<Team> response = new ResponseEntity<Team>(t, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Team> response = new ResponseEntity<Team>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

}
