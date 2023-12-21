package api;

import org.junit.Before;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;
public class UserTest {
    private User u1,u2,u3;

    @Before
    public void init() {
        u1 = new Customer("Stelios", "Bairamhs", "StBair", "123456");
        u2 = new Customer("Anastasios", "Gkogkas", "AnastGk", "654321");
        u3 = new Provider("Provider", "Providing", "Prov", "12345Secure");

        User.addUser(u1.getUsername(), u1.getPassword());
        User.addUser(u2.getUsername(), u2.getPassword());
        User.addUser(u3.getUsername(), u3.getPassword());
    }

    @Test
    public void testGetAndSetPassword() {
        u2.setPassword("rot19");
        assertEquals("rot19", u2.getPassword());
    }

    @Test
    public void testGetAndSetUsername() {
        u1.setUsername("StelBair");
        assertEquals("StelBair", u1.getUsername());
    }

    @Test
    public void testGetName() {
        assertEquals("Provider", u3.getName());
    }

    @Test
    public void testInheritanceTree() {
        assertTrue(u1 instanceof Customer);
        assertTrue(u2 instanceof Customer);
        assertTrue(u3 instanceof Provider);

        assertFalse(u1 instanceof Provider);
        assertFalse(u2 instanceof Provider);
        assertFalse(u3 instanceof Customer);

        assertTrue(u1 instanceof User);
        assertTrue(u2 instanceof User);
        assertTrue(u3 instanceof User);
    }

    @Test
    public void testIfUserAlreadyExists() {
        assertTrue(User.checkIfUserAlreadyExists("Prov"));
        assertFalse(User.checkIfUserAlreadyExists("NonExistent"));
    }

    @Test
    public void testAuthentication() {
        assertTrue(User.authenticateUser(u1.getUsername(), u1.getPassword()));
        assertFalse(User.authenticateUser("NonExistent", "NotSecure"));
    }

    @Test
    public void testFindUserForLogin() {
        assertNull(User.findUserForLogin("NonExistent", "NotSecure"));
    }
}