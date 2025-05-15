import enums.Priority;
import model.User;
import service.TaskManager;
import enums.TaskStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

// Задача:
// Разработать систему управления задачами для команды разработки.
// Система должна позволять добавлять задачи, назначать их исполнителям, изменять статус,
// а также фильтровать задачи по исполнителю и статусу.

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Приложение запущено");

        User alice = new User("Alice");
        User bob = new User("Bob");

        TaskManager manager = new TaskManager();

        manager.addTask("Реализовать аутентификацию", alice, Priority.HIGH);
        manager.addTask("Настроить CI/CD", bob, Priority.MEDIUM);
        manager.addTask("Написать тесты", alice, Priority.LOW);

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

        System.out.println("-- Задачи с высоким приоритетом ---");
        manager.getTasksByPriority(Priority.HIGH).forEach(System.out::println);

        manager.changeTaskPriority(3, Priority.HIGH);

        System.out.println("-- Задачи с высоким приоритетом после изменения приоритетов ---");
        manager.getTasksByPriority(Priority.HIGH).forEach(System.out::println);

        logger.info("Программа завершена");
    }
}
