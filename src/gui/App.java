package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import api.*;

public class App {
    private static JPanel searchPanel = new JPanel();

     private static api.User userOfApp ;

     private JPanel p2 = new JPanel();

     private static JTable table;

     private static int rowSelected=-999;

     private static JButton addButton;

     private static JButton editButton;

     private static JButton deleteButton;

     private static JButton showButton;

     private static JPanel panelForButtons = new JPanel();

     private static DefaultTableModel tableModel;

     private static JFrame AppFrame;

     private static ArrayList<Slave> frames = new ArrayList<>();

     private static int indexForClose;

     private static ProviderFrame providerFrame;

     private static JTabbedPane tp;

     private static ArrayList<AccommodationItem> accItems = new ArrayList<AccommodationItem>();

    public static ArrayList<Accommodation> getAccToShow() {
        return accToShow;
    }

    private static ArrayList<Accommodation> accToShow = new ArrayList<>(Accommodation.getAccommodations());

     public static void setIndexForClose(int indexForClose1)
     {
         indexForClose=indexForClose1;
     }

     public static AccommodationItem getAccItemByData(Accommodation a) {
         for (int i = 0; i < accItems.size(); i++) {
             if (a.equals(accItems.get(i).getAccommodationInfo())) {
                 return accItems.get(i);
             }
         }
         return null;
     }

     public static ArrayList<AccommodationItem> getAccItems() {
         return accItems;
     }

