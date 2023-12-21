package api;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    /**
     * Μέθοδος που κάνει serialize την λίστα που περιέχει όλους τους Customer και την γράφει στο αρχείο: "Customers.txt".
     * Κάνει επίσης serialize την λίστα που περιέχει όλους τους Provider και την γράφει στο αρχείο: "Providers.txt".
     * Καλείται όταν κλείνει το LoginFrame, και όταν εγγράφεται ένας νέος User.
     */

    public static void serializeUsers()
    {
        ArrayList<Customer> listOfCustomers = new ArrayList<>();
        ArrayList<Provider> listOfProviders = new ArrayList<>();

        for(int i=0;i<User.getAllUsers().size();i++)
        {
            if( User.getAllUsers().get(i) instanceof Provider)
            {
                listOfProviders.add((Provider)User.getAllUsers().get(i));
            }
            else
            {
                listOfCustomers.add((Customer)User.getAllUsers().get(i));
            }
        }

        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Providers.txt")))
        {
            outputStream.flush();
            outputStream.writeObject(listOfProviders);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Customers.txt")))
        {
            outputStream.flush();
            outputStream.writeObject(listOfCustomers);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Μέθοδος που κάνει deserialize την λίστα που περιέχει όλους τους Customer από το αρχείο: "Customers.txt".
     * Κάνει επίσης deserialize την λίστα που περιέχει όλους τους Provider από το αρχείο: "Providers.txt".
     *
     * Στην συνέχεια προσθέτει τους providers στην λίστα με όλους τους providers της {@link api.Provider},
     * προσθέτει τους Customers στην λίστα με όλους τους Customers της {@link api.Customer}.
     *
     * Τέλος, αρχικοποιείται η λίστα με όλα τα Accommodation της εφαρμογής.
     */

    public static void deserializationOfUsers()
    {

        ArrayList<Provider> tempList = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Providers.txt"))) {
            tempList = ((ArrayList<Provider>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Customer> tempList2 = null;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Customers.txt"))) {
            tempList2 = ((ArrayList<Customer>) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < tempList.size(); i++) {
            User.getAllUsers().add(tempList.get(i));
            User.addUser(tempList.get(i).getUsername(), tempList.get(i).getPassword());
        }

        for (int i = 0; i < tempList2.size(); i++) {
            User.getAllUsers().add(tempList2.get(i));
            User.addUser(tempList2.get(i).getUsername(), tempList2.get(i).getPassword());
        }

        Provider.getListOfProviders().addAll(tempList);//Τα γεμίζω απο με τα deserialized
        Customer.getListOfCustomers().addAll(tempList2);

        ArrayList<Accommodation> allAccommodations = new ArrayList<>();

        for (int i = 0; i < Provider.getListOfProviders().size(); i++) {
            allAccommodations.addAll(Provider.getListOfProviders().get(i).getMyAccommodations());//Βάζω όλα τα Accommodation των Provider
        }

        for (int i = 0; i < allAccommodations.size(); i++) {
            allAccommodations.get(i).getReviews().clear();//Διαγράφω τα Reviews που είχαν για να βάλω αυτά των Customer
        }
        //Λόγω προβλήματος με τις θέσης μνήμης που δημιουργήθηκε με serialization.

        Customer temp;
        Review temp2;
        for (int i = 0; i < Customer.getListOfCustomers().size(); i++)
        {
            temp = Customer.getListOfCustomers().get(i);
            for (int j = 0; j < temp.getMyReviews().size(); j++)
            {
                temp2 = temp.getMyReviews().get(j);
                if(allAccommodations.contains(temp2.getReviewedAcc()))
                {
                    int index = allAccommodations.indexOf(temp2.getReviewedAcc());
                    allAccommodations.get(index).addReview(temp2);//Αρχικοποίηση των Reviews στα Accommodations
                    temp2.setReviewedAcc(allAccommodations.get(index));
                }
            }
        }

        Accommodation.getAccommodations().addAll(allAccommodations);//Αρχικοποίηση της λίστας με όλα τα Accommodations
    }
}
