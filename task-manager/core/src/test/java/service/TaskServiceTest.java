package service;

import enums.Priority;
import enums.TaskStatus;
import model.Task;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.notification.NotificationService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private NotificationService notificationService;

    private TaskService taskService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(notificationService);
        user1 = new User("User1");
        user2 = new User("User2");
    }

    @Test
    void addTask_ShouldAddTaskAndNotify_Test() {
        taskService.addTask("Task 1", user1, Priority.HIGH);

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(1, tasks.size());

        Task added = tasks.get(0);
        assertEquals("Task 1", added.getDescription());
        assertEquals(user1.getId(), added.getAssignee().getId());
        assertEquals(Priority.HIGH, added.getPriority());

        verify(notificationService).notifyUser(eq(user1), contains("New task added"));
    }

    @Test
    void changeTaskStatus_ShouldChangeAndNotify_WhenTaskExists_Test() {
        taskService.addTask("Task 2", user1, Priority.MEDIUM);
        int id = taskService.getAllTasks().get(0).getId();

        taskService.changeTaskStatus(id, TaskStatus.DONE);

        Task task = taskService.getAllTasks().get(0);
        assertEquals(TaskStatus.DONE, task.getStatus());

        verify(notificationService).notifyUser(eq(user1), contains("Task status changed"));
    }

    @Test
    void changeTaskStatus_ShouldNotNotify_WhenTaskNotFound_Test() {
        taskService.changeTaskStatus(999, TaskStatus.DONE);

        verify(notificationService, never()).notifyUser(any(), any());
    }

    @Test
    void changeTaskPriority_ShouldChangeAndNotify_WhenTaskExists_Test() {
        taskService.addTask("Task 3", user2, Priority.LOW);
        int id = taskService.getAllTasks().get(0).getId();

        taskService.changeTaskPriority(id, Priority.HIGH);

        Task task = taskService.getAllTasks().get(0);
        assertEquals(Priority.HIGH, task.getPriority());

        verify(notificationService).notifyUser(eq(user2), contains("Task priority changed"));
    }

    @Test
    void changeTaskPriority_ShouldNotNotify_WhenTaskNotFound_Test() {
        taskService.changeTaskPriority(999, Priority.HIGH);

        verify(notificationService, never()).notifyUser(any(), any());
    }

    @Test
    void getTasksByUser_ShouldReturnCorrectTasks_Test() {
        taskService.addTask("Task 4", user1, Priority.HIGH);
        taskService.addTask("Task 5", user2, Priority.MEDIUM);
        taskService.addTask("Task 6", user1, Priority.LOW);

        List<Task> user1Tasks = taskService.getTasksByUser(user1);
        assertEquals(2, user1Tasks.size());
        assertTrue(user1Tasks.stream().allMatch(t -> t.getAssignee().getId() == user1.getId()));
    }

    @Test
    void getTasksByStatus_ShouldReturnCorrectTasks_Test() {
        taskService.addTask("Task 7", user1, Priority.HIGH);
        int id = taskService.getAllTasks().get(0).getId();
        taskService.changeTaskStatus(id, TaskStatus.IN_PROGRESS);

        List<Task> inProgressTasks = taskService.getTasksByStatus(TaskStatus.IN_PROGRESS);
        assertEquals(1, inProgressTasks.size());
        assertEquals(TaskStatus.IN_PROGRESS, inProgressTasks.get(0).getStatus());
    }

    @Test
    void getTasksByPriority_ShouldReturnCorrectTasks_Test() {
        taskService.addTask("Task 8", user2, Priority.LOW);
        taskService.addTask("Task 9", user1, Priority.LOW);
        taskService.addTask("Task 10", user1, Priority.HIGH);

        List<Task> lowPriorityTasks = taskService.getTasksByPriority(Priority.LOW);
        assertEquals(2, lowPriorityTasks.size());
        assertTrue(lowPriorityTasks.stream().allMatch(t -> t.getPriority() == Priority.LOW));
    }

    @Test
    void getStatusStatistics_ShouldReturnCorrectCounts_Test() {
        taskService.addTask("Task 11", user1, Priority.HIGH);
        taskService.addTask("Task 12", user1, Priority.LOW);
        int id = taskService.getAllTasks().get(0).getId();
        taskService.changeTaskStatus(id, TaskStatus.DONE);

        Map<TaskStatus, Long> stats = taskService.getStatusStatistics();
        assertEquals(1L, stats.getOrDefault(TaskStatus.DONE, 0L));
        assertEquals(1L, stats.getOrDefault(TaskStatus.NEW, 0L));
    }

    @Test
    void getPriorityStatistics_ShouldReturnCorrectCounts_Test() {
        taskService.addTask("Task 13", user1, Priority.HIGH);
        taskService.addTask("Task 14", user1, Priority.HIGH);
        taskService.addTask("Task 15", user2, Priority.LOW);

        Map<Priority, Long> stats = taskService.getPriorityStatistics();
        assertEquals(2L, stats.getOrDefault(Priority.HIGH, 0L));
        assertEquals(1L, stats.getOrDefault(Priority.LOW, 0L));
    }

    @Test
    void addSubtask_ShouldAddAndNotify_WhenParentExists_Test() {
        taskService.addTask("Parent task", user1, Priority.MEDIUM);
        int parentId = taskService.getAllTasks().get(0).getId();

        boolean added = taskService.addSubtask(parentId, "Subtask 1", user2, Priority.LOW);

        assertTrue(added);
        Task parent = taskService.getAllTasks().get(0);
        assertEquals(1, parent.getSubtasks().size());
        assertEquals("Subtask 1", parent.getSubtasks().get(0).getDescription());

        verify(notificationService).notifyUser(eq(user2), contains("New subtask added"));
    }

    @Test
    void addSubtask_ShouldReturnFalse_WhenParentNotFound_Test() {
        boolean added = taskService.addSubtask(9999, "Subtask X", user1, Priority.HIGH);
        assertFalse(added);
        verify(notificationService, never()).notifyUser(any(), any());
    }
}
