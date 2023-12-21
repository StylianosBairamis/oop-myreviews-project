package gui;

import api.Accommodation;
import api.AccommodationFilter;
import api.Provider;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchTab
{
    private static JTextField[] arrayOfTextFields = new JTextField[4];
    private static String[] typeComboBox = {"Ξενοδοχείο","Διαμέρισμα","Μεζονέτα"};

    private static JComboBox myComboBox = new JComboBox(typeComboBox);

    public static void setSelectedBoolean(ArrayList<ArrayList<Boolean>> selectedBoolean) {
        SearchTab.selectedBoolean = selectedBoolean;
    }

    private static ArrayList<ArrayList<Boolean>> selectedBoolean;

    public static void setSelected(ArrayList<ArrayList<String>> selected) {
        SearchTab.selected = selected;
    }

    private static ArrayList<ArrayList<String>> selected;

    private static JPanel panelCreated;

    public SearchTab()
    {

    }

    public static ArrayList<Accommodation> createSearchTab(ProviderFrame providerFrame, JFrame currentFrame)
    {
        String type = "";
        panelCreated = providerFrame.createPanel(null,arrayOfTextFields,myComboBox,true);
        int option=JOptionPane.showConfirmDialog(currentFrame, panelCreated," ",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        ArrayList<Accommodation> accToShow = new ArrayList<>();
        ArrayList<Accommodation> tempAcc;
        if(option == 0)//Αν έχει πατήσει οκ
        {
            type = String.valueOf(myComboBox.getSelectedItem());
            Integer[] notEmpty = new Integer[4];
            String[] optionsInserted = new String[4];
            boolean allTextFieldsEmpty = true;

            for(int i=0;i<4;i++)//Ελέγχουμε τα 4 JTextFields
            {
                if(arrayOfTextFields[i].getText().equals(""))//Ελέγχω αν έχει επιλέξει κάποιο JTextField
                {
                    notEmpty[i]=-999;
                }
                else
                {
                    notEmpty[i]=i;
                    optionsInserted[i]=arrayOfTextFields[i].getText();
                    allTextFieldsEmpty=false;
                }
            }

            if(!allTextFieldsEmpty)
            {
                accToShow = AccommodationFilter.filterAccommodation(optionsInserted,notEmpty);//παίρνω πίσω τα filtered acc
            }

            boolean hasOptionalsForCheck = false;

            for (int i = 0; i < selectedBoolean.size(); i++)
            {
                if (selectedBoolean.get(i).contains(true))//Αν έχω κάνει check σέ κάποιο optional
                {
                    hasOptionalsForCheck = true;
                    break;
                }
            }

            if ((!hasOptionalsForCheck) && allTextFieldsEmpty)//Αν δεν έχω κάτι για έλεγχω
            {
                return Accommodation.getAccommodations();
            }

            if(hasOptionalsForCheck)
            {
                HashMap<String,String> optionalsToCheck = ProviderFrame.createHashMap(selected,ProviderFrame.getNamesOfButtons());
                accToShow = AccommodationFilter.checkAccommodationsForOptionals(optionalsToCheck,accToShow);
            }

            tempAcc = new ArrayList<>();

            for (int i = 0; i < accToShow.size(); i++) {
                if (!accToShow.get(i).getType().equals(type)) {
                    continue;
                }
                tempAcc.add(accToShow.get(i));
            }
           return accToShow.size() == tempAcc.size() ? accToShow : tempAcc;
        }
        else
        {
            return App.getAccToShow();
        }
    }
}
