package service;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService();
    }

    @Test
    void registerUser_ShouldReturnUser_WhenNew_Test() {
        User user = userService.registerUser("Alice");
        assertNotNull(user);
        assertEquals("Alice", user.getName());

        User secondTry = userService.registerUser("Alice");
        assertNull(secondTry);
    }

    @Test
    void getOrCreateUser_ShouldReturnExistingUser_WhenUserExists_Test() {
        userService.registerUser("Bob");
        User user = userService.getOrCreateUser("Bob");
        assertNotNull(user);
        assertEquals("Bob", user.getName());
    }

    @Test
    void getOrCreateUser_ShouldCreateAndReturnNewUser_WhenUserNotExists_Test() {
        User user = userService.getOrCreateUser("Charlie");
        assertNotNull(user);
        assertEquals("Charlie", user.getName());

        User again = userService.getUser("Charlie");
        assertSame(user, again);
    }

    @Test
    void getUser_ShouldReturnUser_WhenExists_Test() {
        userService.registerUser("Diana");
        User user = userService.getUser("Diana");
        assertNotNull(user);
        assertEquals("Diana", user.getName());
    }

    @Test
    void getUser_ShouldReturnNull_WhenNotExists_Test() {
        User user = userService.getUser("Nobody");
        assertNull(user);
    }

    @Test
    void userExists_ShouldReturnTrue_WhenUserExists_Test() {
        userService.registerUser("Eve");
        assertTrue(userService.userExists("Eve"));
    }

    @Test
    void userExists_ShouldReturnFalse_WhenUserDoesNotExist_Test() {
        assertFalse(userService.userExists("Frank"));
    }

    @Test
    void getAllUsers_ShouldReturnAllRegisteredUsers_Test() {
        userService.registerUser("Gina");
        userService.registerUser("Harry");

        var allUsers = userService.getAllUsers();
        int count = 0;
        for (User user : allUsers) {
            assertNotNull(user.getName());
            count++;
        }
        assertEquals(2, count);
    }
}
