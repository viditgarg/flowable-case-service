package org.ny.its.flowable_case_service.controller;

import java.util.List;
import java.util.Map;

import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.PersonSearchService;
import org.ny.its.flowable_case_service.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final Logger log = LoggerFactory.getLogger(PersonSearchService.class);

    private final ProcessService processService;

    @PostMapping("/start")
    public String startProcess(@RequestHeader("X-App-Id") String appId,
            @RequestBody Map<String, Object> request) {

        return processService.startProcess(appId, request);
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks(@RequestHeader("X-App-Id") String appId) {
        return processService.getAllTasks(appId);
    }

}
