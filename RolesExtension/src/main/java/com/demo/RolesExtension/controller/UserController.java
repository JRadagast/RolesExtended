package com.demo.RolesExtension.controller;

import com.demo.RolesExtension.beans.Role;
import com.demo.RolesExtension.beans.User;
import com.demo.RolesExtension.service.RoleService;
import com.demo.RolesExtension.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService service;

    @CrossOrigin
    @PostMapping("/rolesextension/user/updaterole")
    public ResponseEntity<User> save( @RequestBody User user ) {

        ResponseEntity<User> r = service.updateRole(user);

        return r;
    }

}
