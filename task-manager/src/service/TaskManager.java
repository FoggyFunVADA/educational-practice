package service;

import enums.Priority;
import model.Task;
import model.User;
import enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(String description, User assignee, Priority priority) {
        tasks.add(new Task(description, assignee, priority));
    }

    public void changeTaskStatus(int taskId, TaskStatus status) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setStatus(status);
                return;
            }
        }
    }

    public void changeTaskPriority(int taskId, Priority priority) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setPriority(priority);
                return;
            }
        }
    }

    public List<Task> getTasksByUser(User user) {
        return tasks.stream()
                .filter(t -> t.getAssignee().getId() == user.getId())
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
