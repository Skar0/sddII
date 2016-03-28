package be.ac.umons.gui;

import be.ac.umons.bsp.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by mr_robot on 04-03-16.
 */
public class MainMenu extends JFrame {

    private String selectedFile = "";
    private Heuristic defaultHeuristic = new InOrderHeuristic();
    private Heuristic heuristic = defaultHeuristic;
    private JFrame frame = this;


    public MainMenu() {
        this.setTitle("Heuristic simulator 2016");
        ImageIcon img = new ImageIcon("assets/img/eye.png");
        this.setIconImage(img.getImage());
        this.setSize(250, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.NONE;
        c.ipady = 0;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20,0,0,0);
        JLabel chooseFileLabel = new JLabel("Choose a file : ");
        panel.add(chooseFileLabel,c);

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(20,0,0,0);
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")+ System.getProperty("file.separator")+"Documents"));
        JButton chooseFile = new JButton("Open file");
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    selectedFile = file.getAbsolutePath();
                    System.out.println("Selected file: " + file.getAbsolutePath());
                }
            }
        });
        panel.add(chooseFile,c);

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(20,0,0,0);
        JLabel chooseHeuristicLabel = new JLabel("Choose a heuristic : ");
        panel.add(chooseHeuristicLabel,c);

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(20,0,0,0);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem(defaultHeuristic);
        comboBox.addItem(new RandomHeuristic());
        comboBox.addItem(new FreeSplitsHeuristic());
        comboBox.setSelectedItem(defaultHeuristic);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                heuristic = (Heuristic) cb.getSelectedItem();
            }
        });
        panel.add(comboBox,c);

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 2;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(30,0,0,0);
        JButton createTree = new JButton("View scene");
        createTree.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                if(selectedFile != "" && heuristic != null) {
                    frame.setResizable(true);
                    frame.setContentPane(  new SegmentsPainter(heuristic, selectedFile, frame));
                    frame.revalidate();
                    frame.repaint();
                   // PovChooser povChooser= new PovChooser(heuristic.createTree(loader.loadAsList()));
                  //  frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
                else {
                   JOptionPane.showMessageDialog(frame, "Please fill in all the fields", "Warning",JOptionPane.WARNING_MESSAGE);
               }
            }
        });
        panel.add(createTree,c);

        this.setContentPane(panel);
        this.setVisible(true);

        /*

        to try resize */

        frame.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e)
        {
            frame.repaint();
        }
             });

    }
}
