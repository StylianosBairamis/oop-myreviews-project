package gui;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class MyItemListener implements ItemListener
{
    private ArrayList<String> selected;
    private int index;
    private ArrayList<Boolean> selectedBoolean;

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        if(e.getStateChange() == ItemEvent.SELECTED)
        {
            if(!selected.contains(checkBox.getText()))
            {
                selected.add(checkBox.getText());
            }

            selectedBoolean.set(index,true);
        }
        else
        {
            selected.remove(checkBox.getText());
            selectedBoolean.set(index,false);
        }
    }

    public MyItemListener(ArrayList<String> selected, int index, ArrayList<Boolean> selectedBoolean)
    {
        this.selected=selected;
        this.selectedBoolean=selectedBoolean;
        this.index=index;
    }
}