    // App Index
    public App(User connectedUser, JFrame loginFrame) {
        // Frame
        userOfApp=connectedUser;

        accToShow = Accommodation.getAccommodations();

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        showButton = new JButton("Show Statistics");

        AppFrame = new JFrame();
        AppFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        AppFrame.setTitle("Καταλύματα App");

        // Tabbed Pane
        tp = new JTabbedPane();

        createTab(tp,connectedUser);

        tp.add("Dashboard", p2);
        createDashBoard(p2);

        // Έξοδος
        JPanel p3 = new JPanel();
        tp.add("Έξοδος", p3);

        tp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(tp.getSelectedIndex()==indexForClose)
                {
                    AppFrame.dispose();
                    loginFrame.setVisible(true);
                }
                else if(tp.getSelectedIndex()==1)
                {
                    ProviderFrame providerFrame = new ProviderFrame(AppFrame);
                    accToShow = SearchTab.createSearchTab(providerFrame,AppFrame);
                    createTab(tp, connectedUser);
                }
            }
        });
        AppFrame.setSize(1300,800);
        AppFrame.setResizable(false);
        AppFrame.setLayout(null);
        AppFrame.add(tp);
        tp.setSize(780,450);//780 είναι το αθροίσμα των μεγεθών των column του JTable;
        AppFrame.setVisible(true);
    }

    protected static void createDashBoard(JPanel panelToFill) {
        String columnHeaders[] = {"'Ονομα","Τύπος","Πόλη","Διεύθυνση","Ταχυδρομικός Κώδικας","Βαθμός Αξιολόγησης"};
        Object[][] data = userOfApp.setDataForTable();//Φέρνω τα data!
        tableModel = new DefaultTableModel(data, columnHeaders) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        setSizeOfColumns();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)//Για να εμφανίζεται το JInternalFrame
            {
                rowSelected=table.getSelectedRow();
                Accommodation acc=Accommodation.findAccommodation((String)table.getValueAt(rowSelected,0),(String)table.getValueAt(rowSelected,3), (String)table.getValueAt(rowSelected, 2));
                if(frames.size()!=0)
                {
                    frames.get(0).internalFrame.dispose();
                    frames.clear();
                }
                frames.add(new Slave(AppFrame,acc,userOfApp));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panelToFill.setLayout(new BorderLayout());
        panelToFill.add(scrollPane,BorderLayout.CENTER);

        panelForButtons = new JPanel();
        if (userOfApp instanceof Provider) {
            providerFrame = new ProviderFrame(AppFrame);

            panelForButtons.setLayout(new GridLayout(1,4));
            panelForButtons.add(addButton);
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    providerFrame.createAddFrame((Provider)userOfApp);
                }
            });
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(rowSelected!=-999)
                    {
                        Accommodation temp=Accommodation.findAccommodation(table.getValueAt(rowSelected,0)+"",table.getValueAt(rowSelected,3)+"",(String)table.getValueAt(rowSelected,2));//Βρίσκω το acc με βάση το όνομα και την διευθυνσή
                        providerFrame.createEditFrame(temp);
                        table.setValueAt(temp.getName(),rowSelected,0);//Ενημέρωση του JTable για να μπορώ να το βρω με την findAccommodation
                        table.setValueAt(temp.getType(),rowSelected,1);
                        table.setValueAt(temp.getCity(),rowSelected, 2
                        );
                        table.setValueAt(temp.getRoad(),rowSelected,3);
                        table.setValueAt(temp.getAverage() == -1 ? "Καμία αξιολόγηση." : temp.getAverage() + "",rowSelected,5);
                        rowSelected=-999;
                        Slave.updateTextArea();
                    }
                }
            });

            showButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Μέσος όρος αξιολογήσεων: " + userOfApp.countAverageReviews() + "\n"
                    + "Πλήθος αξιολογήσεων σε όλα τα κατάλυματα σας: " + ((Provider)userOfApp).countReviews());
                }
            });
        } else {
            panelForButtons.setLayout(new GridLayout(1,3));

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(rowSelected!=-999)
                    {
                        userOfApp.editAction(rowSelected,table);
                        rowSelected=-999;
                        Slave.updateTextArea();
                    }
                }
            });
            showButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Μέσος όρος αξιολογήσεων: " + userOfApp.countAverageReviews());
                }
            });
        }

        panelForButtons.add(editButton);
        panelForButtons.add(deleteButton);
        panelForButtons.add(showButton);

        panelToFill.add(panelForButtons,BorderLayout.PAGE_END);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(rowSelected!=-999)
                {
                    userOfApp.deleteAction(rowSelected,table);
                    tableModel.removeRow(rowSelected);//Αφαιρώ απο το table to review
                    rowSelected=-999;
                    Slave.updateTextArea();
                } //Αν rowSelected==-999 δεν έχει κάνει κάπου κλικ ο χρήστης
            }
        });
    }

    protected static void addRowToTable(String name,String type,String city,String address,String postalCode,String rating) {
        tableModel.addRow(new String[]{name, type, city, address, postalCode, rating});
    }

    public void createTab(JTabbedPane tabbedPane, User connectedUser) {
        if (connectedUser instanceof Customer) {
            JPanel p1 = new JPanel();

            AccommodationItem temp;

            accItems = new ArrayList<>();
            GridLayout accommodationLayout = new GridLayout(1,2);
            if (accToShow.size() == Accommodation.getAccommodations().size()) {
                accToShow = Provider.randomAcc();
            }

            for (int i = 0; i < accToShow.size(); i++) {
                double review_avg = accToShow.get(i).calculateAverage();

                String t = review_avg == -1 ? "Καμία Αξιολόγηση." : String.valueOf(review_avg);
                temp = new AccommodationItem(accToShow.get(i), accToShow.get(i).getName(), t);
                AccommodationItem finalTemp = temp;
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() instanceof AccommodationItem) {
                            AccommodationItem t = (AccommodationItem) e.getSource();
                            if(frames.size()!=0)
                            {
                                frames.get(0).internalFrame.dispose();
                                frames.clear();
                            }
                            frames.add(new Slave(AppFrame, finalTemp.getAccommodationInfo(),userOfApp));
                        }
                    }
                });

                temp.setLayout(accommodationLayout);
                accItems.add(temp);
                p1.add(temp);
            }

            GridLayout accommodationsLayout = new GridLayout(10,1);
            p1.setLayout(accommodationsLayout);
            if (tabbedPane.getTabCount() == 0) {
                tabbedPane.add("Καταλύματα", p1);
                tabbedPane.add("Αναζήτηση", searchPanel);
            } else if (tabbedPane.getTabCount() > 2) {
                tabbedPane.remove(0);
                tabbedPane.insertTab("Καταλύματα", null, p1, null, 0);
            }
        }
    }

    private static void setSizeOfColumns() {
        TableColumnModel columnModel = table.getColumnModel();
        for(int i=0;i<table.getColumnCount();i++)
        {
            columnModel.getColumn(i).setPreferredWidth(130);
            columnModel.getColumn(i).setMaxWidth(130);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}