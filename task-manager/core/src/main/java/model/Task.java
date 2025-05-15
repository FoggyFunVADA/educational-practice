package model;

import enums.Priority;
import enums.TaskStatus;

public class Task {
    private static int counter = 1;
    private final int id;
    private final String description;
    private final User assignee;
    private TaskStatus status;
    private Priority priority;

    public Task(String description, User assignee, Priority priority) {
        this.id = counter++;
        this.description = description;
        this.assignee = assignee;
        this.status = TaskStatus.NEW;
        this.priority = priority;
    }

    public int getId() { return id; }
    public User getAssignee() { return assignee; }
    public TaskStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }

    public void setStatus(TaskStatus status) { this.status = status; }
    public void setPriority(Priority priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "Задача #" + id + ": " + description +
               " | Исполнитель: " + assignee.getName() +
               " | Статус: " + status +
               " | Приоритет: " + priority;
    }
}
