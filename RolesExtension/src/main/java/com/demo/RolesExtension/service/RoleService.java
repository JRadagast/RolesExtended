package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Role;
import com.demo.RolesExtension.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public ResponseEntity<Role> getById(Long idrole){
        try {
            Role r = repository.getById(idrole);

            ResponseEntity<Role> response = new ResponseEntity<Role>(r, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Role> response = new ResponseEntity<Role>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }   
    
    public ResponseEntity<Role> getRoleByMembership(String idUser, String idTeam){
        try {
            Role r = repository.getRoleByMembership(idUser, idTeam);

            ResponseEntity<Role> response = new ResponseEntity<Role>(r, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Role> response = new ResponseEntity<Role>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

    /**
     * Tries to add role, if role already exist, update it.
     * @param role
     * @return 
     */
    public ResponseEntity<Role> save(Role role){
        try {
            Role r = repository.getById( role.getIdrole() );
            if (r != null){
                r = repository.update(role);
            } else {
                r = repository.save(role);
            }

            ResponseEntity<Role> response = new ResponseEntity<Role>(r, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Role> response = new ResponseEntity<Role>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }
    
    public void insertStarterRoles(){
        repository.insertStarterRoles();        
    }

}
