package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Heuristic;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by clement on 2/28/16.
 */
public class PaintersAlgorithm extends JPanel {

    private double maxWidth;
    private Pov pov;
    private BSPNode rootOfTree;

    int toRemove = 10;

    public PaintersAlgorithm(Pov pov, BSPNode rootOfTree) {
        this.pov = pov;
        this.rootOfTree = rootOfTree;
       this.maxWidth = Math.sqrt(Math.pow((pov.getProjectionLine().getX1()-pov.getProjectionLine().getX2()),2))+400;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double scaley = (double) this.getWidth()/maxWidth;
        g2.translate((double) this.getWidth()/2, (double) this.getHeight()/2);
        g2.scale(scaley, 1);

        paintersAlgorithm(rootOfTree, g2);
        toRemove = 1;
    }

    public void paintersAlgorithm(BSPNode root, Graphics2D g2 ) {
        if (root != null) {
            if (root.isLeaf()) {
                drawSegments(root.getSegmentsInLine(), g2);
            } else if (getPovPosition(root).isInfinite()) {
                paintersAlgorithm(root.getLeftSon(), g2);
                drawSegments(root.getSegmentsInLine(), g2);
                paintersAlgorithm(root.getRightSon(), g2);
            } else if (getPovPosition(root).isNaN()) {
                paintersAlgorithm(root.getRightSon(), g2);
                drawSegments(root.getSegmentsInLine(), g2);
                paintersAlgorithm(root.getLeftSon(), g2);
            } else {
                paintersAlgorithm(root.getRightSon(), g2);
                paintersAlgorithm(root.getLeftSon(), g2);
            }
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
        double semiAngle = pov.computeAngle(pov.getDirectorVector(), pov.getLine2());


        double[] projectionLine = this.computeLine(pov.getProjectionLine().getX1(), pov.getProjectionLine().getY1(),pov.getProjectionLine().getX2(),pov.getProjectionLine().getY2());

        double[] bound1 = {pov.getLine1().getX2(), pov.getLine1().getY2()};
        double[] bound2 = {pov.getLine2().getX2(), pov.getLine2().getY2()};

        for(Segment seg : segments) {
            double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double angle1 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(), povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            //double angle1 = pov.computeAngle(pov.getDirectorVector(), new Line2D.Double(povPosition[0], povPosition[1],seg.getX1(), seg.getY1()));
            double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
            double angle2 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
            //double angle2 = pov.computeAngle(pov.getDirectorVector(), new Line2D.Double(povPosition[0], povPosition[1],seg.getX2(), seg.getY2()));

            double[] intersection1 = new double[2];
            double[] intersection2 = new double[2];
            System.out.println(toRemove+" "+seg.getColor().toString()+" "+Math.toDegrees(angle1)+" "+Math.toDegrees(angle2)+" "+Math.toDegrees(semiAngle));
            //Segment completly in front of point of view
          //if(-Heuristic.EPSILON<Math.abs(Math.toDegrees(angle1)) && Math.abs(Math.abs(Math.toDegrees(angle1))-90)<Heuristic.EPSILON && -Heuristic.EPSILON<Math.abs(Math.toDegrees(angle2)) && Math.abs(Math.abs(Math.toDegrees(angle2))-90)<Heuristic.EPSILON) {
            if(Math.toDegrees(angle1) < 90 && Math.toDegrees(angle1) > -90 && Math.toDegrees(angle2) < 90 && Math.toDegrees(angle2) > -90){
                intersection1 = this.computeIntersection(povToSegmentExtremity1, projectionLine);
                intersection2 = this.computeIntersection(povToSegmentExtremity2, projectionLine);
/*
                si angle plus grand que demis angle mêe coté on fai rien

                        cas sym pour autre coté
                si deux dans le bon,
                si un des deux dans le bon, on coupe
*/

            
                //Segment contained in projection
                if( toAbsDeg(angle1) <= toAbsDeg(semiAngle) && toAbsDeg(angle2) <= toAbsDeg(semiAngle) ) {
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(intersection1[0],toRemove,intersection2[0],toRemove));
                    g2.draw(new Line2D.Double(intersection1[0],0,intersection2[0],0));
                }
                //not in projection
                else if( toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) >= 0) | (toDeg(angle1) < 0 && toDeg(angle2) < 0)) ) {
                    /*
                    System.out.println("------");
                    System.out.println("cas hors");
                    System.out.println(seg.getColor());
                    System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));
                    */
                }
                //segment completely crosses both projectionlines
                else if( toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) < 0) | (toDeg(angle1) < 0 && toDeg(angle2) >= 0)) ) {
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound1[0],toRemove,bound2[0],toRemove));
                    g2.draw(new Line2D.Double(bound1[0],0,bound2[0],0));
                    /*
                    System.out.println("------");
                    System.out.println("cas coupe completement");
                    System.out.println(seg.getColor());
                    System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));
                    */
                }
                //on prend une des deux intersections, et l'autre sera le bound
                else if( (toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) <= toAbsDeg(semiAngle)) | (toAbsDeg(angle2) > toAbsDeg(semiAngle) && toAbsDeg(angle1) <= toAbsDeg(semiAngle)) ) {

                    //double[] intersection = computeIntersection(this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2()), seg.computeLine());
                    double[] position = seg.computePosition(this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2()), new Segment(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2(), Color.black));

                    if(!Double.isNaN(position[0]) && ! Double.isInfinite(position[0])) {
                        LinkedList<Segment> segmentList = new LinkedList<Segment>();
                        segmentList.add(new Segment(seg.getX1(),seg.getY1(),bound1[0],bound1[1],seg.getColor()));
                        segmentList.add(new Segment(seg.getX2(),seg.getY2(),bound1[0],bound1[1],seg.getColor()));
                        this.drawSegments(segmentList,g2);
                    }
                    else {
                        LinkedList<Segment> segmentList = new LinkedList<Segment>();
                        segmentList.add(new Segment(seg.getX1(),seg.getY1(),bound2[0],bound2[1],seg.getColor()));
                        segmentList.add(new Segment(seg.getX2(),seg.getY2(),bound2[0],bound2[1],seg.getColor()));
                        this.drawSegments(segmentList,g2);
                    }

                    //g2.setColor(seg.getColor());
                    //g2.draw(new Line2D.Double(bound1[0],toRemove,intersection2[0],toRemove));
                    //g2.draw(new Line2D.Double(bound1[0],0,intersection2[0],0));
                }

            }
        }
        toRemove+=10;
    }

    private double toDeg(double x) {
        return Math.toDegrees(x);
    }

    private double toAbsDeg(double x) {
        return Math.abs(Math.toDegrees(x));
    }
    public double computeAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] vector1 = {x2-x1,y2-y1};
        double[] vector2 = {x4-x3,y4-y3};
        double crossProd = (vector1[0]*vector2[0])+(vector1[1]*vector2[1]);
        double v1Norm = Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
        double v2Norm = Math.sqrt( Math.pow(x3-x4,2) + Math.pow(y3-y4,2));
        double cos = (double) crossProd/(v1Norm*v2Norm);
        //return  Math.acos(cos);
        double dotProd = (vector1[0]*vector2[1])-(vector1[1]*vector2[0]);
        return  Math.atan2(dotProd,crossProd);
    }

    public double[] computeLine(double x1, double y1, double x2, double y2) {

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

    public double[] computeIntersection(double[] line1, double[] line2) {

        double[] intersectionPoint = new double[2];

        if(Math.abs(line1[0]-line2[0]) < Heuristic.EPSILON) {
            return null; //droites parallèles
        }
        else {
            // System.out.println("debug "+line[0]+" "+line[1]+" "+line[2]);
            // System.out.println("debug "+lineBis[0]+" "+lineBis[1]+" "+lineBis[2]);
            if(Math.abs(line1[0]-1)<Heuristic.EPSILON || Math.abs(line1[0]+1)<Heuristic.EPSILON ) {

                intersectionPoint[0] = line1[2]/(-line1[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line2[0]) + line2[2];
            }
            else if (Math.abs(line2[0]-1)<Heuristic.EPSILON || Math.abs(line2[0]+1)<Heuristic.EPSILON){
                intersectionPoint[0] = line2[2]/(-line2[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line1[0]) + line1[2];

            }
            else {
                intersectionPoint[0] = (double) ((line2[2] - line1[2]) / (line1[0] - line2[0]));
                intersectionPoint[1] = (intersectionPoint[0] * line1[0]) + line1[2];
            }

            return intersectionPoint;
        }
    }

}
