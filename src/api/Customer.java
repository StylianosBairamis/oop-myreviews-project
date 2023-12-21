package api;

import gui.App;
import gui.Slave;
import javax.swing.JTable;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Κλάση που αναπαριστά έναν Πελάτη στην εφαρμογή, αποτελεί υποκλάση του {@link api.User} και υποσκελίζει τις αφηρημένες
 * μεθόδους: {@link User#setDataForTable()}, {@link api.User#editAction(int,JTable)},
 * {@link api.User#deleteAction(int, JTable)}.
 * Μπορεί να καταχωρήσει αξιολογήσεις σε {@link api.Accommodation}, να τις επεξεργαστεί καθώς και να τις διαγράψει.
 */

public class Customer extends User implements Serializable
{
    /**
     * ArrayList που περιέχει τα Reviews που έχει υποβάλει ο συγκεκριμένος Customer.
     */

    private ArrayList<Review> myReviews = new ArrayList<>();

    /**
     * ArrayList που περιέχει όλους τους Customers της εφαρμογής,
     * γεμίζει από την μέθοδο {@link FileHandler#deserializationOfUsers()} κάθε φορά που ανοίγει η εφαρμογή.
     */

    private static ArrayList<Customer> listOfCustomers = new ArrayList<>();

    public Customer(String name,String surname,String Username,String Password)
    {
        super(name,surname,Username,Password);
    }

    /**
     * Μέθοδος που γεμίζει με δεδομένα τον πίνακα που θα υπάρχει στο DashBoard.
     *
     * @return Επιστρέφει ένα 2D Array που κάθε γραμή του περιέχει δεδομένα του κάθε Review που έχει καταχωρήσει
     * ο συγκεκριμένος Customer.
     */

    @Override
    public Object[][] setDataForTable() { //Μέθοδος για τα data του πίνακα.
        Object[][] data = new Object[this.getMyReviews().size()][6];
        Accommodation temp;
        String[] tempArray;
        for (int i = 0; i < data.length; i++) {//Οσες είναι οι γραμμές του πίνακα
            tempArray = new String[6];
            temp = this.getMyReviews().get(i).getReviewedAcc();
            tempArray[0] = temp.getName();
            tempArray[1] = temp.getType();
            tempArray[2] = temp.getCity();
            tempArray[3] = temp.getRoad();
            tempArray[4] = temp.getPostalCode();
            tempArray[5] = this.getMyReviews().get(i).getRating() == -1 ? "Καμία Αξιολόγηση." : ((double)Math.round(this.getMyReviews().get(i).getRating() * 100) / 100) + "";
            data[i] = tempArray;
        }
        App.setIndexForClose(3);

        return data;
    }

    /**
     * Μέθοδος που ορίζει τι πρέπει να γίνεται κάθε φορά που ο χρήστης πατάει το κουμπί για την
     * επεξεργασία μιας αξιολόγηγης.
     *
     * @param rowSelected Γραμμή του πίνακα που υπάρχει στο DashBoard προς επεξεργασία.
     * @param table Ο πίνακας που περιέχει τα δεδομένα ώστε να τα τροποποιηθούν μετά την επεξεργασία.
     */

    @Override
    public void editAction(int rowSelected,JTable table)
    {
        rowSelected=table.getSelectedRow();
        Accommodation acc=Accommodation.findAccommodation((String)table.getValueAt(rowSelected,0),(String)table.getValueAt(rowSelected,3),(String)table.getValueAt(rowSelected,2));
        Slave.createContent(acc,this,this.getMyReviews().get(rowSelected).getReviewContent(),1,rowSelected);
        acc.setAverage(acc.calculateAverage());
        table.setValueAt(this.getMyReviews().get(rowSelected).getRating(), rowSelected, 5);
        App.getAccItemByData(acc).setReviewLabel(acc.getAverage() + "");
    }

    /**
     * Μέθοδος που ορίζει τι πρέπει να γίνεται κάθε φορά που ο χρήστης πατάει το κουμπί για την
     * διαγραφή μιας αξιολόγηγης.
     *
     * @param selectedRow Γραμμή του πίνακα που υπάρχει στο DashBoard προς διαγραφή.
     * @param table Ο πίνακας που περιέχει τα δεδομένα ώστε να τα αφαιρεθούν μετά την διαγραφή.
     */

    @Override
    public void deleteAction(int selectedRow,JTable table)
    {
        api.Review tempReview = this.getMyReviews().get(selectedRow);

        tempReview.getReviewedAcc().removeReview(tempReview);//Aφαιρώ απο το accommodation το review
        tempReview.getReviewedAcc().setAverage(tempReview.getReviewedAcc().calculateAverage());
        App.getAccItemByData(tempReview.getReviewedAcc()).setReviewLabel(tempReview.getReviewedAcc().getAverage() + "");
        this.removeReview(tempReview);//Αφαιρώ review απο τον Customer
    }

    /**
     * Μέθοδος για την προσθήκη μιας αξιολόγηγης, δημιουργείται ένα Review με βάση τις παραμέτρους και στην συνέχεια
     * εισάγεται στο {@link api.Customer#myReviews} και στην λίστα με τα reviews του Accommodation.
     *
     * @param rating Βαθμός της αξιολόγησης που έχει δώσει ο συγκεκριμένος Customer
     * @param content Περιεχόμενο της αξιολόγησης (free text)
     * @param acc Accommodation που έχει αξιολογηθεί.
     * @param customer Customer που αξιολογεί.
     */

    public void reviewAdd(double rating, String content, Accommodation acc, Customer customer) {
        Review r = new Review(rating, content, acc, customer);
        acc.addReview(r);
        myReviews.add(r);
    }

    public void setReviewContent(int selectedRow, String input) {
        getMyReviews().get(selectedRow).setReviewContent(input);
    }

    public void setRating(int selectedRow, double value)
    {
        getMyReviews().get(selectedRow).setRating(value);
    }

    /**
     * Μέθοδος που χρησιμοποιείται για τον υπολογισμό του μέσου όρου των αξιολογήσεων που έχει δώσει ο Customer
     * @return Επιστρέφεται ο μέσος όρος των αξιολογήσεων με ακρίβεια 2 δεκαδικών ψηφίων.
     */

    public double countAverageReviews() {
        if (myReviews.size() == 0) {
            return 0;
        }

        double avg = 0;
        for (int i = 0; i < myReviews.size(); i++) {
            avg += myReviews.get(i).getRating();
        }

        avg /= myReviews.size();

        return (double)Math.round(avg * 100) / 100;
    }

    public static ArrayList<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public ArrayList<Review> getMyReviews() {return myReviews;}

    /**
     * Μέθοδος για την διαγραφή μιας αξιολόγησης απο τις αξιολογήσεις που έχει καταχωρήσει ο Customer
     * @param reviewForDelete Αξιολόγηση προς διαγραφή.
     */
    public void removeReview(Review reviewForDelete)
    {
        myReviews.remove(reviewForDelete);
    }

}
