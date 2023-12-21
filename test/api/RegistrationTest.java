package api;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationTest {

    User u1,u2;

    @Before
    public void init() {
        u1 = new Customer("Anastasios", "Gkogkas", "AnastGk08", "12345Secure");
        u2 = new Customer("Stelios", "Bairamhs", "StelBair8", "Secure54321");

        User.getAllUsers().clear();
        User.addUser(u1.getUsername(), u1.getPassword());
        User.addUser(u2.getUsername(), u2.getPassword());
    }

    @Test
    public void matchesPattern() {
        String input = "AnastGk09";
        assertTrue(Registration.matchesPattern(input));

        input = "Anast_+G2#K";
        assertFalse(Registration.matchesPattern(input));

        input = "Anast Gk";
        assertFalse(Registration.matchesPattern(input));
    }

    @Test
    public void checkUserName() {
        String input = "AnastGk09";
        assertTrue(Registration.checkUserName(input));

        input = "AnastGk08";
        assertTrue(Registration.checkUserName(input));
    }
}