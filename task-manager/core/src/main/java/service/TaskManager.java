package service;

import enums.Priority;
import model.Task;
import model.User;
import enums.TaskStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static final Logger logger = LogManager.getLogger(TaskManager.class);
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(String description, User assignee, Priority priority) {
        Task task = new Task(description, assignee, priority);
        tasks.add(task);
        logger.info("Добавлена задача: " + task);
    }

    public void changeTaskStatus(int taskId, TaskStatus status) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                logger.info("Изменение статуса задачи " + taskId +
                        " с " + task.getStatus() + " на " + status);
                task.setStatus(status);
                return;
            }
        }
        logger.warn("Не найдена задача с id=" + taskId + ", невозможно изменить статус");
    }

    public void changeTaskPriority(int taskId, Priority priority) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                logger.info("Изменение приоритета задачи " + taskId +
                        " с " + task.getPriority() + " на " + priority);
                task.setPriority(priority);
                return;
            }
        }
        logger.warn("Не найдена задача с id=" + taskId + ", невозможно изменить приоритет");
    }

    public List<Task> getTasksByUser(User user) {
        logger.debug("Запрошены задачи пользователя: " + user);
        return tasks.stream()
                .filter(t -> t.getAssignee().getId() == user.getId())
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.debug("Запрошены задачи по статусу: " + status);
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        logger.debug("Запрошены задачи по приоритету: " + priority);
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        logger.debug("Получение всех задач (" + tasks.size() + " шт)");
        return new ArrayList<>(tasks);
    }
}
