package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Role;
import com.demo.RolesExtension.repository.RoleRepository;
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
class RoleServiceTests {

    @InjectMocks
    RoleService service;

    @Mock
    RoleRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    public Role getRole(Long id){
        Role r = new Role();

        r.setIdrole(id);
        r.setIsdefault(false);
        r.setName("ROLE TEST");

        return r;
    }

    @Test
    public void testGetById() throws SQLException {
        Role role = getRole(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(role);

        ResponseEntity<Role> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());

        assertNotNull("testGetById", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    public void testGetRoleByMembership() throws SQLException {
        Role role = getRole(1L);

        Mockito.when(repository.getRoleByMembership(Mockito.any(), Mockito.any())).thenReturn(role);

        ResponseEntity<Role> response = service.getRoleByMembership( "USER", "TEAM" );

        Mockito.verify(repository, Mockito.times(1)).getRoleByMembership(Mockito.any(), Mockito.any());
        assertNotNull("testGetRoleByMembership", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }   
        
    @Test
    public void testSave() throws SQLException {
        Role role = getRole(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(null).thenReturn(role);
        Mockito.when(repository.save(Mockito.any())).thenReturn(role);

        ResponseEntity<Role> response = service.save(role);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(role);
        Mockito.verify(repository, Mockito.times(1)).save(role);
        assertNotNull("testSave", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    public void testUpdate() throws SQLException {
        Role role = getRole(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(role);
        Mockito.when(repository.update(Mockito.any())).thenReturn(role);

        ResponseEntity<Role> response = service.save(role);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(role);
        Mockito.verify(repository, Mockito.times(0)).save(role);
        assertNotNull("testUpdate", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }
    
    @Test
    public void testErrorGetRoleByMembership() throws SQLException {
        Mockito.when(repository.getRoleByMembership(Mockito.any(), Mockito.any())).thenThrow(SQLException.class);
        ResponseEntity<Role> response = service.getRoleByMembership("USER", "TEAM");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testErrorGetById() throws SQLException {
        Mockito.when(repository.getById(Mockito.any())).thenThrow(SQLException.class);
        
        ResponseEntity<Role> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }  

    @Test
    public void testErrorUpdate() throws SQLException {
        Role role = getRole(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(role);
        Mockito.when(repository.update(Mockito.any())).thenThrow(SQLException.class);

        ResponseEntity<Role> response = service.save(role);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(role);
        Mockito.verify(repository, Mockito.times(0)).save(role);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
