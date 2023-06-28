package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.User;
import com.demo.RolesExtension.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<User>  getById(String idUser){
        try {
            User u = repository.getById(idUser);

            ResponseEntity<User> response = new ResponseEntity<User>(u, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<User> response = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

    /**
     * Tries to add user, if user already exist, update it.
     * @param user
     * @return 
     */
    public ResponseEntity<User> save(User user){
        try {
            User u = repository.getById(user.getIduser());
            if (u != null){
                u = repository.update(user);
            } else {
                u = repository.save(user);
            }

            ResponseEntity<User> response = new ResponseEntity<User>(u, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<User> response = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }
}
