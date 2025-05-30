package service.notification;

import model.User;

public interface NotificationService {
    void notifyUser(User user, String message);
}
