package api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccommodationTest {
    private Provider p1,p2;

    private Customer c1,c2;
    private Accommodation a1,a2,a3;

    private static ArrayList<ArrayList<String>> selected;
    private static final String[] methods = {"Θέα","Μπάνιο","Πλύσιμο ρούχων","Ψυχαγωγία","Θέρμανση και κλιματισμός","Διαδίκτυο","Κουζίνα και τραπεζαρία","Εξωτερικός χώρος","Χώρος στάθμευσης"};;

    @Before
    public void init() {
        p1 = new Provider("Giwrgos", "Katsos", "GiKat", "1234Secure");
        p2 = new Provider("Nikos", "Terros", "NikTer", "Secure1234");
        a1 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a2 = new Accommodation("Test2", "Ξενοδοχείο", "Test2", "57400", "An even better place.",
                "Thessaloniki", new HashMap<String, String>());
        a3 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "57400", "A good place.",
                "Thessaloniki", new HashMap<String, String>());

        Accommodation.setAccommodations(new ArrayList<>());
        p1.addAccommodation(a1);
        p2.addAccommodation(a2);

        c1 = new Customer("Stelios", "Bairamhs", "StBair", "123456");
        c2 = new Customer("Anastasios", "Gkogkas", "AnastGk", "654321");

        selected = new ArrayList<>();
    }

    @Test
    public void testSetAndGetName() {
        a1.setName("New Name");
        assertEquals(a1.getName(), "New Name");
    }

    @Test
    public void testSetAndGetType() {
        a1.setType("Διαμέρισμα");
        assertEquals(a1.getType(), "Διαμέρισμα");
    }

    @Test
    public void testSetAndGetAddress() {
        a1.setRoad("Αγγελάκη 37");
        assertEquals(a1.getRoad(), "Αγγελάκη 37");
    }

    @Test
    public void testSetAndGetDescription() {
        a1.setDescription("New Description");
        assertEquals(a1.getDescription(), "New Description");
    }

    @Test
    public void testSetAndGetPostalCode() {
        a1.setPostalCode("New P.C");
        assertEquals(a1.getPostalCode(), "New P.C");
    }

    @Test
    public void testSetAndGetAverage() {
        a1.setAverage(1.82);
        assertEquals(a1.getAverage(), 1.82, 0.001);
    }

    @Test
    public void testFindAccommodation() {
        // Θετικά Test
        Accommodation temp = Accommodation.findAccommodation("Test1", "Test1", "Thessaloniki");
        Assert.assertEquals(temp, a1);

        Accommodation temp2 = Accommodation.findAccommodation("Test2", "Test2", "Thessaloniki");
        Assert.assertEquals(temp2, a2);

        // Αρνητικά Test
        temp = Accommodation.findAccommodation("Test2", "Test1", "Thessaloniki");
        Assert.assertNull("Accommodation1 not found", temp);

        temp2 = Accommodation.findAccommodation("Test2", "Test1", "Thessaloniki");
        Assert.assertNull("Accommodation2 not found", temp2);
    }

    @Test
    public void testSetAndGetUtilities() {
        String[] text = new String[]{"Θέα σε πισίνα", "θέα στη θάλασσα"};

        ArrayList<String> t = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            t.add(text[i]);
        }

        Collections.sort(t);
        String t2 = "";

        for (int i = 0; i < t.size(); i++) {
            t2 += t.get(i);
            if (i != t.size() - 1) {
                t2 += ", ";
            }
        }

        HashMap<String, String> testUtilities = new HashMap<>();
        for (int i = 0; i < methods.length; i++) {
            testUtilities.put(methods[i], "");
        }

        testUtilities.put("Θέα", t2);

        // Check Accommodation.setSelected()
        ArrayList<ArrayList<String>> sel = new ArrayList<>();
        sel.add(new ArrayList<>(Arrays.asList(text)));
        a1.setSelected(sel);
        assertEquals(a1.getSelected(), sel);

        // Check Accommodation.setUtilities() and Accommodation.getUtilities()
        a1.setUtilities(testUtilities);
        assertEquals(a1.getUtilities(), testUtilities);

        Assert.assertTrue(a1.hasOptionals());
    }

    @Test
    public void testAddGetAndRemoveReview() {
        Review r1 = new Review(4.0, "Good place.", a1, c1);
        a1.addReview(r1);
        assertEquals(a1.getReviews().size(), 1);

        assertEquals(a1.getReviews().get(a1.getReviews().indexOf(r1)), r1);

        a1.removeReview(r1);
        assertEquals(a1.getReviews().size(), 0);

        Review r2 = new Review(3.5, "Good place", a1, c1);
        Review r3 = new Review(4.75, "Very good place", a1, c2);
        a1.addReview(r2);
        a1.addReview(r3);

        assertEquals(a1.calculateAverage(), 4.12, 0.01);
    }

    @Test
    public void testAccommodationEquals() {
        // Συμμετρική
        assertNotEquals(a1, a2);
        assertNotEquals(a2, a1);

        // Ανακλαστική
        assertEquals(a1, a1);

        // Μεταβατική
        assertEquals(a1, a3);
        assertNotEquals(a1, a2);
        assertNotEquals(a2, a3);
    }

    @Test
    public void testPrintable() {
        String testStr = "Name of the accommodation is: Test1\n" +
                "The type of the accommodation is: Ξενοδοχείο\n" +
                "The City of the accommodation is: Thessaloniki\n" +
                "The road of the accommodation is: Test1\n" +
                "The postalCode of the accommodation is: 55131\n" +
                "A small description for the accommodation: A good place.\n" +
                "The rating of the accommodations is: No reviews.\n" +
                "The accommodation has 0 reviews.\n";
        assertEquals(a1.toString(), testStr);
    }

    @Test
    public void testGetAccommodations() {
        assertEquals(2, Accommodation.getAccommodations().size());

        assertEquals(a1, Accommodation.getAccommodations().get(Accommodation.getAccommodations().indexOf(a1)));
        assertEquals(a2, Accommodation.getAccommodations().get(Accommodation.getAccommodations().indexOf(a2)));
    }

    @Test
    public void testSetAndGetSelectedBooleans() {
        ArrayList<ArrayList<Boolean>> bools = new ArrayList<>();
        ArrayList<Boolean> tempBools = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tempBools.clear();
            for (int j = 0; j < 2; j++) {
                tempBools.add(true);
            }
            bools.add(tempBools);
        }

        a1.setSelectedBoolean(bools);
        assertEquals(bools, a1.getSelectedBoolean());
    }
}