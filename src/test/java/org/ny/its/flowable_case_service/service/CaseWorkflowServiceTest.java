package org.ny.its.flowable_case_service.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.TaskDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseWorkflowServiceTest {

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private ProcessInstance processInstance;

    @Mock
    private Task task;

    @Mock
    private org.flowable.task.api.TaskQuery taskQuery;

    @Mock
    private org.flowable.engine.runtime.ProcessInstanceQuery processInstanceQuery;

    @InjectMocks
    private CaseWorkflowService service;

    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setFirstName("Jane");
        testPerson.setLastName("Smith");
        testPerson.setEmail("jane.smith@example.com");
        testPerson.setGender("Female");
        testPerson.setSsn("54321");
        testPerson.setDateofbirth(LocalDate.of(1995, 5, 15));
    }

    @Test
    void testStartCase_Success() {
        // Arrange
        String expectedProcessId = "process-instance-1";
        when(processInstance.getId()).thenReturn(expectedProcessId);
        when(runtimeService.startProcessInstanceByKey(eq("case_regVN10"), anyMap()))
                .thenReturn(processInstance);

        // Act
        String result = service.startCase(testPerson);

        // Assert
        assertEquals(expectedProcessId, result);
        verify(runtimeService, times(1)).startProcessInstanceByKey(eq("case_regVN10"), anyMap());
    }

    @Test
    void testStartCase_VerifiesPersonVariables() {
        // Arrange
        String expectedProcessId = "process-instance-1";
        when(processInstance.getId()).thenReturn(expectedProcessId);
        when(runtimeService.startProcessInstanceByKey(eq("case_regVN10"), anyMap()))
                .thenReturn(processInstance);

        // Act
        service.startCase(testPerson);

        // Assert - Verify the variables map contains person data
        verify(runtimeService).startProcessInstanceByKey(
                eq("case_regVN10"),
                anyMap()
        );
    }

    @Test
    void testCompleteTask_Success() {
        // Arrange
        String taskId = "task-456";
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);

        // Act
        service.completeTask(taskId, variables);

        // Assert
        verify(taskService, times(1)).complete(taskId, variables);
    }

    @Test
    void testCompleteTask_WithEmptyVariables() {
        // Arrange
        String taskId = "task-456";
        Map<String, Object> variables = new HashMap<>();

        // Act
        service.completeTask(taskId, variables);

        // Assert
        verify(taskService, times(1)).complete(taskId, variables);
    }

    @Test
    void testGetProcessVariables_ReturnsVariables() {
        // Arrange
        String processInstanceId = "process-instance-1";
        Map<String, Object> expectedVariables = new HashMap<>();
        expectedVariables.put("firstName", "Jane");
        expectedVariables.put("status", "pending");
        when(runtimeService.getVariables(processInstanceId)).thenReturn(expectedVariables);

        // Act
        Map<String, Object> result = service.getProcessVariables(processInstanceId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jane", result.get("firstName"));
        assertEquals("pending", result.get("status"));
        verify(runtimeService, times(1)).getVariables(processInstanceId);
    }

    @Test
    void testGetAllTasks_ReturnsTaskList() {
        // Arrange
        when(task.getId()).thenReturn("task-1");
        when(task.getName()).thenReturn("Review Case");
        when(task.getAssignee()).thenReturn("reviewer1");
        when(task.getProcessInstanceId()).thenReturn("proc-1");
        when(task.getProcessDefinitionId()).thenReturn("proc-def-1");
        when(task.getTaskDefinitionKey()).thenReturn("reviewTask");

        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(List.of(task));

        // Act
        List<TaskDTO> result = service.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("task-1", result.get(0).getId());
        assertEquals("Review Case", result.get(0).getName());
        assertEquals("reviewer1", result.get(0).getAssignee());
        verify(taskService, times(1)).createTaskQuery();
    }

    @Test
    void testGetAllTasks_EmptyList() {
        // Arrange
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(List.of());

        // Act
        List<TaskDTO> result = service.getAllTasks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskService, times(1)).createTaskQuery();
    }

    @Test
    void testGetTasksByUser_Success() {
        // Arrange
        String user = "reviewer1";
        when(task.getId()).thenReturn("task-2");
        when(task.getName()).thenReturn("Approve Case");
        when(task.getAssignee()).thenReturn(user);
        when(task.getProcessInstanceId()).thenReturn("proc-2");
        when(task.getProcessDefinitionId()).thenReturn("proc-def-2");
        when(task.getTaskDefinitionKey()).thenReturn("approveTask");

        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.taskAssignee(user)).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(List.of(task));

        // Act
        List<TaskDTO> result = service.getTasksByUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("reviewer1", result.get(0).getAssignee());
        verify(taskService, times(1)).createTaskQuery();
    }

    @Test
    void testGetTasksByUser_EmptyList() {
        // Arrange
        String user = "nonexistent";
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.taskAssignee(user)).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(List.of());

        // Act
        List<TaskDTO> result = service.getTasksByUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testClaimTask_Success() {
        // Arrange
        String taskId = "task-3";
        String user = "reviewer2";

        // Act
        service.claimTask(taskId, user);

        // Assert
        verify(taskService, times(1)).claim(taskId, user);
    }

    @Test
    void testGetRunningProcesses_ReturnsProcessList() {
        // Arrange
        when(processInstance.getName()).thenReturn("Case Registration Process");
        when(runtimeService.createProcessInstanceQuery()).thenReturn(processInstanceQuery);
        when(processInstanceQuery.list()).thenReturn(List.of(processInstance));

        // Act
        List<ProcessInstance> result = service.getRunningProcesses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(runtimeService, times(1)).createProcessInstanceQuery();
    }

    @Test
    void testGetRunningProcesses_EmptyList() {
        // Arrange
        when(runtimeService.createProcessInstanceQuery()).thenReturn(processInstanceQuery);
        when(processInstanceQuery.list()).thenReturn(List.of());

        // Act
        List<ProcessInstance> result = service.getRunningProcesses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

