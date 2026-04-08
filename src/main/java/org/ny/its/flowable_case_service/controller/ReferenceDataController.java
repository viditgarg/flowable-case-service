package org.ny.its.flowable_case_service.controller;

import java.util.List;

import org.ny.its.flowable_case_service.service.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class ReferenceDataController {

    @Autowired
    private ReferenceDataService service;

    @GetMapping("/genders")
    public List<String> getGenders() {
        return service.getGenderOptions();
    }

    @GetMapping("/incarceration-status")
    public List<String> getIncarcerationOptions() {
        return service.getIncarcerationOptions();
    }

    @GetMapping("/ssn-validation")
    public List<String> getSsnValidationOptions() {
        return service.getSsnValidationOptions();
    }
}
