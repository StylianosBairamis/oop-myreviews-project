package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class MyActionListener implements ActionListener
{
    private final String[] options;
    private final ArrayList<String> selected;
    private ArrayList<Boolean> selectedBoolean;

    public MyActionListener(String[] options, ArrayList<String> selected, ArrayList<Boolean> selectedBoolean,boolean Add)
    {
        this.options=options;
        this.selected=selected;
        this.selectedBoolean=selectedBoolean;

        if(Add)
        {
            for(int i=0;i<options.length;i++)
            {
                selectedBoolean.add(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JCheckBox[] arrayOfCheckBoxes =new JCheckBox[options.length];
        JPanel myPanel2 = new JPanel();
        myPanel2.setLayout(new BoxLayout(myPanel2,BoxLayout.Y_AXIS));
        for(int i=0;i<options.length;i++)
        {
            arrayOfCheckBoxes[i] = new JCheckBox(options[i]);

            arrayOfCheckBoxes[i].addItemListener(new MyItemListener(selected,i,selectedBoolean));

            if(selectedBoolean.get(i))
            {
                arrayOfCheckBoxes[i].setSelected(true);
            }
            myPanel2.add(arrayOfCheckBoxes[i]);
        }
        int option=JOptionPane.showConfirmDialog(null,myPanel2,"", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}