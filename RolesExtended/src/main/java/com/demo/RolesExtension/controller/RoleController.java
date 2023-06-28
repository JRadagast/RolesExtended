package com.demo.RolesExtension.controller;

import com.demo.RolesExtension.beans.Role;
import com.demo.RolesExtension.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RoleController {

    @Autowired
    private RoleService service;

    @CrossOrigin
    @GetMapping("/rolesextension/role/getrolebymembership")
    public ResponseEntity<Role> getRoleByMembership( @RequestParam(name="iduser") String idUser, @RequestParam(name="idteam") String idTeam ) {

        ResponseEntity<Role> r = service.getRoleByMembership(idUser, idTeam);

        return r;
    }  

    @CrossOrigin
    @GetMapping("/rolesextension/role/getbyid")
    public ResponseEntity<Role> getById( @RequestParam(name="idrole") Long idRole ) {

        ResponseEntity<Role> r = service.getById(idRole);

        return r;
    }

    @CrossOrigin
    @PostMapping("/rolesextension/role/saverole")
    public ResponseEntity<Role> save( @RequestBody Role role ) {

        ResponseEntity<Role> r = service.save(role);

        return r;
    }

}
