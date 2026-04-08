package org.ny.its.flowable_case_service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");
    }

    @Test
    void testTaskDTOCreation_Success() {
        // Assert that TaskDTO was created successfully
        assertNotNull(taskDTO);
        assertEquals("task-001", taskDTO.getId());
        assertEquals("Review Application", taskDTO.getName());
        assertEquals("reviewer1", taskDTO.getAssignee());
        assertEquals("process-001", taskDTO.getProcessInstanceId());
        assertEquals("process-def-001", taskDTO.getProcessDefinitionId());
        assertEquals("reviewTask", taskDTO.getTaskDefinitionKey());
        assertEquals("John Doe", taskDTO.getApplicantName());
    }

    @Test
    void testTaskDTOAllArgsConstructor() {
        // Act
        TaskDTO dto = new TaskDTO("task-002", "Approve Application", "approver1",
                "process-002", "process-def-002", "approveTask", "Jane Smith");

        // Assert
        assertEquals("task-002", dto.getId());
        assertEquals("Approve Application", dto.getName());
        assertEquals("approver1", dto.getAssignee());
        assertEquals("process-002", dto.getProcessInstanceId());
        assertEquals("process-def-002", dto.getProcessDefinitionId());
        assertEquals("approveTask", dto.getTaskDefinitionKey());
        assertEquals("Jane Smith", dto.getApplicantName());
    }

    @Test
    void testTaskDTONoArgsConstructor() {
        // Act - Note: We need to use the all-args constructor since @NoArgsConstructor is required
        TaskDTO dto = new TaskDTO(null, null, null, null, null, null, null);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getName());
    }

    @Test
    void testTaskDTOSettersAndGetters() {
        // Arrange
        TaskDTO dto = new TaskDTO(null, null, null, null, null, null, null);

        // Act
        dto.setId("new-task-id");
        dto.setName("New Task Name");
        dto.setAssignee("new-assignee");
        dto.setProcessInstanceId("new-process-id");
        dto.setProcessDefinitionId("new-process-def-id");
        dto.setTaskDefinitionKey("newTaskKey");
        dto.setApplicantName("New Applicant");

        // Assert
        assertEquals("new-task-id", dto.getId());
        assertEquals("New Task Name", dto.getName());
        assertEquals("new-assignee", dto.getAssignee());
        assertEquals("new-process-id", dto.getProcessInstanceId());
        assertEquals("new-process-def-id", dto.getProcessDefinitionId());
        assertEquals("newTaskKey", dto.getTaskDefinitionKey());
        assertEquals("New Applicant", dto.getApplicantName());
    }

    @Test
    void testTaskDTOEquality() {
        // Arrange
        TaskDTO dto1 = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");
        TaskDTO dto2 = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void testTaskDTOInequality() {
        // Arrange
        TaskDTO dto1 = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");
        TaskDTO dto2 = new TaskDTO("task-002", "Approve Application", "approver1",
                "process-002", "process-def-002", "approveTask", "Jane Smith");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testTaskDTOHashCode() {
        // Arrange
        TaskDTO dto1 = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");
        TaskDTO dto2 = new TaskDTO("task-001", "Review Application", "reviewer1",
                "process-001", "process-def-001", "reviewTask", "John Doe");

        // Act & Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testTaskDTOToString() {
        // Act
        String toString = taskDTO.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("task-001") || toString.contains("id"));
    }

    @Test
    void testTaskDTOWithNullFields() {
        // Arrange & Act
        TaskDTO dto = new TaskDTO(null, null, null, null, null, null, null);

        // Assert
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getAssignee());
        assertNull(dto.getProcessInstanceId());
        assertNull(dto.getProcessDefinitionId());
        assertNull(dto.getTaskDefinitionKey());
        assertNull(dto.getApplicantName());
    }
}

