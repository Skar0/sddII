package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by clement on 2/28/16.
 */
public class PaintersAlgorithm extends JPanel {

    private Pov pov;
    private BSPNode rootOfTree;

    public PaintersAlgorithm(Pov pov, BSPNode rootOfTree) {
        this.pov = pov;
        this.rootOfTree = rootOfTree;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        paintersAlgorithm(rootOfTree, g2);
    }

    public void paintersAlgorithm(BSPNode root, Graphics2D g2 ) {
        if (root.isLeaf()) {
           drawSegments(root.getSegmentsInLine(), g2);
        }
        else if (getPovPosition(root).isInfinite()) {
            paintersAlgorithm(root.getLeftSon(), g2);
            drawSegments(root.getSegmentsInLine(), g2);
            paintersAlgorithm(root.getRightSon(), g2);
        }
        else if (getPovPosition(root).isNaN()) {
            paintersAlgorithm(root.getRightSon(), g2);
            drawSegments(root.getSegmentsInLine(), g2);
            paintersAlgorithm(root.getLeftSon(), g2);
        }
        else {
            paintersAlgorithm(root.getRightSon(), g2);
            paintersAlgorithm(root.getLeftSon(), g2);
        }
    }

    private Double getPovPosition(BSPNode root) {
            double[] line = root.getLine();

            double value;

            double returnValue = 0;

            if((line[0] == 1) && (line[1] == 0)) {
                value = -(line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
            }

            else if( line[0] > 0 ) {
                value = -(line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
            }
            else {
                value = (line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
            }

            if(value > 0) {
                returnValue = Double.POSITIVE_INFINITY;
            }

            else if (value < 0) {
                returnValue = Double.NaN;
            }

            return returnValue;
    }

    public void drawSegments(List<Segment> segments, Graphics2D g2) {

        for(Segment seg : segments) {
        }
    }

}
