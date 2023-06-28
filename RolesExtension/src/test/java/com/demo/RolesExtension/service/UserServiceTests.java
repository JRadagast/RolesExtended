package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.User;
import com.demo.RolesExtension.repository.UserRepository;
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
class UserServiceTests {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    public User getUser(String id){
        User u = new User();

        u.setIduser(id);
        u.setFirstName("USER");
        u.setDisplayName("USER");
        u.setAvatarUrl("URL");
        u.setLocation("LOCATION");
        u.setIdrole(1L);

        return u;
    }

    @Test
    public void testGetById() throws SQLException {
        User user = getUser("IDUSER");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(user);

        ResponseEntity<User> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());

        assertNotNull("testGetById", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }  
        
    @Test
    public void testSave() throws SQLException {
        User user = getUser("IDUSER");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(null).thenReturn(user);
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        ResponseEntity<User> response = service.save(user);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(user);
        Mockito.verify(repository, Mockito.times(1)).save(user);
        assertNotNull("testSave", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdate() throws SQLException {
        User user = getUser("IDUSER");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(user);
        Mockito.when(repository.update(Mockito.any())).thenReturn(user);

        ResponseEntity<User> response = service.save(user);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(user);
        Mockito.verify(repository, Mockito.times(0)).save(user);
        assertNotNull("testUpdate", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testErrorGetById() throws SQLException {
        Mockito.when(repository.getById(Mockito.any())).thenThrow(SQLException.class);
        
        ResponseEntity<User> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }  

    @Test
    public void testErrorUpdate() throws SQLException {
        User user = getUser("IDUSER");

        Mockito.when(repository.getById(Mockito.any())).thenReturn(user);
        Mockito.when(repository.update(Mockito.any())).thenThrow(SQLException.class);

        ResponseEntity<User> response = service.save(user);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(user);
        Mockito.verify(repository, Mockito.times(0)).save(user);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
