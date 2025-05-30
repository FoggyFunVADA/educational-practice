package utils;

import enums.Priority;
import enums.TaskStatus;
import model.User;
import service.TaskService;
import service.UserService;
import service.notification.ConsoleNotificationService;
import service.notification.NotificationService;

import java.util.Map;
import java.util.Scanner;

public class ConsoleInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final NotificationService notificationService = new ConsoleNotificationService();
    private final TaskService manager = new TaskService(notificationService);
    private final UserService userService = new UserService();

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addTask();
                    case "2" -> manager.getAllTasks().forEach(System.out::println);
                    case "3" -> changeTaskStatus();
                    case "4" -> changeTaskPriority();
                    case "5" -> showTasksByUser();
                    case "6" -> showTasksByStatus();
                    case "7" -> showTasksByPriority();
                    case "8" -> registerUser();
                    case "9" -> listUsers();
                    case "10" -> showStatusStatistics();
                    case "11" -> showPriorityStatistics();
                    case "12" -> addSubtask();
                    case "0" -> {
                        System.out.println("Exiting...");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add task");
        System.out.println("2. Show all tasks");
        System.out.println("3. Change task status");
        System.out.println("4. Change task priority");
        System.out.println("5. Show tasks by user");
        System.out.println("6. Show tasks by status");
        System.out.println("7. Show tasks by priority");
        System.out.println("8. Register new user");
        System.out.println("9. Show all users");
        System.out.println("10. Show status statistics");
        System.out.println("11. Show priority statistics");
        System.out.println("12. Add subtask");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private void addTask() {
        System.out.print("Task description: ");
        String desc = scanner.nextLine();

        User user = getOrCreateUser();
        Priority priority = askPriority();

        manager.addTask(desc, user, priority);
        System.out.println("Task added.");
    }

    private void changeTaskStatus() {
        int id = askInt("Task ID: ");
        TaskStatus status = askStatus();
        manager.changeTaskStatus(id, status);
        System.out.println("Status updated.");
    }

    private void changeTaskPriority() {
        int id = askInt("Task ID: ");
        Priority priority = askPriority();
        manager.changeTaskPriority(id, priority);
        System.out.println("Priority updated.");
    }

    private void showTasksByUser() {
        User user = getExistingUser();
        if (user != null) {
            manager.getTasksByUser(user).forEach(System.out::println);
        } else {
            System.out.println("User not found.");
        }
    }

    private void showTasksByStatus() {
        TaskStatus status = askStatus();
        manager.getTasksByStatus(status).forEach(System.out::println);
    }

    private void showTasksByPriority() {
        Priority priority = askPriority();
        manager.getTasksByPriority(priority).forEach(System.out::println);
    }

    private void registerUser() {
        System.out.print("Enter new user name: ");
        String name = scanner.nextLine().trim();
        if (userService.registerUser(name) != null) {
            System.out.println("User registered.");
        } else {
            System.out.println("User already exists.");
        }
    }

    private void listUsers() {
        var users = userService.getAllUsers();
        if (!users.iterator().hasNext()) {
            System.out.println("No registered users.");
        } else {
            System.out.println("Registered users:");
            users.forEach(user -> System.out.println(user.getName()));
        }
    }

    private User getOrCreateUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine().trim();
        return userService.getOrCreateUser(name);
    }

    private User getExistingUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine().trim();
        return userService.getUser(name);
    }

    private Priority askPriority() {
        while (true) {
            System.out.print("Priority (HIGH/MEDIUM/LOW): ");
            try {
                return Priority.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Try again.");
            }
        }
    }

    private TaskStatus askStatus() {
        while (true) {
            System.out.print("Status (TODO/IN_PROGRESS/DONE): ");
            try {
                return TaskStatus.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Try again.");
            }
        }
    }

    private int askInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void showStatusStatistics() {
        Map<TaskStatus, Long> stats = manager.getStatusStatistics();
        System.out.println("Task status statistics:");
        for (TaskStatus status : TaskStatus.values()) {
            long count = stats.getOrDefault(status, 0L);
            System.out.printf("%s: %d%n", status, count);
        }
    }

    private void showPriorityStatistics() {
        Map<Priority, Long> stats = manager.getPriorityStatistics();
        System.out.println("Task priority statistics:");
        for (Priority priority : Priority.values()) {
            long count = stats.getOrDefault(priority, 0L);
            System.out.printf("%s: %d%n", priority, count);
        }
    }

    private void addSubtask() {
        int parentId = askInt("Parent task ID: ");
        System.out.print("Subtask description: ");
        String desc = scanner.nextLine();
        User assignee = getOrCreateUser();
        Priority priority = askPriority();

        boolean success = manager.addSubtask(parentId, desc, assignee, priority);
        if (success) {
            System.out.println("Subtask added.");
        } else {
            System.out.println("Parent task with given ID not found.");
        }
    }
}
