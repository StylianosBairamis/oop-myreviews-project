package api;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {
    private Date d;
    private Accommodation a1,a2,a3;

    private Provider p1,p2,p3;

    private Customer c1,c2;

    private Review r1,r2,r3,r4,r5;

    @Before
    public void init() {
        p1 = new Provider("Giwrgos", "Katsos", "GiKat", "1234Secure");
        p2 = new Provider("Nikos", "Terros", "NikTer", "Secure1234");
        p3 = new Provider("Stelios", "Bairamhs", "StelBar", "Secure1234");

        a1 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a2 = new Accommodation("Test2", "Ξενοδοχείο", "Test2", "57400", "An even better place.",
                "Thessaloniki", new HashMap<String, String>());
        a3 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "57400", "A good place.",
                "Thessaloniki", new HashMap<String, String>());

        c1 = new Customer("Stelios", "Bairamhs", "StBair", "123456");
        c2 = new Customer("Anastasios", "Gkogkas", "AnastGk", "654321");

        r1 = new Review(4.0, "Good place", a1, c1);
        r2 = new Review(3.5, "Good place", a1, c2);
        r3 = new Review(4.75, "Very good place", a2, c2);
        d = new Date();
        r4 = new Review(2, "Bad place", a2, c1);
        r5 = new Review(5, "Excelent place", a3, c1);
        Customer.getListOfCustomers().clear();
        a1.addReview(r1);
        a1.addReview(r2);
        a2.addReview(r3);
        a2.addReview(r4);
        a3.addReview(r5);
    }

    @Test
    public void testGetAndSetRating() {
        r1.setRating(r1.getRating() + 0.5);
        assertEquals(4.5, r1.getRating(), 0.01);
    }

    @Test
    public void testGetAndSetContent() {
        r1.setReviewContent(r1.getReviewContent() + ".");
        assertEquals("Good place.", r1.getReviewContent());
    }

    @Test
    public void testGetAndSetReviewedAcc() {
        r1.setReviewedAcc(a3);
        assertEquals(a3, r1.getReviewedAcc());
        r1.setReviewedAcc(a1);
    }

    @Test
    public void testGetCustomer() {
        assertEquals(c1, r1.getCustomer());
    }

    @Test
    public void testHashCodeAndEquals() {
        assertFalse(r1.equals(r2));

        Review temp = r1;
        r1 = r2;
        assertTrue(r1.equals(r2));
        assertEquals(r1.hashCode(), r2.hashCode());
        r1 = temp;
    }

    @Test
    public void testPrintable() {
        String inp = "The rating of the accommodation is: 4.75\n" +
                "Review by: AnastGk\n" +
                "Review: Very good place\n" +
                "The review was uploaded at: " + d + "\n";
        assertEquals(r3.toString(), inp);
    }
}