package service;

import enums.Priority;
import model.Task;
import model.User;
import enums.TaskStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);
    private final NotificationService notificationService;
    private final List<Task> tasks = new ArrayList<>();

    public TaskService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void addTask(String description, User assignee, Priority priority) {
        Task task = new Task(description, assignee, priority);
        tasks.add(task);
        notificationService.notifyUser(assignee, "New task added: " + description);
        logger.info("Task added: " + task);
    }

    public void changeTaskStatus(int taskId, TaskStatus status) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                TaskStatus oldStatus = task.getStatus();
                task.setStatus(status);
                logger.info("Changing status of task " + taskId +
                        " from " + oldStatus + " to " + status);
                notificationService.notifyUser(task.getAssignee(), "Task status changed to " + status);
                return;
            }
        }
        logger.warn("Task with id=" + taskId + " not found, cannot change status");
    }

    public void changeTaskPriority(int taskId, Priority priority) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                Priority oldPriority = task.getPriority();
                task.setPriority(priority);
                logger.info("Changing priority of task " + taskId +
                        " from " + oldPriority + " to " + priority);
                notificationService.notifyUser(task.getAssignee(), "Task priority changed to " + priority);
                return;
            }
        }
        logger.warn("Task with id=" + taskId + " not found, cannot change priority");
    }

    public List<Task> getTasksByUser(User user) {
        logger.debug("Requested tasks for user: " + user);
        return tasks.stream()
                .filter(t -> t.getAssignee().getId() == user.getId())
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.debug("Requested tasks by status: " + status);
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        logger.debug("Requested tasks by priority: " + priority);
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        logger.debug("Retrieving all tasks (" + tasks.size() + " total)");
        return new ArrayList<>(tasks);
    }

    public Map<TaskStatus, Long> getStatusStatistics() {
        logger.debug("Requested status statistics");
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    public Map<Priority, Long> getPriorityStatistics() {
        logger.debug("Requested priority statistics");
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }

    public boolean addSubtask(int parentTaskId, String description, User assignee, Priority priority) {
        for (Task parent : tasks) {
            if (parent.getId() == parentTaskId) {
                Task subtask = new Task(description, assignee, priority);
                parent.addSubtask(subtask);
                logger.debug("Added subtask to task id=" + parentTaskId + ": " + subtask);
                notificationService.notifyUser(assignee, "New subtask added: " + description);
                return true;
            }
        }
        logger.warn("Parent task not found for adding subtask: id=" + parentTaskId);
        return false;
    }
}
