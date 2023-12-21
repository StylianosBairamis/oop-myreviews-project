package api;

import gui.App;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class ProviderTest {
    private Provider p1,p2,p3;
    private Accommodation a1,a2,a3,a4,a5;

    private Customer c1,c2;

    private Review r1,r2;

    @Before
    public void init() {
        p1 = new Provider("Giwrgos", "Katsos", "GiKat", "1234Secure");
        p2 = new Provider("Anastasios", "Gkogkas", "AnastGk", "1234Secure");
        p3 = new Provider("Stelios", "Bairamhs", "StelBar", "Secure1234");
        a1 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a2 = new Accommodation("Test2", "Ξενοδοχείο", "Test2", "57400", "An even better place.",
                "Thessaloniki", new HashMap<String, String>());
        a3 = new Accommodation("Test3", "Ξενοδοχείο", "Test3", "57400", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a4 = new Accommodation("Test4", "Ξενοδοχείο", "Test4", "55131", "Inconvenient place.",
                "Thessaloniki", new HashMap<>());
        a5 = new Accommodation("Test5", "Ξενοδοχείο", "Test5", "55131", "Inconvenient place.",
                "Thessaloniki", new HashMap<>());

        c1 = new Customer("Stelios", "Bairamhs", "StBair", "123456");
        c2 = new Customer("Anastasios", "Gkogkas", "AnastGk", "654321");

        p1.addAccommodation(a1);
        p2.addAccommodation(a2);
        p2.addAccommodation(a3);

        Provider.getListOfProviders().clear();
        Provider.getListOfProviders().add(p1);
        Provider.getListOfProviders().add(p2);
        Provider.getListOfProviders().add(p3);
    }

    @Test
    public void testGetMyAccommodations() {
        // Test Size
        Assert.assertEquals(1, p1.getMyAccommodations().size());
        Assert.assertEquals(2, p2.getMyAccommodations().size());

        // Test Content
        ArrayList<Accommodation> accsToTest = new ArrayList<>();
        accsToTest.add(a1);

        Assert.assertEquals(accsToTest, p1.getMyAccommodations());

        accsToTest.clear();
        accsToTest.add(a2);
        accsToTest.add(a3);

        Assert.assertEquals(accsToTest, p2.getMyAccommodations());

        p2.removeAccommodation(1);
        Assert.assertNotEquals(accsToTest, p2.getMyAccommodations());
    }

    @Test
    public void testRandomAcc() {
        ArrayList<Accommodation> toTest = new ArrayList<>();
        toTest.add(a1);
        toTest.add(a2);
        toTest.add(a3);
        toTest.add(a4);
        toTest.add(a5);
        Assert.assertNotEquals(toTest, Provider.randomAcc());
    }

    @Test
    public void testCountAverageReviews() {
        Assert.assertEquals(0, p1.countAverageReviews(), 0.01);
        Assert.assertEquals(0, p1.countReviews());

        r1 = new Review(4.5, "Test", a3, c1);
        r2 = new Review(1.5, "Test2", a3, c2);

        a3.addReview(r1);
        a3.addReview(r2);

        Assert.assertEquals(3, p2.countAverageReviews(), 0.01);
        Assert.assertEquals(2, p2.countReviews());
    }

    @Test
    public void testProviderSize() {
        Assert.assertEquals(3, Provider.getListOfProviders().size());
    }

    @Test
    public void testSetDataForTable() {
        Object[][] data = new Object[p1.getMyAccommodations().size()][6];
        Accommodation temp;
        String[] tempArray;

        for (int i = 0; i < data.length; i++) {//Οσες είναι οι γραμμές του πίνακα
            temp = p1.getMyAccommodations().get(i);
            tempArray = new String[6];
            tempArray[0] = temp.getName();
            tempArray[1] = temp.getType();
            tempArray[2] = temp.getCity();
            tempArray[3] = temp.getRoad();
            tempArray[4] = temp.getPostalCode();
            tempArray[5] = temp.getAverage() == -1 ? "Καμία Αξιολόγηση." : temp.getAverage() + "";
            data[i] = tempArray;
        }

        Assert.assertEquals(data, p1.setDataForTable());
    }
}