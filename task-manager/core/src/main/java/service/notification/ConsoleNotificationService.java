package service.notification;

import model.User;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyUser(User user, String message) {
        System.out.printf("Notification for %s: %s%n", user.getName(), message);
    }
}
