package org.ny.its.flowable_case_service.service;


import org.flowable.task.api.Task;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;

import org.flowable.engine.runtime.ProcessInstance;
import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CaseWorkflowService {

    private final Logger log = LoggerFactory.getLogger(CaseWorkflowService.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

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



    public void completeTask(String taskId, Map<String,Object> variables) {
        taskService.complete(taskId, variables);
    }

    public Map<String,Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        return tasks.stream().map(
                t -> new TaskDTO(
                        t.getId(),
                        t.getName(),
                        t.getAssignee(),
                        t.getProcessInstanceId(),
                        t.getProcessDefinitionId(),
                        t.getTaskDefinitionKey(),
                        "Unknown"
                )).toList();
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
                        "Unknown"
                )).toList();
    }

    public List<TaskDTO> getTasksForAssignee(String assignee) {
        return getTasksByUser(assignee);
    }

    public void claimTask(String taskId, String user) {
        taskService.claim(taskId, user);
    }

    public String completeTask(String taskId, String processInstanceId, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
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




}
