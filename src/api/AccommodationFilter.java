package api;

import java.util.ArrayList;
import java.util.HashMap;

public class AccommodationFilter {
//    public AccommodationFilter() {}

    /**
     *
     * @param optionsSelected
     * @param notEmpty
     * @return Επιστρέφει λίστα απο Accommodation που έχουν φιλτραριστεί με βάση τις δυο παραμέτρους
     */

    public static ArrayList<Accommodation> filterAccommodation(String[] optionsSelected, Integer[] notEmpty) {
        ArrayList<Accommodation> temp;

        ArrayList<Accommodation> toReturn = new ArrayList<>(Accommodation.getAccommodations());

        for (int i = 0; i < notEmpty.length; i++)
        {
            if(notEmpty[i]==-999)
            {
                continue;
            }

            temp = new ArrayList<>();

            for(int j=0;j<toReturn.size();j++)
            {
                if (i == 0)
                {
                    if (toReturn.get(j).getName().equals(optionsSelected[i]))
                    {
                        temp.add(toReturn.get(j));
                    }
                }
                else if(i == 1)
                {
                    if (toReturn.get(j).getRoad().equals(optionsSelected[i]))
                    {
                        temp.add(toReturn.get(j));
                    }
                }
                else if (i == 2)
                {
                    if (toReturn.get(j).getCity().equals(optionsSelected[i]))
                    {
                        temp.add(toReturn.get(j));
                    }
                }
                else if(i == 3)
                {
                    if (toReturn.get(j).getPostalCode().equals(optionsSelected[i]))
                    {
                        temp.add(toReturn.get(j));
                    }
                }
            }
            toReturn.clear();
            toReturn.addAll(temp);
        }
        return toReturn;
    }

    public static ArrayList<Accommodation> checkAccommodationsForOptionals(HashMap<String,String> forCheck, ArrayList<Accommodation> toReturn)
    {
        ArrayList<Accommodation> temp = new ArrayList<>();
        boolean equals;
        for(int i=0;i<toReturn.size();i++) {
            equals = true;
            if(!toReturn.get(i).hasOptionals())//Αν το Accommodation δεν έχει optionals απορρίπτεται
            {
                continue;
            }
            for(String key:toReturn.get(i).getUtilities().keySet())//Ελέγχουμε αν τα HashMaps ταιριάζουν
            {
                if(!toReturn.get(i).getUtilities().get(key).equals(forCheck.get(key)))
                {
                    equals = false;
                    break;
                }
            }

            if (equals) {
                temp.add(toReturn.get(i));
            }
        }
        return temp;
    }
}
