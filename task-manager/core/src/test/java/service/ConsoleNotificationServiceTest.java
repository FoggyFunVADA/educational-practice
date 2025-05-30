package service;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import service.notification.ConsoleNotificationService;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleNotificationServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void notifyUser_ShouldPrintNotificationMessage() {
        ConsoleNotificationService service = new ConsoleNotificationService();
        User user = new User("TestUser");

        service.notifyUser(user, "Test message");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Notification for TestUser: Test message"));
    }
}
