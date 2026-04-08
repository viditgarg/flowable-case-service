package org.ny.its.flowable_case_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.task.api.Task;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.ny.its.flowable_case_service.model.TaskDTO;

public class ProcessService {

    private final RuntimeService runtimeService = null;
    private final TaskService taskService = null;

    public String startProcess(String appId, Map<String, Object> request) {
        // You can pass all UI data as variables
        Map<String, Object> variables = new HashMap<>(request);

        ProcessInstance processInstance = runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionKey("case_regVN10") // your BPMN id
                .tenantId(appId)
                .variables(variables)
                .start();

        return processInstance.getId();
    }

    public List<TaskDTO> getAllTasks(String appId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKeyLike("case_regVN10")
                .taskTenantId(appId)
                .list();

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
    }

}
