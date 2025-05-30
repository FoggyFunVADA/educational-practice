package model;

import enums.Priority;
import enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private static int counter = 1;
    private final int id;
    private final String description;
    private final User assignee;
    private TaskStatus status;
    private Priority priority;
    private final List<Task> subtasks = new ArrayList<>();

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
    public String getDescription() { return description; }
    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void setStatus(TaskStatus status) { this.status = status; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void addSubtask(Task subtask) {
        subtasks.add(subtask);
    }

    @Override
    public String toString() {
        return "Task #" + id + ": " + description +
                " | Assignee: " + assignee.getName() +
                " | Status: " + status +
                " | Priority: " + priority;
    }
}
