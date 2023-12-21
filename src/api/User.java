package api;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
    * Η κλάση αναπαριστά έναν χρήστη με 2 υποκλάσεις, τον Customer {@link api.Customer}
    * και τον Provider {@link api.Provider}.
    * Η κλάση είναι εφοδιασμένη με πεδία τα οποία αντιστοιχούν στα στοιχεία ενός χρήστη, καθώς και με βοηθητικές μεθόδους
    * όπως setters, getters και μεθόδους για την προσθήκη χρηστών, αυθεντικοποίση χρηστών καθώς και αφηρημένες
    * μεθόδους για τις κλάσεις που την κληρονομούν (Customer & Provider).
 */
public abstract class User implements Serializable {

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String password;
    private String name;
    private String surname;

    private static HashMap<String, String> users = new HashMap<>();

    private static HashSet<String> temp_usernames = new HashSet<>();

    private static ArrayList<User> allUsers = new ArrayList<>(); // ArrayList me olous tous Users provider h customers

    /**
       * Constructor της User.
       * @param name Το πρώτο (μικρό) όνομα ενός χρήστη.
       * @param surname Το επίθετο ενός χρήστη.
       * @param username Το username ενός χρήστη.
       * @param password Ο κωδικός πρόσβασης ενός χρήστη.
     */
    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    /**
        * Μέθοδος που προσθέτει έναν χρήστη στην λίστα λιστών.
        * @param username Το username του χρήστη
        * @param password Ο κωδικός του χρήστη
     */
    public static void addUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            temp_usernames.add(username);
        }
    }

    /**
        * Μέθοδος που γεμίζει την λίστα με όλους τους χρήστες βάση ενός αρχείου.
     */
    public static void fillAllUsers()
    {
        String line="";

        try(BufferedReader myReader = new BufferedReader(new FileReader("Files/users.txt")))
        {
            while((line=myReader.readLine())!=null)
            {
                if(line.split(" ")[4].equals("Πάροχος"))
                {
                    allUsers.add(new Provider(line.split(" ")[2],line.split(" ")[3],line.split(" ")[0],line.split(" ")[1]));
                }
                else
                {
                    allUsers.add(new Customer(line.split(" ")[2],line.split(" ")[3],line.split(" ")[0],line.split(" ")[1]));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
        * Μέθοδος που ελέγχει αν ένας χρήστης υπάρχει ήδη (το username είναι πιασμένο).
        * @param username Το username που να ελεγχθεί.
        * @return Αν υπάρχει ήδη χρήστης με αυτό το username.
     */
    public static boolean checkIfUserAlreadyExists(String username) {
        return users.containsKey(username);
    }

    /**
        * Μέθοδος που αυθεντικοποιεί έναν χρήστη.
        * @param username Το username προς έλεγχο.
        * @param password Το password προς έλεγχο.
        * @return Αν υπάρχει χρήστης με αυτόν τον συνδιασμό ονόματος/επιθέτου.
     */
    public static boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    /**
        Μέθοδος που βρίσκει έναν συγκεκριμένο user βάση ενός συνδιασμού username: password.
        @param username Όνομα χρήστη προς έλεγχο.
        @param password Κωδικός χρήστη προς έλεγχο.
        @return Αντικέιμενο τύπου User αν υπάρχει User με τον συνδιασμό username: password
        ,αλλιώς null.
     */
    public static User findUserForLogin(String username, String password)
    {
        for(int i=0;i<allUsers.size();i++)
        {
            if(allUsers.get(i).getUsername().equals(username) && allUsers.get(i).getPassword().equals(password))
            {
                return allUsers.get(i);
            }
        }
        return null;
    }

    public static ArrayList<User> getAllUsers() {return allUsers;}

    public abstract Object[][] setDataForTable();

    public abstract void editAction(int selectedRow, JTable table);

    public abstract void deleteAction(int selectedRow,JTable table);

    public abstract double countAverageReviews();
}
