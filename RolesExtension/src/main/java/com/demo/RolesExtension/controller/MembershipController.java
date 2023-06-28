package com.demo.RolesExtension.controller;

import com.demo.RolesExtension.beans.Membership;
import com.demo.RolesExtension.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MembershipController {

    @Autowired
    private MembershipService service;
     
    @CrossOrigin
    @GetMapping("/rolesextension/membership/getmembershipsbyrole")
    public ResponseEntity<List<Membership>> getMembershipByRole( @RequestParam(name="idrole") Long idRole ) {

        ResponseEntity<List<Membership>> r = service.getMembershipsByRole(idRole);

        return r;
    }

}
