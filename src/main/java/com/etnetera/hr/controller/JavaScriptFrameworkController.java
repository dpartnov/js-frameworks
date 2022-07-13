package com.etnetera.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.etnetera.hr.service.FrameworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 *
 */
@RestController()
@RequestMapping("/frameworks")
public class JavaScriptFrameworkController {

    @Autowired
    private FrameworkService service;

    @GetMapping()
    public ResponseEntity frameworks() {
        return ResponseEntity.ok(service.getAll());
    }

}
