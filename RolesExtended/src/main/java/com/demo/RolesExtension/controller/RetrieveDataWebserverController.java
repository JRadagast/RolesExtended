package com.demo.RolesExtension.controller;

import com.demo.RolesExtension.service.RetrieveDataWebserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RetrieveDataWebserverController {

    @Autowired
    private RetrieveDataWebserverService service;
     
    @CrossOrigin
    @GetMapping("/rolesextension/retrievedata")
    public ResponseEntity<Boolean> retrieveDataWebservice() {

        ResponseEntity<Boolean> r = service.retrieveDataFromWebserver();

        return r;
    }

}
