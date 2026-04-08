package org.ny.its.flowable_case_service.service;

import org.flowable.task.api.Task;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;

import org.flowable.engine.runtime.ProcessInstance;
import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.ProcessDTO;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaseWorkflowService {

    private final Logger log = LoggerFactory.getLogger(CaseWorkflowService.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    public String startCase(Person person) {

        Map<String, Object> vars = new HashMap<>();
        vars.put("firstName", person.getFirstName());
        vars.put("lastName", person.getLastName());
        vars.put("ssn", person.getSsn());
        vars.put("email", person.getEmail());
        vars.put("gender", person.getGender());
        vars.put("dateofbirth", person.getDateofbirth());

        return runtimeService
                .startProcessInstanceByKey("case_regVN10", vars)
                .getId();
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKeyLike("case_regVN10").list();
        return tasks.stream().map(
                t -> {
                    String firstName = Optional
                            .ofNullable(runtimeService.getVariable(t.getProcessInstanceId(), "firstName"))
                            .map(Object::toString)
                            .orElse(null);

                    String lastName = Optional
                            .ofNullable(runtimeService.getVariable(t.getProcessInstanceId(), "lastName"))
                            .map(Object::toString)
                            .orElse(null);

                    String applicantName = firstName + " " + lastName;

                    return new TaskDTO(
                            t.getId(),
                            t.getName(),
                            t.getAssignee(),
                            t.getProcessInstanceId(),
                            t.getProcessDefinitionId(),
                            t.getTaskDefinitionKey(),
                            applicantName);
                }).toList();
        // return taskService.createTaskQuery().list();
    }

    public List<TaskDTO> getTasksByUser(String user) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(user)
                .list();

        return tasks.stream().map(
                t -> new TaskDTO(
                        t.getId(),
                        t.getName(),
                        t.getAssignee(),
                        t.getProcessInstanceId(),
                        t.getProcessDefinitionId(),
                        t.getTaskDefinitionKey(),
                        "Unknown"))
                .toList();
    }

    public void claimTask(String taskId, String user) {
        taskService.claim(taskId, user);
    }

    public String completeTask(String taskId, String processInstanceId, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionKey = processInstance.getProcessDefinitionKey();

        String nextView = "casedashboard";
        if (processDefinitionKey != null && processDefinitionKey.equals("case_regV5")) {
            switch (task.getTaskDefinitionKey()) {
                case "caseDetailsTask":
                    log.info("Task Key :" + task.getTaskDefinitionKey());
                    completeCaseDetailsTask(task);
                    break;
                case "doccsManualTask":
                    log.info("Task Key :" + task.getTaskDefinitionKey());
                    completeCaseCreationProcessTask(taskId);
                    break;
                case "finalReviewTask":
                    log.info("Task Key :" + task.getTaskDefinitionKey());
                    completeCaseCreationProcessTask(taskId);
                    break;
                case "ssnManualTask":
                    log.info("Task Key :" + task.getTaskDefinitionKey());
                    completeCaseCreationProcessTask(taskId);
                    break;
                default:
                    log.info(task.getTaskDefinitionKey());
                    completeCaseCreationProcessTask(taskId);

            }

            log.info("Process def key is case_regV5 ");

        } else {
            log.info("Process def key is other ");
            taskService.complete(taskId);
        }
        return nextView;
    }

    private void completeCaseDetailsTask(Task task) {

        taskService.complete(task.getId());
    }

    private void completeCaseCreationProcessTask(String taskId) {
        log.info("Task id is " + taskId);

        taskService.complete(taskId);
    }

    public List<ProcessInstance> getRunningProcesses() {
        List<ProcessInstance> piList = runtimeService.createProcessInstanceQuery().list();
        piList.forEach(pi -> {
            log.info(pi.getName());
        });

        return piList;
    }

    public List<ProcessDTO> getRunningProcessesN() {
        List<ProcessInstance> piList = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("case_regVN10")
                .list();
        return piList.stream().map(
                p -> {
                    Map<String, Object> vars = runtimeService.getVariables(p.getProcessInstanceId());

                    return new ProcessDTO(
                            p.getProcessDefinitionKey(),
                            p.getProcessInstanceId(),
                            p.getProcessDefinitionName(),
                            p.getProcessDefinitionId(),
                            vars.get("firstName").toString() + " " + vars.get("lastName").toString(),
                            p.getStartTime());
                }).toList();
    }

    public String getTaskDefinitionKey(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        log.info("Task Definition Key::" + task.getTaskDefinitionKey());
        return task.getTaskDefinitionKey();
    }

    public Map<String, Object> getCaseDetails(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        Map<String, Object> response = new HashMap<>();

        response.put("person", getPersonData(task.getProcessInstanceId()));
        response.put("taskId", task.getId());
        response.put("SSN_Valid",
                runtimeService.getVariable(task.getProcessInstanceId(), "SSN_Valid"));
        response.put("incarcerationStatus",
                runtimeService.getVariable(task.getProcessInstanceId(), "incarcerationStatus"));

        return response;
    }

    public Map<String, Object> getAddressDetails(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        Map<String, Object> response = new HashMap<>();

        response.put("street", runtimeService.getVariable(task.getProcessInstanceId(), "street"));
        response.put("postalCode", runtimeService.getVariable(task.getProcessInstanceId(), "postalCode"));
        response.put("city", runtimeService.getVariable(task.getProcessInstanceId(), "city"));
        response.put("state", runtimeService.getVariable(task.getProcessInstanceId(), "state"));
        response.put("postalCodeInWords",
                runtimeService.getVariable(task.getProcessInstanceId(), "postalCodeInWords"));

        return response;
    }

    public Person getPersonData(String processInstanceId) {
        Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
        Person person = new Person();
        person.setFirstName(Optional.ofNullable(vars.get("firstName"))
                .map(Object::toString)
                .orElse(null));
        person.setLastName(Optional.ofNullable(vars.get("lastName"))
                .map(Object::toString)
                .orElse(null));
        person.setEmail(Optional.ofNullable(vars.get("email"))
                .map(Object::toString)
                .orElse(null));
        ;
        person.setGender(Optional.ofNullable(vars.get("gender"))
                .map(Object::toString)
                .orElse(null));
        // SSN (null-safe)
        person.setSsn(Optional.ofNullable(vars.get("ssn"))
                .map(Object::toString)
                .orElse(null));

        // Date of Birth (null-safe)
        person.setDateofbirth(Optional.ofNullable(vars.get("dateofbirth"))
                .map(Object::toString)
                .filter(s -> !s.isEmpty())
                .map(LocalDate::parse)
                .orElse(null));
        person.setIncarcerationStatus(Optional.ofNullable(vars.get("incarcerationStatus"))
                .map(Object::toString)
                .orElse(null));

        return person;
    }

    public String cleanAllData() {
        runtimeService.createProcessInstanceQuery()
                .list()
                .forEach(pi -> runtimeService.deleteProcessInstance(
                        pi.getId(), "cleanup"));

        historyService.createHistoricProcessInstanceQuery()
                .list()
                .forEach(hpi -> historyService.deleteHistoricProcessInstance(hpi.getId()));
        return "cleaned";
    }

    public void completeAddressTask(String taskId, Map<String, Object> request) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        String processInstanceId = task.getProcessInstanceId();

        // Extract values
        String street = (String) request.get("street");
        String city = (String) request.get("city");
        String state = (String) request.get("state");
        String postalCode = (String) request.get("postalCode");
        String updatePostalCode = (String) request.get("updatePostalCode");

        // Build variable map
        Map<String, Object> variables = new HashMap<>();
        variables.put("street", street);
        variables.put("city", city);
        variables.put("state", state);
        variables.put("postalCode", postalCode);

        if (updatePostalCode != null) {
            variables.put("updatePostalCode", updatePostalCode);
        }

        // completing task
        taskService.complete(taskId, variables);
    }

}
