package org.example.nezet;

import org.example.model.GuiFestoModel;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GUIForm {
    private JComboBox cmbElemek;
    private JTextField txtElem;
    private JButton btnFelvesz;
    private JList lista;
    private JCheckBox chbMozgat;
    private JPanel pnlMain;
    private JButton btnMasolas;

    private JFrame frame;
    private JMenuItem Ment, Betolt;


    public GUIForm() {
        ini();
        btnFelvesz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String elem = "Ismeretlen (Nike)";
                if(!txtElem.getText().isBlank()){
                    elem = txtElem.getText();
                }
                cmbElemek.addItem(elem);
                txtElem.setText("");
            }
        });


        cmbElemek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String elem = (String)cmbElemek.getSelectedItem();
                int i = cmbElemek.getSelectedIndex();

                if(i > 0){
                    DefaultListModel<String> lm = (DefaultListModel<String>) lista.getModel();
                    lm.addElement(elem);

                    if(chbMozgat.isSelected()){
                        cmbElemek.removeItem(elem);
                        cmbElemek.setSelectedIndex(0);
                    }
                }
            }
        });

        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2){
                    String elem = (String) lista.getSelectedValue();
                    DefaultListModel dlm = (DefaultListModel) lista.getModel();
                    dlm.removeElement(elem);
                }
            }
        });

        Ment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(new File(System.getProperty("szobrok.txt")));
                if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                    File fajl = jfc.getSelectedFile();
                    try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fajl))){

                        GuiFestoModel model = new GuiFestoModel();

                        model.setChbMozgat(chbMozgat.isSelected());

                        List<String> cl = new ArrayList<>();
                        for (int i = 0; i < cmbElemek.getItemCount(); i++) {
                            cl.add((String) cmbElemek.getItemAt(i));
                        }

                        model.setComboSzovegek(cl);

                        List<String> ll = new ArrayList<>();
                        ListModel lm = lista.getModel();
                        for (int i = 0; i < lm.getSize(); i++) {
                            ll.add((String) lm.getElementAt(i));
                        }

                        model.setListSzovegek(ll);

                        oos.writeObject(model);
                    }catch (FileNotFoundException ex){
                        System.err.println("mentés: Nincs meg a fájl: " + ex.getMessage());
                        ex.printStackTrace();
                    }catch (IOException ex){
                        System.err.println("mentés: I/O hiba: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });

        Betolt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(new File(System.getProperty("szoborok.txt")));
                if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File fajl = jfc.getSelectedFile();
                    try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fajl))){

                        GuiFestoModel model = (GuiFestoModel) ois.readObject();
                        chbMozgat.setSelected(model.isChbMozgat());

                        DefaultComboBoxModel<String> dlm = (DefaultComboBoxModel<String>) cmbElemek.getModel();
                        for (String s : model.getComboSzovegek()) {
                            dlm.addElement(s);
                        }

                    }catch (FileNotFoundException ex){
                        System.err.println("olvasás: Nincs meg a fájl: " + ex.getMessage());
                        ex.printStackTrace();
                    }catch (IOException | ClassNotFoundException ex){
                        System.err.println("olvasás: I/O hiba: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void ini() {
        frame = new JFrame("ComboLista");
        frame.setContentPane(pnlMain);
        frame.setSize(520, 240);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        DefaultListModel<String> dlm = new DefaultListModel<>();
        lista.setModel(dlm);

        Ment = new JMenuItem("Mentés");
        Betolt = new JMenuItem("Betölt");
        JMenu mnuPrg = new JMenu("ProgramMenü");
        mnuPrg.add(Ment);
        mnuPrg.add(Betolt);
        mnuPrg.add(new JSeparator());
        mnuPrg.add(new JMenuItem("Kilépés"));
        JMenuBar mnuBar = new JMenuBar();
        mnuBar.add(mnuPrg);
        frame.setJMenuBar(mnuBar);
        frame.pack();

    }

    public static void main(String[] args) {
        new GUIForm();
    }
}
