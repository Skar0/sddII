package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.util.List;

/**
 * Created by clement on 2/28/16.
 */
public class mainPanel extends JPanel {

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

    public void drawSegments(List<Segment> segments) {

    }

}
