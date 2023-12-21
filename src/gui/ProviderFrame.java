package gui;

import api.Accommodation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProviderFrame {
    private JButton[] arrayOfButtons = new JButton[9];
    private static JTextField[] arrayOfTextFields = new JTextField[4];
    private static JLabel[] arrayOfLabels = new JLabel[6];
    private static final String[] nameOfLabels = {"Όνομα καταλύματος", "Τύπος καταλύματος", "Διεύθυνση", "Πόλη","Ταχυδρομικός κώδικας", "Περιγραφή"};

    private static String[] typeComboBox = {"Ξενοδοχείο","Διαμέρισμα","Μεζονέτα"};
    private static JComboBox myComboBox = new JComboBox(typeComboBox);

    private static JTextArea area;

    private static JFrame currentFrame;

    private static JPanel myPanel;

    private static ArrayList<ArrayList<Boolean>> selectedBoolean;

    private static ArrayList<ArrayList<String>> selected;

    public static String[] getNamesOfButtons() {
        return namesOfButtons;
    }

    private static final String[] namesOfButtons = {"Θέα","Μπάνιο","Πλύσιμο ρούχων","Ψυχαγωγία","Θέρμανση και κλιματισμός","Διαδίκτυο","Κουζίνα και τραπεζαρία","Εξωτερικός χώρος","Χώρος στάθμευσης"};

    private static final HashMap<Integer,String[]> options = new HashMap<>();

    private static final String[] views = {"Θέα σε πισίνα","Θέα σε παραλία","Θέα στη θάλασσα","Θέα στο λιμάνι","Θέα στο βουνό","Θέα στον δρόμο"};
    private static final String[] bath = {"Πιστολάκι μαλλιών"};
    private static final String[] wash = {"Πλυντήριο ρούχων","Στεγνωτήριο"};
    private static final String[] entertainment = {"Τηλεόραση"};
    private static final String[] AC = {"Εσωτερικό τζάκι","κλιματισμός","κεντρική θέρμανση"};
    private static final String[] internet = {"wifi", "ethernet"};
    private static final String[] dining_room = {"Κουζίνα","Ψυγείο","Φούρνος μικροκυμάτων","Μαγειρικά είδη","Πιάτα και μαχαιροπίρουνα","Πλυντήριο πιάτων","Καφετιέρα"};
    private static final String[] inner_room = {"Μπαλκόνι","αυλή"};
    private static final String[] parking = {"Δωρεάν χώρος στάθμευσης στην ιδιοκτησία","Δωρεάν πάρκινγκ στο δρόμο"};
    public ProviderFrame(JFrame currentFrame) {
        this.currentFrame = currentFrame;

        options.put(0,views); //Δημιουργεία του HashMap που περιέχει όλες τις δυνατές επιλογές ενος Accommodation.
        options.put(1,bath);
        options.put(2,wash);
        options.put(3,entertainment);
        options.put(4,AC);
        options.put(5,internet);
        options.put(6,dining_room);
        options.put(7,inner_room);
        options.put(8,parking);

        for(int i=0;i<6;i++) { //Δημιουργία των JLabels
            arrayOfLabels[i] = new JLabel(nameOfLabels[i]);
        }
    }

    private void ShowErrorDialog(String msg) {
        JOptionPane.showMessageDialog(null, msg,"", JOptionPane.ERROR_MESSAGE);
    }

    public HashMap<Integer,String[]> getOptions() {return options;}

    public static HashMap<String, String> createHashMap(ArrayList<ArrayList<String>> toArrays, String[] optionals) {
        HashMap<String,String> hashMapForSend = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            ArrayList<String> tempArrayList = toArrays.get(i);
            Collections.sort(tempArrayList);
            String temp = "";
            for(int j = 0; j < tempArrayList.size(); j++) {
                if (j == tempArrayList.size() - 1) {
                    temp += tempArrayList.get(j);
                }
                else {
                    temp += tempArrayList.get(j) + ", ";
                }
            }
            hashMapForSend.put(optionals[i], temp);
        }
        return hashMapForSend;
    }

    public void createAddFrame(api.Provider provider) {
        arrayOfTextFields = new JTextField[4];

        createPanel(null,arrayOfTextFields,myComboBox,false);

        int option = JOptionPane.showConfirmDialog(currentFrame, myPanel,"Δημιουργία",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if (option == 0) {
            try {
                if (arrayOfTextFields[0].getText().length() < 3 || arrayOfTextFields[0].getText().length() > 128) {
                    throw new Exception("Name of accommodation must be between 3 and 128 characters.");
                }
                if (arrayOfTextFields[1].getText().length() < 4 || arrayOfTextFields[1].getText().length() > 256) {
                    throw new Exception("Address of accommodation must be between 4 and 256 characters.");
                }
                if (arrayOfTextFields[2].getText().length() < 3 || arrayOfTextFields[2].getText().length() > 128) {
                    throw new Exception("City of accommodation must be between 3 and 128 characters.");
                }
                if (arrayOfTextFields[3].getText().length() < 4 || arrayOfTextFields[3].getText().length() > 8) {
                    throw new Exception("PostalCode of accommodation must be between 4 and 8 characters.");
                }
                if (Accommodation.findAccommodation(arrayOfTextFields[0].getText(), arrayOfTextFields[1].getText(), arrayOfTextFields[2].getText()) != null) {
                    throw new Exception("Similar Accommodation already exists! Check name, address and city!");
                }

                Accommodation accForCreation = new Accommodation(arrayOfTextFields[0].getText(), myComboBox.getSelectedItem() + "", arrayOfTextFields[1].getText(), arrayOfTextFields[3].getText(), area.getText(), arrayOfTextFields[2].getText(), createHashMap(selected, namesOfButtons));
                provider.addAccommodation(accForCreation);//Προστιθέται στην λίστα του provider και στην λίστα με όλα τα accommodations
                App.addRowToTable(accForCreation.getName(), accForCreation.getType(), accForCreation.getCity(), accForCreation.getRoad(),accForCreation.getPostalCode(), accForCreation.getAverage() == -1 ? "Καμία Αξιολόγηση." : accForCreation.getAverage() + "");
                accForCreation.setSelectedBoolean(selectedBoolean);
                accForCreation.setSelected(selected);
            } catch (Exception e) {
                ShowErrorDialog(e.getMessage());
            }
        }
    }

    public void createEditFrame(Accommodation acc)
    {
        arrayOfTextFields = new JTextField[4];

        createPanel(acc,arrayOfTextFields,myComboBox,false);//Δημιουργείται το παράθυρο

        arrayOfTextFields[0].setText(acc.getName());//Βάζω τι θα εχούν τα JTextFields.
        arrayOfTextFields[1].setText(acc.getRoad());
        arrayOfTextFields[2].setText(acc.getCity());
        arrayOfTextFields[3].setText(acc.getPostalCode());
        myComboBox.setSelectedItem(acc.getType());
        area.setText(acc.getDescription());

        int option=JOptionPane.showConfirmDialog(currentFrame, myPanel,"Επεξεργασία",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

        if (option == 0) {//Aν έχει πατήσει οκ!
            try {
                if (arrayOfTextFields[0].getText().length() < 3 || arrayOfTextFields[0].getText().length() > 128) {
                    throw new Exception("Name of accommodation must be between 3 and 128 characters.");
                }
                if (arrayOfTextFields[1].getText().length() < 4 || arrayOfTextFields[1].getText().length() > 256) {
                    throw new Exception("Address of accommodation must be between 4 and 256 characters.");
                }
                if (arrayOfTextFields[2].getText().length() < 3 || arrayOfTextFields[2].getText().length() > 128) {
                    throw new Exception("City of accommodation must be between 3 and 128 characters.");
                }
                if (arrayOfTextFields[3].getText().length() < 4 || arrayOfTextFields[3].getText().length() > 8) {
                    throw new Exception("PostalCode of accommodation must be between 4 and 8 characters.");
                }
                Accommodation temp = Accommodation.findAccommodation(arrayOfTextFields[0].getText(), arrayOfTextFields[1].getText(), arrayOfTextFields[2].getText());
                if (temp != null && !temp.equals(acc)) {
                    throw new Exception("Similar Accommodation already exists! Check name, address and city!");
                }
                acc.setName(arrayOfTextFields[0].getText());
                acc.setRoad(arrayOfTextFields[1].getText());
                acc.setCity(arrayOfTextFields[2].getText());
                acc.setPostalCode(arrayOfTextFields[3].getText());
                acc.setType(myComboBox.getSelectedItem()+"");
                acc.setDescription(area.getText());
                acc.setUtilities(createHashMap(selected,namesOfButtons));
            } catch (Exception e) {
                ShowErrorDialog(e.getMessage());
            }
                //Τροποποιώ το αντικείμενο!
        }
    }

    public JPanel createPanel(Accommodation acc,JTextField[] arrayOfTextFields,JComboBox myComboBox,boolean search)
    {
        for(int i=0;i<4;i++)
        {
            arrayOfTextFields[i] = new JTextField();
        }

        TitledBorder border = BorderFactory.createTitledBorder("");

        area = new JTextArea();

        myPanel = new JPanel();

        myPanel.setLayout(new GridLayout(0,1));
        myPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        myPanel.add(arrayOfLabels[0]);
        myPanel.add(arrayOfTextFields[0]);
        myPanel.add(arrayOfLabels[1]);
        myPanel.add(myComboBox);
        myPanel.add(arrayOfLabels[2]);
        myPanel.add(arrayOfTextFields[1]);
        myPanel.add(arrayOfLabels[3]);
        myPanel.add(arrayOfTextFields[2]);
        myPanel.add(arrayOfLabels[4]);
        myPanel.add(arrayOfTextFields[3]);

        if(!search)
        {
            myPanel.add(arrayOfLabels[5]);
            myPanel.add(area);
        }

        myPanel.setBorder(border);

        for(int i=0;i<9;i++)
        {
            arrayOfButtons[i] = new JButton(namesOfButtons[i]);
            myPanel.add(arrayOfButtons[i]);//Προσθήκη κουμπιών στο panel.
        }

        if(acc==null)//Για την δημιουργεία αντικειμένου
        {
            selected = new ArrayList<>();
            selectedBoolean = new ArrayList<>();

            if(search)
            {
                SearchTab.setSelected(selected);
                SearchTab.setSelectedBoolean(selectedBoolean);
            }

            for(int i=0;i<9;i++)
            {
                selected.add(new ArrayList<>());
                selectedBoolean.add(new ArrayList<>());
                arrayOfButtons[i].addActionListener(new MyActionListener(options.get(i),selected.get(i),selectedBoolean.get(i), true));
            }
        }
        else//Επεξεργασία αντικειμένου
        {
            selected = acc.getSelected();
            selectedBoolean = acc.getSelectedBoolean();
            for(int i=0;i<9;i++)
            {
                arrayOfButtons[i].addActionListener(new MyActionListener(options.get(i),selected.get(i),selectedBoolean.get(i),false));
            }
        }
        return myPanel;
    }
}
