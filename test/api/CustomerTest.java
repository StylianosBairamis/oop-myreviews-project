package api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class CustomerTest {
    private Accommodation a1, a2;

    private Provider p1;

    private Customer c1, c2;

    private Review r1, r2, r3;
    @Before
    public void init() {
        Customer.getAllUsers().clear();
        c1 = new Customer("Stelios", "Bairamhs", "StBair", "123456");
        c2 = new Customer("Anastasios", "Gkogkas", "AnastGk", "654321");

        p1 = new Provider("Giwrgos", "Katsos", "GiKat", "1234Secure");
        a1 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a2 = new Accommodation("Test1", "Ξενοδοχείο", "Test2", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        p1.addAccommodation(a1);
        p1.addAccommodation(a2);

        r1 = new Review(4, "Very good place.", a1, c1);
        r2 = new Review(3.5, "A very good place.", a1, c2);
        r3 = new Review(3.75, "A very good place2.", a2, c2);

        a1.addReview(r1);
        a1.addReview(r2);
        a2.addReview(r3);

        c1.reviewAdd(r1.getRating(), r1.getReviewContent(), r1.getReviewedAcc(), r1.getCustomer());
        c2.reviewAdd(r2.getRating(), r2.getReviewContent(), r2.getReviewedAcc(), r2.getCustomer());
        c2.reviewAdd(r3.getRating(), r3.getReviewContent(), r3.getReviewedAcc(), r3.getCustomer());

        Customer.getListOfCustomers().clear();
        Customer.getListOfCustomers().add(c1);
        Customer.getListOfCustomers().add(c2);
    }

    @Test
    public void testSetDataForTable() {
        Object[][] data = new Object[c1.getMyReviews().size()][6];
        Accommodation temp = r1.getReviewedAcc();
        String[] tempArray;
        for (int i = 0; i < data.length; i++) {//Οσες είναι οι γραμμές του πίνακα
            tempArray = new String[6];
            tempArray[0] = temp.getName();
            tempArray[1] = temp.getType();
            tempArray[2] = temp.getCity();
            tempArray[3] = temp.getRoad();
            tempArray[4] = temp.getPostalCode();
            tempArray[5] = c1.getMyReviews().get(i).getRating() == -1 ? "Καμία Αξιολόγηση." : ((double)Math.round(c1.getMyReviews().get(i).getRating() * 100) / 100) + "";
            data[i] = tempArray;
        }

        Assert.assertEquals(data, c1.setDataForTable());
    }

    @Test
    public void testSetReviewContent() {
        // Test myReviewsChange
        Assert.assertEquals(r2.getReviewContent(), "A very good place.");
        c2.setReviewContent(c2.getMyReviews().indexOf(r2), "Not a very good place after all...");
        Assert.assertEquals("Not a very good place after all...", c2.getMyReviews().get(c2.getMyReviews().indexOf(r2)).getReviewContent());
    }

    @Test
    public void testSetRating() {
        c2.setRating(c2.getMyReviews().indexOf(r3), 3.65);
        Assert.assertEquals(3.7, c2.countAverageReviews(), 0.01);
    }

    @Test
    public void testRemoveReview() {
        // Test Defaults
        Assert.assertEquals(3.62, c2.countAverageReviews(), 0.01);
        Assert.assertEquals(2, c2.getMyReviews().size());

        // Test Customer.removeReview
        c2.removeReview(r3);
        Assert.assertEquals(3.75, c2.countAverageReviews() , 0.01);
        Assert.assertEquals(1, c2.getMyReviews().size());
    }

    @Test
    public void testGetCustomerList() {
        Assert.assertEquals(2, Customer.getListOfCustomers().size());
    }

    @Test
    public void testGetAverage() {
        Assert.assertEquals(3.62, c2.countAverageReviews(), 0.01d);
    }
}