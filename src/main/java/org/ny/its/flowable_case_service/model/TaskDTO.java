package org.ny.its.flowable_case_service.model;

import lombok.NoArgsConstructor;
import java.util.Objects;

@NoArgsConstructor
public class TaskDTO {
    private String id;
    private String name;
    private String assignee;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String applicantName;

    public TaskDTO(String id, String name, String assignee, String processInstanceId,
                   String processDefinitionId, String taskDefinitionKey, String applicantName) {
        this.id = id;
        this.name = name;
        this.assignee = assignee;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.applicantName = applicantName;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public String getApplicantName() {
        return applicantName;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return Objects.equals(id, taskDTO.id) &&
                Objects.equals(name, taskDTO.name) &&
                Objects.equals(assignee, taskDTO.assignee) &&
                Objects.equals(processInstanceId, taskDTO.processInstanceId) &&
                Objects.equals(processDefinitionId, taskDTO.processDefinitionId) &&
                Objects.equals(taskDefinitionKey, taskDTO.taskDefinitionKey) &&
                Objects.equals(applicantName, taskDTO.applicantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignee, processInstanceId, processDefinitionId,
                taskDefinitionKey, applicantName);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", applicantName='" + applicantName + '\'' +
                '}';
    }
}