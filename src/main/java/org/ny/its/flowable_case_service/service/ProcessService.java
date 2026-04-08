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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessService {

        @Autowired
        private RuntimeService runtimeService;
        @Autowired
        private TaskService taskService;

        public String startProcess(String appId, Map<String, Object> request) {
                // You can pass all UI data as variables
                Map<String, Object> variables = new HashMap<>(request);

                ProcessInstance processInstance = runtimeService
                                .createProcessInstanceBuilder()
                                .processDefinitionKey("case_regVN11_T") // your BPMN id
                                .tenantId(appId)
                                .variables(variables)
                                .start();

                return processInstance.getId();
        }

        public List<TaskDTO> getAllTasks(String appId) {
                List<Task> tasks = taskService.createTaskQuery()
                                .processDefinitionKeyLike("case_regVN11_T")
                                .taskTenantId(appId)
                                .list();

                return tasks.stream().map(
                                t -> {
                                        String firstName = Optional
                                                        .ofNullable(runtimeService.getVariable(t.getProcessInstanceId(),
                                                                        "firstName"))
                                                        .map(Object::toString)
                                                        .orElse(null);

                                        String lastName = Optional
                                                        .ofNullable(runtimeService.getVariable(t.getProcessInstanceId(),
                                                                        "lastName"))
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
