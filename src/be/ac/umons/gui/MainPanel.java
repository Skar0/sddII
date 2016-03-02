package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by clement on 2/28/16.
 */
public class MainPanel extends JPanel {

    public void paintComponent(Graphics g){
        g.fillOval(20, 20, 75, 75);
        g.drawString("Tiens ! Le Site du Zéro !", 10, 20);

        GridLayout grid = new GridLayout(3,2);
        grid.setHgap(20);
        grid.setVgap(20);
        this.setLayout(grid);

        JButton button1 = new JButton("push");
        this.add(button1);
        JButton button2 = new JButton("push");
        this.add(button2);
        JButton button3 = new JButton("push");
        this.add(button3);
    }

    public void paintersAlgorithm(BSPNode root, double[] pov) {
        if (root.isLeaf()) {
           drawSegments(root.getSegmentsInLine());
        }
        else if (getPovPosition(pov).isInfinite()) {
            paintersAlgorithm(root.getLeftSon(), pov);
            drawSegments(root.getSegmentsInLine());
            paintersAlgorithm(root.getRightSon(), pov);
        }
        else if (getPovPosition(pov).isNaN()) {
            paintersAlgorithm(root.getRightSon(), pov);
            drawSegments(root.getSegmentsInLine());
            paintersAlgorithm(root.getLeftSon(), pov);
        }
        else {
            paintersAlgorithm(root.getRightSon(), pov);
            paintersAlgorithm(root.getLeftSon(), pov);
        }
    }

    private Double getPovPosition(double[] pov) {
        return 5.0;
    }

    public void drawSegments(List<Segment> segments) {}

}
