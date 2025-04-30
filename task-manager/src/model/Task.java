package model;

import util.TaskStatus;

public class Task {
    private static int counter = 1;
    private final int id;
    private final String description;
    private final User assignee;
    private TaskStatus status;

    public Task(String description, User assignee) {
        this.id = counter++;
        this.description = description;
        this.assignee = assignee;
        this.status = TaskStatus.NEW;
    }

    public int getId() { return id; }
    public User getAssignee() { return assignee; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Задача #" + id + ": " + description +
               " | Исполнитель: " + assignee.getName() +
               " | Статус: " + status;
    }
}
