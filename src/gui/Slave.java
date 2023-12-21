package gui;
import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Slave {
    private static JLabel myLabel = new JLabel("Βαθμολογία καταλύματος:");
    private static JLabel myLabel2 = new JLabel("Δώσε την αξιολόγηση σου:");

    private static final Font font = new Font("Courier", Font.BOLD,12);
    private static SpinnerModel model = new SpinnerNumberModel(1,1,5,0.1);
    private static JSpinner mySpinner = new JSpinner(model);
    private static JTextField tex= new JTextField();

    private static JButton myButton = new JButton("Υποβολή");

    private static  JPanel myPanel = new JPanel();

    protected static JInternalFrame internalFrame;

    private static Accommodation currentAccommodation;

    private JButton buttonForInternal = new JButton("Εισαγωγή αξιολόγησης");

    private static JTextArea area;

    public Slave(JFrame frame, Accommodation acc, User connectedUser) {
        currentAccommodation = acc;
        myPanel.setLayout(new GridLayout(4,1));

        internalFrame = new JInternalFrame();
        internalFrame.setClosable(true);
        internalFrame.setLayout(new BorderLayout());
        internalFrame.setTitle(acc.getName());

        area = new JTextArea(acc.toString());
        area.setEditable(false);
        area.setFont(font);

        internalFrame.add(area,BorderLayout.CENTER);
        createContentOfFrame();
        if (connectedUser instanceof Customer) {
            internalFrame.add(buttonForInternal,BorderLayout.PAGE_END);

            buttonForInternal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    createContent(acc,connectedUser,"",0,0);
                    //((Customer) connectedUser).getMyReviews().get(selected)
                }
            });
        }

        internalFrame.pack();
        frame.add(internalFrame);
        internalFrame.setLocation(781,21);//Στο 780 σταματαεί το JTabbedPane αρα 1 pixel πιο δεξία. Το κάθε Tab έχει 19 pixels ύψος, επιλέγω 21 για να είναι στο ίδιο ύψος τελικα.

        internalFrame.setVisible(true);
        internalFrame.setResizable(false);
    }

    public static void createContent(Accommodation acc, User connectedUser, String message, int service, int selectedRow)
    {
        JPanel temp = new JPanel();
        mySpinner = new JSpinner(model);
        temp.setLayout(new GridLayout(2,2));
        temp.add(myLabel);
        temp.add(mySpinner);
        temp.add(myLabel2);
        mySpinner.setSize(50,50);

        if(service == 1)
        {
           Double value =((Customer) connectedUser).getMyReviews().get(selectedRow).getRating();//Παίρνω την τιμή για το mySpinner
           mySpinner.setValue(value);
        }

        String input = JOptionPane.showInputDialog(null,temp,message);

        if (input == null) {
            return;
        }

        try {
            if (input.length() < 3 || input.length() > 1024) {
                throw new Exception("Review content must be between 3 and 1024 characters.");
            }

            if (service == 0) { // Αν το service==0 τοτέ εισάγουμε καινουργια αξιολόγηση, αλλιώ επεξεργαζόμαστε κάποια.
                ((Customer) connectedUser).reviewAdd((Double)mySpinner.getValue(), input, acc, (Customer)connectedUser);
                App.addRowToTable(acc.getName(),acc.getType(),acc.getCity(),acc.getRoad(),acc.getPostalCode(),mySpinner.getValue()+"");

            } else
            {
                ((Customer) connectedUser).setReviewContent(selectedRow, input);
                ((Customer) connectedUser).setRating(selectedRow, (Double)mySpinner.getValue());
            }

            for (int i = 0; i < App.getAccItems().size(); i++) {
                if (App.getAccItems().get(i).getAccommodationInfo().equals(acc)) {
                    App.getAccItems().get(i).setReviewLabel(acc.getAverage() + "");
                }
            }

            updateTextArea();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected static void updateTextArea() {
        area.setText(currentAccommodation.toString());
        internalFrame.pack();
    }

    private void createContentOfFrame() {
        myPanel.setLayout(new GridLayout(2,2));
        myPanel.add(myLabel);
        myPanel.add(mySpinner);
        myPanel.add(myLabel2);
        mySpinner.setSize(50,50);
        area.setFont(font);
    }
}
