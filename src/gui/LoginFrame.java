package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

public class LoginFrame {
    private JFrame frame = new JFrame();

    private JTextField name = new JTextField("");
    private JLabel labelname = new JLabel("Όνομα χρήστη");
    private JPasswordField password = new JPasswordField("");
    private JLabel labelpassword = new JLabel("Κωδικός Πρόσβασης");

    private JPanel panel = new JPanel();
    private JPanel panel2 = new JPanel();

    private JButton myButton = new JButton("Είσοδος");

    private JButton myButton2 = new JButton("Εγγραφή");

    private User connectedUser;

    private static int timesConnected = 0;

    public LoginFrame() {
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        panel.setBorder(BorderFactory.createTitledBorder(""));
        panel.setLayout(new GridLayout(2,2,2,2));

        panel.add(labelname);
        panel.add(name);

        panel.add(labelpassword);
        panel.add(password);

        panel2.add(myButton);
        panel2.add(myButton2);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.PAGE_END);

       if(timesConnected==0)
       {
            FileHandler.deserializationOfUsers();
            timesConnected++;
        }
        // Login Button onClick Handler.
        myButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get username input and check length.
                    String usr = name.getText();
                    if (usr.length() == 0) {
                        throw new Exception("Username cannot be empty!");
                    }
                    if (usr.length() < 3 || usr.length() > 16) {
                        throw new Exception("Username must be between 3 and 16 characters.");
                    }

                    // Check if username matches the pattern (a-Z & 0-9)
                    if (!Pattern.matches("[a-zA-Z0-9_]*", usr)) {
                        throw new Exception("Username is not eligible, please check the format (english alphabet & numbers ONLY).");
                    }

                    // Get password input and check length.
                    String pwd = String.valueOf(password.getPassword());
                    if (pwd.length() == 0) {
                        throw new Exception("Password cannot be empty!");
                    }
                    if (pwd.length() < 6 || pwd.length() > 32) {
                        throw new Exception("Password must be between 6 and 32 characters.");
                    }

                    // No regex should be used on passwords to allow for stronger passwords to be created.

                    if (!User.authenticateUser(usr, pwd)) {
                        throw new Exception("Username doesn't exist or password is not correlated to that username.");
                    }

                    connectedUser = User.findUserForLogin(usr,pwd);
                    frame.dispose();

                    new App(connectedUser,frame);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, exc.getMessage(),"", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        myButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RegistryFrame a = new RegistryFrame(frame);
            }
        });

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                FileHandler.serializeUsers();
                System.exit(0);
            }
        });
    }
    public static void main(String[] args)
    {
        LoginFrame a = new LoginFrame();
    }
}