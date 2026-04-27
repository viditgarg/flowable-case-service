package org.ny.its.flowable_case_service.controller;

import java.util.List;

import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.ProcessDTO;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/tenant")
@RequiredArgsConstructor
public class ProcessController {

    private final Logger log = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService processService;

    @PostMapping("/start")
    public String startProcess(@RequestHeader("App-Id") String appId,
            @RequestBody Person person) {

        log.info("App ID ::" + appId);
        return processService.startProcess(appId, person);
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks(@RequestHeader("App-Id") String appId) {
        return processService.getAllTasks(appId);
    }

    @GetMapping("/processes")
    public List<ProcessDTO> getProcesses(@RequestHeader("App-Id") String appId) {
        return processService.getRunningProcesses(appId);
    }
}
