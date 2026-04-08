package org.ny.its.flowable_case_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.ny.its.flowable_case_service.model.Person;
import org.ny.its.flowable_case_service.model.TaskDTO;
import org.ny.its.flowable_case_service.service.CaseWorkflowService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaseWorkflowService caseWorkflowService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person testPerson;
    private TaskDTO testTask;

    @BeforeEach
    void setUp() {
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
    void testStartCase_ReturnsProcessId() throws Exception {
        // Arrange
        String expectedProcessId = "process-id-123";
        when(caseWorkflowService.startCase(any())).thenReturn(expectedProcessId);

        // Act & Assert
        mockMvc.perform(post("/api/cases/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedProcessId));

        verify(caseWorkflowService, times(1)).startCase(any());
    }

    @Test
    void testStartCase_WithInvalidPerson() throws Exception {
        // Arrange - Person with missing required fields
        Person invalidPerson = new Person();
        invalidPerson.setFirstName(""); // Invalid - required field empty

        // Act & Assert
        mockMvc.perform(post("/api/cases/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetTasks_ReturnsTaskList() throws Exception {
        // Arrange
        List<TaskDTO> tasks = List.of(testTask);
        when(caseWorkflowService.getTasksForAssignee("user1")).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/cases/tasks/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("task-123")))
                .andExpect(jsonPath("$[0].name", is("Test Task")))
                .andExpect(jsonPath("$[0].assignee", is("user1")));

        verify(caseWorkflowService, times(1)).getTasksForAssignee("user1");
    }

    @Test
    void testGetTasks_EmptyList() throws Exception {
        // Arrange
        when(caseWorkflowService.getTasksForAssignee("nonexistent")).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/cases/tasks/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(caseWorkflowService, times(1)).getTasksForAssignee("nonexistent");
    }

    @Test
    void testCompleteTask_Success() throws Exception {
        // Arrange
        Map<String, Object> variables = new HashMap<>();
        variables.put("status", "approved");

        // Act & Assert
        mockMvc.perform(post("/api/cases/tasks/task-123/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task completed"));

        verify(caseWorkflowService, times(1)).completeTask("task-123", variables);
    }

    @Test
    void testGetProcessVariables_ReturnsVariables() throws Exception {
        // Arrange
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", "John");
        variables.put("lastName", "Doe");
        when(caseWorkflowService.getProcessVariables("process-123")).thenReturn(variables);

        // Act & Assert
        mockMvc.perform(get("/api/cases/process/process-123/variables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(caseWorkflowService, times(1)).getProcessVariables("process-123");
    }

    @Test
    void testTestEndpoint_ReturnsMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cases/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("API is working!"));
    }

    @Test
    void testGetTasks_WithDifferentAssignee() throws Exception {
        // Arrange
        TaskDTO task2 = new TaskDTO("task-456", "Another Task", "user2",
                "proc-456", "proc-def-456", "anotherTask", "Jane Smith");
        List<TaskDTO> tasks = List.of(task2);
        when(caseWorkflowService.getTasksForAssignee("user2")).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/cases/tasks/user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].assignee", is("user2")));

        verify(caseWorkflowService, times(1)).getTasksForAssignee("user2");
    }
}

