package com.etnetera.hr.controller;

import com.etnetera.hr.data.dto.FrameworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.etnetera.hr.service.FrameworkService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/find")
    public ResponseEntity frameworksByFilter(@RequestParam(required = true, name = "name") String name) {
        return ResponseEntity.ok(service.getAllByFilter(name));
    }

    @GetMapping("{id}")
    public ResponseEntity frameworkDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @PostMapping()
    public ResponseEntity createFramework(@Valid @RequestBody final FrameworkDto payload, final BindingResult result) {
        service.createFramework(payload);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public ResponseEntity updateFramework(@Valid @RequestBody final FrameworkDto payload, @PathVariable(required = true) Long id, final BindingResult result) {
        service.updateFramework(payload, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFramework(@PathVariable(required = true) Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
