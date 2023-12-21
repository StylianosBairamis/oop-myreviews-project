package gui;

import api.Customer;
import api.FileHandler;
import api.Provider;
import api.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistryFrame extends JDialog {
    private String[] userOrProvider = {"Χρήστης", "Πάροχος"};
    private String[] namesOfLabels= {"Όνομα","Επίθετο","Όνομα χρήστη","Κωδικός","Τύπος Χρήστη"};
    private JLabel[] labels = new JLabel[5];
    private JTextField[] textFields = new JTextField[3];
    private JPasswordField passwordField = new JPasswordField();
    private JPanel myPanel = new JPanel();
    private JLabel[] feedBack = new JLabel[4];

    private JButton button = new JButton("Εγγραφη");

    private JComboBox myComboBox = new JComboBox(userOrProvider);

    public RegistryFrame(JFrame frame)
    {
        super(frame,"",true);

        GridLayout gridLayout = new GridLayout(5,3,2,2);
        TitledBorder border=BorderFactory.createTitledBorder("");

        myPanel.setBorder(border);
        myPanel.setLayout(gridLayout);
        createJLabels();

        add(myPanel,BorderLayout.CENTER);

        add(button,BorderLayout.PAGE_END);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    String firstname = textFields[0].getText();
                    if (firstname.length() == 0) {
                        throw new Exception("FirstName field cannot be blank.");
                    }

                    String lastname = textFields[1].getText();
                    if (lastname.length() == 0) {
                        throw new Exception("LastName field cannot be blank.");
                    }

                    String username = textFields[2].getText();
                    if (username.length() < 3 || username.length() > 16) {
                        throw new Exception("Username must be between 3 and 16 characters.");
                    }

                    String pwd = String.valueOf(passwordField.getPassword());
                    if (pwd.length() == 0) {
                        throw new Exception("Password field cannot be blank.");
                    }

                    if (username.length() < 3 || username.length() > 16) {
                        throw new Exception("Username must be between 3 and 16 characters.");
                    }

                    if (pwd.length() < 6 || pwd.length() > 32) {
                        throw new Exception("Password must be between 6 and 32 characters.");
                    }

                    if (firstname.length() < 3 || firstname.length() > 32) {
                        throw new Exception("First Name must be between 3 and 32 characters.");
                    }

                    if (lastname.length() < 3 || lastname.length() > 32) {
                        throw new Exception("Last Name must be between 3 and 32 characters.");
                    }

                    getJDialog().dispose();

                    if(myComboBox.getSelectedItem().equals("Χρήστης"))
                    {
                        User.getAllUsers().add(new Customer(firstname,lastname,username,pwd));
                        User.addUser(username,pwd);
                    }
                    else
                    {
                        User.getAllUsers().add(new Provider(firstname,lastname,username,pwd));
                        User.addUser(username,pwd);
                    }

                    FileHandler.serializeUsers();
                    //new App();
                } catch(Exception exc) {
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        myComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        pack();
        setLocation(frame.getX()+30,frame.getY()+30);
        setResizable(false);
        setVisible(true);
    }

    private JDialog getJDialog() {return this;}

    private void createJLabels()
    {
        for(int i=0;i<5;i++)
        {
            labels[i] = new JLabel(namesOfLabels[i]);//Δημιουργία και ονομασία των labels
            myPanel.add(labels[i]);//Για να μπεί και το τελευταίο JLabel!
            if(i<3)
            {
                feedBack[i] = new JLabel();
                textFields[i] = new JTextField();
                Document d =textFields[i].getDocument();
                d.addDocumentListener(new MyDocumentListener(textFields[i],feedBack[i]));
                myPanel.add(textFields[i]);//Πρώτα μπαίνει το JLabel, μετά το JTextField, μετά Το JLabel που αφορά τα σφάλματ
                myPanel.add(feedBack[i]);
                if(i==2)
                {
                    textFields[i].setName("Username");
                }
                else
                {
                    textFields[i].setName("DontCare");
                }
            }
            else if(i==3)
            {
                feedBack[i] = new JLabel();
                myPanel.add(passwordField);
                myPanel.add(feedBack[i]);
            }
        }
        myPanel.add(myComboBox);
    }
}
