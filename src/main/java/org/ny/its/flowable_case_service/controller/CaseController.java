package org.ny.its.flowable_case_service.controller;

import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.ProcessDTO;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.CaseWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        return service.getTasksByUser(assignee);
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        return service.getAllTasks();
    }

    @PostMapping("/tasks/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        service.completeTask(taskId, variables);
        return "Task completed";
    }

    @GetMapping("/process/{processInstanceId}/variables")
    public Map<String, Object> getProcessVariables(@PathVariable String processInstanceId) {
        return service.getProcessVariables(processInstanceId);
    }

    @GetMapping("/processes")
    public List<ProcessDTO> getProcesses() {
        return service.getRunningProcessesN();
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working!";
    }

    @GetMapping("/tasks/{taskId}/taskDefinitionKey")
    public String getTaskDefinitonKey(@PathVariable String taskId) {
        return service.getTaskDefinitionKey(taskId);
    }

    @GetMapping("/tasks/{taskId}/case-details")
    public Map<String, Object> getCaseDetails(@PathVariable String taskId) {

        return service.getCaseDetails(taskId);
    }

    @GetMapping("/tasks/{taskId}/tenantId")
    public String getCaseTenantId(@PathVariable String taskId) {
        return service.getCaseTenantId(taskId);
    }

    @GetMapping("/tasks/{taskId}/address-details")
    public Map<String, Object> getAddressDetails(@PathVariable String taskId) {

        return service.getAddressDetails(taskId);
    }

    /*--
    This rest endpoint is called from process flow to validate ssn
     */
    @PostMapping("/ssnValidation")
    @ResponseBody
    public boolean validateSSN(@RequestBody Map<String, Object> map) {

        // log.info("validateSSN started");
        String ssn = (String) map.get("ssn");
        double randomValue = Math.random();
        boolean valid = false;
        // valid = randomValue < 0.5;
        System.out.println("SSN:" + ssn + " successfully validated, returning SSN_VALID as " + valid);
        return valid; // randomValue < 0.5;;

    }

    // to clean up existing data
    @GetMapping("/cleanup")
    @ResponseBody
    public String cleanAllData() {
        return service.cleanAllData();
    }

    @PostMapping("/searchPerson")
    @ResponseBody
    public boolean searchPerson(@RequestBody Map<String, Object> map) {
        // log.info("searchPerson started for " + map.get("firstName") + " " +
        // map.get("lastName"));
        return false;
    }

    @PostMapping("/tasks/{taskId}/complete-address")
    public String completeAddressTask(@PathVariable String taskId,
            @RequestBody Map<String, Object> request) {

        service.completeAddressTask(taskId, request);

        return "Address task completed";
    }

}