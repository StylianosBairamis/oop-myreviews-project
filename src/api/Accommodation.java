package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Η Κλάση αναπαριστά ενα κατάλυμα. Κάθε κατάλυμα έχει πεδία που το αποτελούν τα στοιχεία του καθώς και κάποιες παροχές
 * {@link api.Accommodation#utilities}. Tα πεδία όνομα, πόλη και διεύθυνση προσδιορίζουν
 * μοναδικά ένα κατάλυμα. Επιπλέον το κατάλυμα μπορεί να δεχτεί αξιολογήσεις από έναν {@link api.Customer}.
 * Τέλος το κατάλυμα παρέχει μεθόδους για την εισαγώγη και διαγραφή αξιολόγησης για το κατάλυμα.
 */

public class Accommodation implements Serializable
{
    private String type;
    private String name;
    private String road;
    private String city;
    private String postalCode;
    private String description;
    private double average;
    private HashMap<String, String> utilities;
    private static ArrayList<Accommodation> accommodations = new ArrayList<>();

    private ArrayList<Review> reviews = new ArrayList<>(); // Reviews για το συγκεκριμένο accommodation

    /**
     * Αποτελεί λίστα λίστων με σκοπό την αποθήκευση των προαιρετικών παροχών του καταλύματος.
     * Είναι παράλληλη λίστα με την selectedBoolean.
     */

    private ArrayList<ArrayList<String>> selected = new ArrayList<>();

    public void setSelected(ArrayList<ArrayList<String>> selected) {
        this.selected.addAll(selected);
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<ArrayList<String>> getSelected() {
        return selected;
    }

    public ArrayList<ArrayList<Boolean>> getSelectedBoolean() {
        return selectedBoolean;
    }

    public void setSelectedBoolean(ArrayList<ArrayList<Boolean>> selectedBoolean) {
        this.selectedBoolean.addAll(selectedBoolean);
    }

    private ArrayList<ArrayList<Boolean>> selectedBoolean = new ArrayList<>();

    /**
     * @param name Όνομα του καταλύματος, το μήκος του ονόματος μπορεί να είναι απο 3 μέχρι 128 χαρατήρες.
     * Μπορεί να υπάρξουν δυο καταλύματα με ίδιο όνομα αρκεί να μην έχουν ίδια πόλη, διεύθυνση δηλωμένη.
     * @param type Τύπος καταλύματος, μπορεί να είναι ξενοδοχείο ή διαμέρισμα ή και μεζονέτα.
     * @param road Οδός που βρίσκεται το κατάλυμα.
     * @param postalCode Ταχυδρομικός κώδικας
     * @param description Μικρή περιγραφή του καταλύματος
     * @param city Πόλη που βρίσκεται το κατάλυμα.
     * @param optionals Οι παροχές του καταλύματος, αποτελούν ένα HashMap με key την επιλογή π.χ θεα ή ψυχαγωγία και
     * value τι παρέχει το κατάλυμα. Για παράδειγμα για key: Διαδίκτυο το κατάλυμα έχει value: wifi, ethernet.
     */

    public Accommodation(String name, String type, String road, String postalCode, String description, String city, HashMap<String, String> optionals) {
        this.name = name;
        this.type = type;
        this.road = road;
        this.postalCode = postalCode;
        this.description = description;
        this.utilities = optionals;
        this.city=city;
        this.average = -1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoad(String addr) {
        this.road = addr;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postal) {
        this.postalCode = postal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUtilities(HashMap util) {
        this.utilities = util;
    }

    public static boolean addAccommodation(String name, String type, String addr, String postalCode, String desc, String city, HashMap<String, String> utilities, String provider) {
        if (!accommodations.contains(new Accommodation(name, type, addr, postalCode, desc, city, utilities))) {
            accommodations.add(new Accommodation(name, type, addr, postalCode, desc, city, utilities));
            return true;
        }
        return false;
    }

    // Used for testing purposes
    public static void setAccommodations(ArrayList<Accommodation> accs) {
        accommodations = accs;
    }

    public static ArrayList<Accommodation> getAccommodations() {
        return accommodations;
    }

    /**
     * Μέθοδος που υπολογίζει το μέσο όρο των αξιολογήσεων που έχει δεχτεί το Accommodation.
     * Καλείται κάθε φορά που δέχεται ή αφαιρείται ένα Review απο το Accommodation.
     * @return double τιμή που αναπαριστά τον μ.ο. των αξιολογήσεων με ακρίβεια 2 δεκαδικών ψηφίων.
     */

    public double calculateAverage() {
        if (this.reviews.size() == 0) { return -1; }

        double sum = 0;
        for (int i = 0; i < this.reviews.size(); i++) {
            sum += this.reviews.get(i).getRating();
        }

        return (double)Math.round(sum / this.reviews.size() * 100) / 100;
    }

    /**
     * @return String aναλυτική περιγραφή του καταλύματος.
     */

    public String toString()
    {
        String a = "Name of the accommodation is: " + this.name + "\n";
        a += "The type of the accommodation is: " + this.type + "\n";
        a += "The City of the accommodation is: " + this.city + "\n";
        a += "The road of the accommodation is: " + this.road + "\n";
        a += "The postalCode of the accommodation is: " + this.postalCode + "\n";
        a += "A small description for the accommodation: " + this.description + "\n";
        a += "The rating of the accommodations is: " + (this.average == -1 ? "No reviews." : calculateAverage()) + "\n";

        for (String key : utilities.keySet()) {
            if (utilities.get(key).equals("")) {
                continue;
            }
            a += "The accommodation has " + key + " more details: \n ";
            int counter = 1;
            for (String v : utilities.get(key).split(",")) {
                a += "(" + counter + ") " + v + " ";
                if (counter % 3 == 0) {
                    a += "\n ";
                }
                counter++;
            }
            a += "\n";
        }

        a += "The accommodation has " + reviews.size() + " review" + (reviews.size() != 1 ? "s" : "")  + ".\n";

        for (int i = 0; i < reviews.size(); i++) {
            a += reviews.get(i);
        }

        return a;
    }

    public String getName() {
        return name;
    }

    public void setAverage(double a) {
        this.average = a;
    }

    public double getAverage() {
        return average;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * Προσθέτω το Review στο {@link api.Accommodation#reviews}, στην συνέχεια υπολογίζω ξανά
     * το μέσο όρο των Reviews που έχει δεχτεί το συγκεκριμένο Accommodation.
     *
     * @param rev Review που πρέπει να προστεθεί στις αξιολογήσεις του Accommodation.
     */

    public void addReview(Review rev) { // Μέθοδος που προσθέτει review στο Accommodation
        reviews.add(rev);
        this.average = calculateAverage();
    }

    /**
     *  Αφαιρώ το Review απο το {@link api.Accommodation#reviews}, στην συνέχεια υπολογίζω ξανά
     *  το μέσο όρο των Reviews που έχει δεχτεί το συγκεκριμένο Accommodation.
     *
     *  @param reviewForDelete Review που πρέπει να φύγει απο το Accommodation
     */

    public void removeReview(Review reviewForDelete) {
        reviews.remove(reviewForDelete);
        this.average = calculateAverage();
    }

    /**
     * Μέθοδος που ελέγχει αν υπάρχει κάποιο Accommodation καταχωρημένο με βάση το όνομα, οδό και πόλη.
     *
     * @param name Όνομα καταλύματος
     * @param road Διεύθυνση καταλύματος
     * @param city Πολή του καταλύματος
     * @return Επιστρέφει Accommodation με που έχει ως τιμές τις παραμέτρους της μεθόδου, αλλιώς αν δεν βρεθεί κάποιο
     * Accommodation με αυτές τις τιμές τότε επιστρέφει null.
     */

    public static Accommodation findAccommodation(String name,String road,String city) {
        for(int i = 0; i < accommodations.size(); i++) {
            if(accommodations.get(i).getRoad().equals(road) && accommodations.get(i).getName().equals(name) && accommodations.get(i).getCity().equals(city)) {
                return accommodations.get(i);
            }
        }
        return null;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getType() {
        return type;
    }

    public String getRoad()
    {
        return road;
    }

    public HashMap<String, String> getUtilities() {
        return utilities;
    }

    /**
     * @param obj κατάλυμα προς εξέταση.
     * Δυο Accommodation είναι ίδια αν-ν εχόυν τα πεδία όνομα, πόλη, και διεύθυνση ίσα.
     * @return boolean τιμή ανάλογα με το αν δυο καταλύματα είναι ίδια.
     */

    @Override
    public boolean equals(Object obj)
    {
        if(this==obj)
        {
            return true;
        }

        if(!(obj instanceof Accommodation)) {
            return false;
        }

        if(this.city.equals(((Accommodation) obj).city))
        {
            if(this.name.equals(((Accommodation) obj).getName()))
            {
                if(this.road.equals(((Accommodation) obj).getRoad()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Μέθοδος που ελέγχει αν ένα Accommodation έχει έστω και μία παροχή καταλύματος.
     * Διατρέχει το HashMap με τις παροχές, και ελέγχει αν υπάρχει μη κενή τιμή.
     * @return boolean τιμή ανάλογα με το αν έχει κάποια παροχή ή όχι.
     */

    public boolean hasOptionals()
    {
        for(String key:utilities.keySet())
        {
            if(!utilities.get(key).equals(""))
            {
                return true;
            }
        }
        return false;
    }
}