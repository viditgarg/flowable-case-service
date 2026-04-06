package org.ny.its.flowable_case_service.controller;

import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.service.CaseWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseWorkflowService service;

    @PostMapping("/start")
    public String startCase(@RequestBody Person person) {
        System.out.println("Received person data: " + person);
        return service.startCase(person);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working!";
    }
}