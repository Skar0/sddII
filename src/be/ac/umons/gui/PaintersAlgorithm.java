package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
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
        Line2D povLine1 = pov.getLine1();
        Line2D povLine2 = pov.getLine2();
        double[] povPosition = pov.getPosition();

        for(Segment seg : segments) {
            double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());

            //intersection of povTosegmentExtremity1 with projection line



        }
    }

    public double[] computeLine(double x1, double x2, double y1, double y2) {

        double[] line = new double[3];

        //If x2-x1 == 0 the line is vertical, its equation is 1*x + 0*y - x1
        if (x2 - x1 == 0) {
            line[0] = 1;
            line[1] = 0;
            line[2] = -x1;
            return line;
        } else {
            line[0] = (y2 - y1) / (x2 - x1);
            line[1] = -1;
            line[2] = y1 - (line[0] * x1);
            return line;
        }

    }

    public double[] computeIntersection(Line2D line1, Line2D line2) {
        return null;
    }

}
