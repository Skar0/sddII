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
        this.setTitle("BSP creator");
        this.setSize(300, 400);
       //this.setMinimumSize(new Dimension(300,400));
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        Box mainBox = Box.createVerticalBox();

        JLabel title = new JLabel("Main menu");
        mainBox.add(title);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
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
        mainBox.add(chooseFile);

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
        mainBox.add(comboBox);

        JButton createTree = new JButton("Create view");
        createTree.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                if(selectedFile != "" && heuristic != null) {
                    SegmentLoader loader = new SegmentLoader(selectedFile);

                    frame.setContentPane(  new SegmentsPainter(heuristic.createTree(loader.loadAsList())));
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
        mainBox.add(createTree);

        panel.add(mainBox);
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
