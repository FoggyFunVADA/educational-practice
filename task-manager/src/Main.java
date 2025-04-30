import model.User;
import service.TaskManager;
import util.TaskStatus;

// Задача:
// Разработать систему управления задачами для команды разработки.
// Система должна позволять добавлять задачи, назначать их исполнителям, изменять статус,
// а также фильтровать задачи по исполнителю и статусу.

public class Main {
    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob = new User("Bob");

        TaskManager manager = new TaskManager();

        manager.addTask("Реализовать аутентификацию", alice);
        manager.addTask("Настроить CI/CD", bob);
        manager.addTask("Написать тесты", alice);

        System.out.println("--- Все задачи ---");
        manager.getAllTasks().forEach(System.out::println);

        manager.changeTaskStatus(2, TaskStatus.IN_PROGRESS);
        manager.changeTaskStatus(1, TaskStatus.DONE);

        System.out.println("--- Все задачи после смены статусов ---");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("--- Задачи Alice ---");
        manager.getTasksByUser(alice).forEach(System.out::println);
        System.out.println("--- Задачи Bob ---");
        manager.getTasksByUser(bob).forEach(System.out::println);

        System.out.println("--- Выполненные задачи ---");
        manager.getTasksByStatus(TaskStatus.DONE).forEach(System.out::println);
    }
}
