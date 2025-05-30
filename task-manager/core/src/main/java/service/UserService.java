package service;

import model.User;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class);
    private final Map<String, User> users = new HashMap<>();

    public User registerUser(String name) {
        if (users.containsKey(name)) {
            logger.info("Attempt to register an existing user: " + name);
            return null;
        }
        User user = new User(name);
        users.put(name, user);
        logger.info("New user registered: " + name);
        return user;
    }

    public User getOrCreateUser(String name) {
        User user = users.get(name);
        if (user == null) {
            user = new User(name);
            users.put(name, user);
            logger.info("Created new user via getOrCreate: " + name);
        } else {
            logger.info("User found: " + name);
        }
        return user;
    }

    public User getUser(String name) {
        User user = users.get(name);
        if (user != null) {
            logger.info("User retrieved: " + name);
        } else {
            logger.warn("User not found: " + name);
        }
        return user;
    }

    public boolean userExists(String name) {
        return users.containsKey(name);
    }

    public Iterable<User> getAllUsers() {
        return users.values();
    }
}
