package org.ny.its.flowable_case_service.controller;

import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.CaseWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/tasks/{assignee}")
    public List<TaskDTO> getTasks(@PathVariable String assignee) {
        return service.getTasksForAssignee(assignee);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestBody Map<String,Object> variables) {
        service.completeTask(taskId, variables);
        return "Task completed";
    }

    @GetMapping("/process/{processInstanceId}/variables")
    public Map<String,Object> getProcessVariables(@PathVariable String processInstanceId){
        return service.getProcessVariables(processInstanceId);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working!";
    }
}