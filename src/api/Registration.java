package api;

import java.util.regex.Pattern;

/**
 * Βοηθητική Κλάση που χρησιμοποιείται για τον έλεγχο της εισόδου απο των JTextFields που υπάρχουν στο RegistryFrame.
 */

public class Registration
{
    /**
     * Μέθοδος που χρησιμοποιεί Regular Expression, ώστε το input στο JTextField να αποτελείται μόνο απο πεζούς ή
     * κεφαλαίους λατινικούς χαρακτήρες, αριθμούς και τον ειδικό χαρακτήρα '_'.
     *
     * @param input Το υποψήφιο όνομα του χρήστη
     *
     * @return boolean τιμή αν το input είναι έγκυρο ή οχι.
     */
    public static boolean matchesPattern(String input)// Χρησιμοποιείται για firstName,LastName
    {
        return Pattern.matches("[a-zA-Z0-9_]*",input);
    }

    /**
     * Μέθοδος που χρησιμοποιεί Regular Expression, ώστε τα όνομα του User να περιέχει μόνο πεζούς ή κεφαλαίους
     * λατινικούς χαρακτήρες, αριθμούς και τον ειδικό χαρακτήρα '_'. Τέλος ελέγχει αν δεν υπάρχει το όνομα ήδη.

     * @param userName Το υποψήφιο όνομα του χρήστη
     *
     * @return boolean τιμή αν το username είναι έγκυρο ή οχι.
     */

    public static boolean checkUserName(String userName)//Χρησιμοποιείται για το userName
    {
        return Pattern.matches("[a-zA-Z0-9_]*",userName) && !User.checkIfUserAlreadyExists(userName);
    }
}
