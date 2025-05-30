import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import utils.ConsoleInterface;


// Задача:
// Разработать систему управления задачами для команды разработки.
// Система должна позволять добавлять задачи, назначать их исполнителям, изменять статус,
// а также фильтровать задачи по исполнителю и статусу.


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Application started");
        ConsoleInterface consoleInterface = new ConsoleInterface();
        consoleInterface.run();
        logger.info("Application finished");
    }
}
