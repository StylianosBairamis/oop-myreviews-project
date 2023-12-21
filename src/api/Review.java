package api;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 *  Η κλάση αναπαριστά μια αξιολόγηση για ενα {@link api.Accommodation}. Κάθε αξιολόγηση έχει πεδία που αποτελούν
 *  τα στοιχεία της.
 */

public class Review implements Serializable
{
    private double rating;
    private String reviewContent;
    private String reviewer;
    private Date reviewDate;

    public void setReviewedAcc(Accommodation reviewedAcc) {
        this.reviewedAcc = reviewedAcc;
    }

    private Accommodation reviewedAcc;
    private Customer customer;

    public void setReviewContent(String reviewContent)
    {
        this.reviewContent = reviewContent;
    }

    public String getReviewContent()
    {
        return reviewContent;
    }

    public void setRating(double rating) {
        this.rating = (double)Math.round(rating * 100) / 100; // Για στρογγυλοποίηση 2 δεκαδικών ψηφίων
    }

   /**
     * @param rating Βαθμός της αξιολόγησης που έχει δώσει ο συγκεκριμένος Customer
     * @param content Περιεχόμενο της αξιολόγησης (free text)
     * @param acc Accommodation που έχει αξιολογηθεί.
     * @param customer Customer που αξιολογεί.
     */

    public Review(double rating, String content, Accommodation acc, Customer customer) {
        this.rating = (double)Math.round(rating * 100) / 100; // Για στρογγυλοποίηση 2 δεκαδικών ψηφίων
        this.reviewContent = content;
        this.reviewer = customer.getUsername();
        this.reviewDate = new Date();
        this.reviewedAcc = acc;
        this.customer = customer;
    }
    public Customer getCustomer() { return customer; }

    public double getRating() {
        return this.rating;
    }

    public Accommodation getReviewedAcc() {
        return reviewedAcc;
    }

    /**
     * @return Αναλυτική περιγραφή της αξιολόγησης.
     */

    public String toString() {
        String a = "The rating of the accommodation is: " + rating + "\n";
        a += "Review by: " + reviewer + "\n";
        a += "Review: " + reviewContent + "\n";
        a += "The review was uploaded at: " + reviewDate + "\n";
        return a;
    }

    /**
     * Δύο αξιολογήσης είναι ίδιες αν-ν τα πεδία reviewer και reviewDate των δύο αξιολογήσεων είναι ίσα.
     *
     * @param obj Αξιολόγηση προς έλεγχο.
     *
     * @return boolean τιμή για το αν δύο αξιολογήσεις είναι ίδιες.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Review)) {
            return false;
        }

        return reviewer.equals(((Review) obj).reviewer) && reviewDate.equals(((Review) obj).reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, reviewContent, reviewer, reviewDate, reviewedAcc, customer);
    }
}
