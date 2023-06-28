package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Team;
import com.demo.RolesExtension.repository.TeamRepository;
import java.sql.SQLException;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RunWith(JUnit4.class)
@ExtendWith(MockitoExtension.class)        
class TeamServiceTests {

    @InjectMocks
    TeamService service;

    @Mock
    TeamRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    public Team getTeam(String id){
        Team t = new Team();

        t.setIdteam(id);
        t.setName("TEAM");
        t.setTeamLeadId("IDTEAMLEAD");

        return t;
    }

    @Test
    public void testGetById() throws SQLException {
        Team team = getTeam("IDTEAM");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(team);

        ResponseEntity<Team> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());

        assertNotNull("testGetById", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(team, response.getBody());
    }  
        
    @Test
    public void testSave() throws SQLException {
        Team team = getTeam("IDTEAM");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(null).thenReturn(team);
        Mockito.when(repository.save(Mockito.any())).thenReturn(team);

        ResponseEntity<Team> response = service.save(team);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(team);
        Mockito.verify(repository, Mockito.times(1)).save(team);
        assertNotNull("testSave", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(team, response.getBody());
    }

    @Test
    public void testUpdate() throws SQLException {
        Team team = getTeam("IDTEAM");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(team);
        Mockito.when(repository.update(Mockito.any())).thenReturn(team);

        ResponseEntity<Team> response = service.save(team);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(team);
        Mockito.verify(repository, Mockito.times(0)).save(team);
        assertNotNull("testUpdate", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(team, response.getBody());
    }

    @Test
    public void testErrorGetById() throws SQLException {
        Mockito.when(repository.getById(Mockito.any())).thenThrow(SQLException.class);
        
        ResponseEntity<Team> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }  

    @Test
    public void testErrorUpdate() throws SQLException {
        Team team = getTeam("IDTEAM");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(team);
        Mockito.when(repository.update(Mockito.any())).thenThrow(SQLException.class);

        ResponseEntity<Team> response = service.save(team);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(team);
        Mockito.verify(repository, Mockito.times(0)).save(team);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
