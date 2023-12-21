package gui;

import api.Registration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MyDocumentListener implements DocumentListener
{
    private JTextField textField;
    private JLabel label;

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        if(textField.getName().equals("Username"))
        {
            if(!Registration.checkUserName(textField.getText()))
            {
                label.setText("Invalid Input!");
            }
            else
            {
                label.setText("");
            }
        }
        else
        {
            if(!Registration.matchesPattern(textField.getText()))
            {
                label.setText("Invalid Input!");
            }
            else
            {
                label.setText("");
            }
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        insertUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public MyDocumentListener(JTextField textField,JLabel label)
    {
        this.textField=textField;
        this.label=label;
    }
}
