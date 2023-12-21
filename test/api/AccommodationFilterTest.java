package api;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AccommodationFilterTest {
    private Provider p1;

    private Accommodation a1,a2;

    private static final String[] methods = {"Θέα","Μπάνιο","Πλύσιμο ρούχων","Ψυχαγωγία","Θέρμανση και κλιματισμός","Διαδίκτυο","Κουζίνα και τραπεζαρία","Εξωτερικός χώρος","Χώρος στάθμευσης"};;

    @Before
    public void init() {
        p1 = new Provider("Giwrgos", "Katsos", "GiKat", "1234Secure");
        a1 = new Accommodation("Test1", "Ξενοδοχείο", "Test1", "55131", "A good place.",
                "Thessaloniki", new HashMap<String, String>());
        a2 = new Accommodation("Test2", "Ξενοδοχείο", "Test2", "57400", "An even better place.",
                "Thessaloniki", new HashMap<String, String>());

        Accommodation.setAccommodations(new ArrayList<>());
        p1.addAccommodation(a1);
        p1.addAccommodation(a2);
    }

    @Test
    public void filterAccommodation() {
        Integer[] ints = {0,-999,-999,-999};
        String[] vals = {"Test2", "", "", ""};

        ArrayList<Accommodation> accommodationsToTest = new ArrayList<>();
        accommodationsToTest.add(a2);
        assertEquals(accommodationsToTest, AccommodationFilter.filterAccommodation(vals, ints));
    }

    @Test
    public void checkAccommodationsForOptionals() {
        HashMap<String,String> optionals = new HashMap<>();
        for (int i = 0; i < methods.length; i++) {
            optionals.put(methods[i], "");
        }

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

        optionals.put("Θέα", t2);
        a2.setUtilities(optionals);

        ArrayList<Accommodation> accsToTest = new ArrayList<>();
        accsToTest.add(a2);
        assertEquals(accsToTest, AccommodationFilter.checkAccommodationsForOptionals(optionals, Accommodation.getAccommodations()));
    }
}