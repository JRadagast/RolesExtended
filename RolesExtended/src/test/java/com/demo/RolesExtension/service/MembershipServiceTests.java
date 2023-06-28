package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Membership;
import com.demo.RolesExtension.repository.MembershipRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class MembershipServiceTests {

    @InjectMocks
    MembershipService service;

    @Mock
    MembershipRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    public Membership getMembership(Long id){
        Membership m = new Membership();

        m.setIdmembership(id);
        m.setIduser("IDUSER");
        m.setIdteam("IDTEAM");

        return m;
    }

    @Test
    public void testGetById() throws SQLException {
        Membership member = getMembership(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(member);

        ResponseEntity<Membership> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());

        assertNotNull("testGetById", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(member, response.getBody());
    }

    @Test
    public void testGetMembershipsByRole() throws SQLException {
        List<Membership> listaMembership = new ArrayList<>();
        listaMembership.add(getMembership(1L));
        listaMembership.add(getMembership(2L));

        Mockito.when(repository.getMembershipsByRole(Mockito.any())).thenReturn(listaMembership);

        ResponseEntity<List<Membership>> response = service.getMembershipsByRole(1L);

        Mockito.verify(repository, Mockito.times(1)).getMembershipsByRole(Mockito.any());
        assertNotNull("testGetMembershipsByRole", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listaMembership, response.getBody());
    }
        
    @Test
    public void testSave() throws SQLException {
        Membership membership = getMembership(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(null).thenReturn(membership);
        Mockito.when(repository.save(Mockito.any())).thenReturn(membership);

        ResponseEntity<Membership> response = service.save(membership);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(membership);
        Mockito.verify(repository, Mockito.times(1)).save(membership);
        assertNotNull("testSave", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(membership, response.getBody());
    }

    @Test
    public void testUpdate() throws SQLException {
        Membership membership = getMembership(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(membership);
        Mockito.when(repository.update(Mockito.any())).thenReturn(membership);

        ResponseEntity<Membership> response = service.save(membership);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(membership);
        Mockito.verify(repository, Mockito.times(0)).save(membership);
        assertNotNull("testUpdate", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(membership, response.getBody());
    }
    
    @Test
    public void testErrorGetMembershipsByRole() throws SQLException {
        Mockito.when(repository.getMembershipsByRole(Mockito.any())).thenThrow(SQLException.class);
        ResponseEntity<List<Membership>> response = service.getMembershipsByRole(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testErrorGetById() throws SQLException {
        Mockito.when(repository.getById(Mockito.any())).thenThrow(SQLException.class);
        
        ResponseEntity<Membership> response = service.getById(Mockito.any());

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }  

    @Test
    public void testErrorUpdate() throws SQLException {
        Membership membership = getMembership(1L);

        Mockito.when(repository.getById(Mockito.any())).thenReturn(membership);
        Mockito.when(repository.update(Mockito.any())).thenThrow(SQLException.class);

        ResponseEntity<Membership> response = service.save(membership);

        Mockito.verify(repository, Mockito.times(1)).getById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(membership);
        Mockito.verify(repository, Mockito.times(0)).save(membership);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
