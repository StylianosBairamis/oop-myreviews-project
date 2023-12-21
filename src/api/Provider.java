package api;

import gui.App;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Κλάση που αναπαριστά έναν Πελάτη στην εφαρμογή, αποτελεί υποκλάση του {@link api.User} και υποσκελίζει τις αφηρημένες
 * μεθόδους: {@link User#setDataForTable()}, {@link api.User#editAction(int,JTable)},
 * {@link api.User#deleteAction(int, JTable)}.
 * Μπορεί να καταχωρήσει {@link api.Accommodation}, να τa επεξεργαστεί καθώς και να τa διαγράψει.
 */
public class Provider extends User implements Serializable
{

    /**
     * Λίστα που περιέχει όλους του παρόχους της εφαρμογής.
     */

    private static ArrayList<Provider> listOfProviders = new ArrayList<>();

    public ArrayList<Accommodation> getMyAccommodations() {
        return myAccommodations;
    }

    /**
     * Λίστα που περιέχει καταλύματα κάθε παρόχου.
     */

    private ArrayList<Accommodation> myAccommodations = new ArrayList<>();

    public Provider(String name, String surname, String username, String password) {
        super(name, surname, username, password);
    }

    public static ArrayList<Provider> getListOfProviders() {
        return listOfProviders;
    }

    /**
     * Μέθοδος για την προσθήκη ενός καταλύματος στην εφαρμογή. Το κατάλυμα προστίθεται τόσο στην λίστα του παρόχου
     * όσο στην λίστα όλων των καταλυμάτων που υπάρχουν.
     *
     * @param acc Το κατάλυμα που θα προστεθεί.
     */

    public void addAccommodation(Accommodation acc)
    {
       myAccommodations.add(acc);
       Accommodation.getAccommodations().add(acc);
    }

    /**
     * Μέθοδος για την αφαίρεση ενός καταλύματος απο την εφαρμογή. Το κατάλυμα αφαιρείται τόσο απο την λίστα του παρόχου
     * όσο και απο την λίστα όλων των καταλυμάτων που υπάρχουν.
     *
     * @param index Ποίο κατάλυμα θα αφαιρεθεί.
     */

    public void removeAccommodation(int index)
    {
        Accommodation tempAcc = myAccommodations.get(index);
        Review tempReview;

        for(int i=0;i<tempAcc.getReviews().size();i++) {
            tempReview=tempAcc.getReviews().get(i);
            tempReview.getCustomer().removeReview(tempReview);//Aφαιρώ τα Review απο τον Customer που τα έκανε!
        }
        Accommodation.getAccommodations().remove(tempAcc);//Αφαίρεση απο την λίστα που περιέχει ολα τα Accommodations
        myAccommodations.remove(tempAcc);
    }

    /**
     * Μέθοδος που γεμίζει με δεδομένα τον πίνακα που θα υπάρχει στο DashBoard.
     * @return Επιστρέφει ένα 2D Array που κάθε γραμή του περιέχει δεδομένα για κάθε Accommodation που έχει καταχωρήσει
     * συγκερκιμένος Provider.
     */

    @Override
    public Object[][] setDataForTable() {//set data για τον πίνακα
        Object[][] data = new Object[myAccommodations.size()][6];

        Accommodation temp;
        String[] tempArray;
        for (int i = 0; i < data.length; i++) {//Οσες είναι οι γραμμές του πίνακα!
            tempArray = new String[6];
            temp = myAccommodations.get(i);
            tempArray[0] = temp.getName();
            tempArray[1] = temp.getType();
            tempArray[2] = temp.getCity();
            tempArray[3] = temp.getRoad();
            tempArray[4] = temp.getPostalCode();
            tempArray[5] = temp.getAverage() == -1 ? "Καμία Αξιολόγηση." : temp.getAverage() + "";
            data[i] = tempArray;
        }
        App.setIndexForClose(1);

        return data;
    }

    /**
     * Μέθοδος που διαλέγει έως και 4 τυχαία καταλύματα απο το σύνολο των καταλυματων και τα επιστρέφει.
     *
     * @return Τα τυχαία επιλεγμένα καταλύματα.
     */

    public static ArrayList<Accommodation> randomAcc() {
        ArrayList<Integer> selected_accs = new ArrayList<Integer>();
        int l = Accommodation.getAccommodations().size();
        int r = Math.min(4, l);

        Random rand = new Random();
        int i_r = rand.nextInt(l);

        ArrayList<Accommodation> toReturn = new ArrayList<Accommodation>();
        for (int i = 0; i < r; i++) {
            while (selected_accs.contains(i_r)) {
                i_r = rand.nextInt(l);
            }
            selected_accs.add(i_r);
            toReturn.add(Accommodation.getAccommodations().get(i_r));
        }

        return toReturn;
    }

    /**
     * Μέθοδος που υπολογίζει τον συνολικό αριθμό των Reviews που έχουν δεχτεί τα καταλύματα του.
     *
     * @return int τιμή που είναι ο συνολικός αριθμός των Reviews των καταλυμάτων του.
     */

    public int countReviews() {
        int c = 0;
        for (int i = 0; i < myAccommodations.size(); i++) {
            c += myAccommodations.get(i).getReviews().size();
        }
        return c;
    }

    /**
     * Μέθοδος που υπολογίζει τον συνολικό μέσο όρο των αξιολογήσεων των καταλυμάτων. Αν δεν έχει καταλύματα ή
     * τα καταλύματα του δεν έχουν δέχτει αξιολογήσεις τότε επιστρέφεται 0.
     *
     * @return double τιμή με ακρίβεια 2 δεκαδικών ψηφείων.
     */

    public double countAverageReviews() {
        if (myAccommodations.size() == 0) {
            return 0;
        }

        double avg = 0;
        int counter = 0;
        for (int i = 0; i < myAccommodations.size(); i++) {
            if (myAccommodations.get(i).getAverage() == -1) {
                counter++;
                continue;
            }
            avg += myAccommodations.get(i).getAverage();
        }

        if (myAccommodations.size() == counter) {
            return 0;
        }
        avg /= (myAccommodations.size() - counter);
        return (double)Math.round(avg * 100) / 100;
    }

    /**
     * Μέθοδος που ορίζει τι πρέπει να γίνεται κάθε φορά που ο χρήστης πατάει το κουμπί για την
     * επεξεργασία μιας αξιολόγηγης.
     */

    @Override
    public void editAction(int selectedRow, JTable table)
    {

    }

    /**
     * Μέθοδος που ορίζει τι πρέπει να γίνεται κάθε φορά που ο πάροχος πατάει το κουμπί για την
     * διαγραφή ενος καταλύματος.
     *
     * @param selectedRow Γραμμή του πίνακα που υπάρχει στο DashBoard προς διαγραφή.
     * @param table Ο πίνακας που περιέχει τα δεδομένα ώστε να τα αφαιρεθούν μετά την διαγραφή.
     */

    @Override
    public void deleteAction(int selectedRow, JTable table)
    {
        removeAccommodation(selectedRow);
    }
}
