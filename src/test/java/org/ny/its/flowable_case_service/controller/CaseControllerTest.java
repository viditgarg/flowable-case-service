package org.ny.its.flowable_case_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.CaseWorkflowService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseControllerTest {

    @Mock
    private CaseWorkflowService service;

    @InjectMocks
    private CaseController controller;

    private Person testPerson;
    private TaskDTO testTask;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testPerson = new Person();
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setEmail("john.doe@example.com");
        testPerson.setGender("Male");
        testPerson.setSsn("12345");
        testPerson.setDateofbirth(LocalDate.of(1990, 1, 1));

        testTask = new TaskDTO("task-123", "Test Task", "user1",
                "proc-123", "proc-def-123", "testTask", "John Doe");
    }

    @Test
    void testStartCase_Success() {
        // Arrange
        String expectedProcessId = "process-id-123";
        when(service.startCase(testPerson)).thenReturn(expectedProcessId);

        // Act
        String result = controller.startCase(testPerson);

        // Assert
        assertEquals(expectedProcessId, result);
        verify(service, times(1)).startCase(testPerson);
    }

    @Test
    void testStartCase_WithNullPerson() {
        // Arrange
        when(service.startCase(null)).thenThrow(new NullPointerException("Person cannot be null"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> controller.startCase(null));
        verify(service, times(1)).startCase(null);
    }

    @Test
    void testGetTasks_ReturnsTaskList() {
        // Arrange
        String assignee = "user1";
        List<TaskDTO> expectedTasks = List.of(testTask);
        when(service.getTasksForAssignee(assignee)).thenReturn(expectedTasks);

        // Act
        List<TaskDTO> result = controller.getTasks(assignee);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("task-123", result.get(0).getId());
        verify(service, times(1)).getTasksForAssignee(assignee);
    }

    @Test
    void testGetTasks_EmptyList() {
        // Arrange
        String assignee = "nonexistent-user";
        when(service.getTasksForAssignee(assignee)).thenReturn(List.of());

        // Act
        List<TaskDTO> result = controller.getTasks(assignee);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, times(1)).getTasksForAssignee(assignee);
    }

    @Test
    void testCompleteTask_Success() {
        // Arrange
        String taskId = "task-123";
        Map<String, Object> variables = new HashMap<>();
        variables.put("status", "approved");

        // Act
        String result = controller.completeTask(taskId, variables);

        // Assert
        assertEquals("Task completed", result);
        verify(service, times(1)).completeTask(taskId, variables);
    }

    @Test
    void testCompleteTask_WithEmptyVariables() {
        // Arrange
        String taskId = "task-123";
        Map<String, Object> variables = new HashMap<>();

        // Act
        String result = controller.completeTask(taskId, variables);

        // Assert
        assertEquals("Task completed", result);
        verify(service, times(1)).completeTask(taskId, variables);
    }

    @Test
    void testGetProcessVariables_ReturnsVariables() {
        // Arrange
        String processInstanceId = "process-123";
        Map<String, Object> expectedVariables = new HashMap<>();
        expectedVariables.put("firstName", "John");
        expectedVariables.put("lastName", "Doe");
        when(service.getProcessVariables(processInstanceId)).thenReturn(expectedVariables);

        // Act
        Map<String, Object> result = controller.getProcessVariables(processInstanceId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get("firstName"));
        assertEquals("Doe", result.get("lastName"));
        verify(service, times(1)).getProcessVariables(processInstanceId);
    }

    @Test
    void testGetProcessVariables_EmptyVariables() {
        // Arrange
        String processInstanceId = "process-123";
        when(service.getProcessVariables(processInstanceId)).thenReturn(new HashMap<>());

        // Act
        Map<String, Object> result = controller.getProcessVariables(processInstanceId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service, times(1)).getProcessVariables(processInstanceId);
    }

    @Test
    void testTestEndpoint() {
        // Act
        String result = controller.testEndpoint();

        // Assert
        assertEquals("API is working!", result);
    }
}

