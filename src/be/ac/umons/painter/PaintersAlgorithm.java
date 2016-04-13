package be.ac.umons.painter;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Heuristic;
import be.ac.umons.bsp.Segment;
import be.ac.umons.gui.Pov;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

/**
 * Created by mr_robot on 13-04-16.
 */
public class PaintersAlgorithm {

    public List<Segment> getSegmentToDraw(BSPNode root, Pov pov) {
        List<Segment> toDraw = new LinkedList<>();
        paintersAlgorithm(root, pov, toDraw);
        return toDraw;
    }


    public void paintersAlgorithm(BSPNode root, Pov pov, List toDraw) {
        if (root != null) {
            if (root.isLeaf()) {
                scanConvert(root.getSegmentsInLine(),pov,toDraw);
            } else if (getPovPosition(root, pov).isInfinite()) {
                paintersAlgorithm(root.getLeftSon(), pov, toDraw);
                scanConvert(root.getSegmentsInLine(), pov, toDraw);
                paintersAlgorithm(root.getRightSon(), pov, toDraw);
            } else if (getPovPosition(root, pov).isNaN()) {
                paintersAlgorithm(root.getRightSon(), pov, toDraw);
                scanConvert(root.getSegmentsInLine(), pov, toDraw);
                paintersAlgorithm(root.getLeftSon(), pov, toDraw);
            } else {
                paintersAlgorithm(root.getRightSon(), pov, toDraw);
                paintersAlgorithm(root.getLeftSon(), pov, toDraw);
            }
        }
    }


    private Double getPovPosition(BSPNode root, Pov pov) {
        double[] line = root.getLine();

        double value;

        double returnValue = 0;

        if(Math.abs(line[0]-1)<= Heuristic.EPSILON && (line[1] == 0)) {
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

    /**
     * Draws all the segments and parts of segments in the list visible by the point of view
     * @param segments the list of segments
     */
    public void scanConvert(java.util.List<Segment> segments, Pov pov, List<Segment> toDraw) {
        //First we compute the lines that make the point of view
        double[] povLine1 = this.computeLine(pov.getLine1().getX1(), pov.getLine1().getY1(),pov.getLine1().getX2(),pov.getLine1().getY2());
        Segment povSegment1 = new Segment(pov.getLine1().getX1(), pov.getLine1().getY1(),pov.getLine1().getX2(),pov.getLine1().getY2(), Color.PINK);
        double[] povLine2 = this.computeLine(pov.getLine2().getX1(), pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2());
        Segment povSegment2 = new Segment(pov.getLine2().getX1(), pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2(), Color.PINK);

        double[] povPosition = pov.getPosition();

        //Angle between the director vector (bisectrix of both povLines) and one of the povLines (giving us half of the angle of the pov
        double semiAngle = this.computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(),pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),pov.getLine2().getX1(),pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2());

        //The projection line joins both ends of the segments that make the point of view
        double[] projectionLine = this.computeLine(pov.getProjectionLine().getX1(), pov.getProjectionLine().getY1(),pov.getProjectionLine().getX2(),pov.getProjectionLine().getY2());

        //both "bounds" ie ends of the projection segment
        double[] bound1 = {pov.getLine1().getX2(), pov.getLine1().getY2()};
        double[] bound2 = {pov.getLine2().getX2(), pov.getLine2().getY2()};

        //For each segment, we check if it is visible by the pov
        for(Segment seg : segments) {

            //Angles between the director vector and the lines from the pov origin to the ends of the segment
            double angle1 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(), povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double angle2 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),povPosition[0], povPosition[1],seg.getX2(), seg.getY2());

            //If both angles are smaller than the semi angle the segment is completely seen by the pov
            if(toAbsDeg(angle1) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {

                //We compute the lines from the pov position to the ends of the segment and then the intersection of those
                //lines with the projection line
                double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                double[] intersection2 = this.computeIntersection(povToSegmentExtremity2,projectionLine);

                //Getting the right color for the segment
                //g2.setColor(seg.getColor());
                //Zoomed version of the painter's algorithm (starting on top of the frame, scaled to take more space
                //g2.draw(new Line2D.Double(((intersection1[0]-minBound)*u)-(maxWidth/2),y,((intersection2[0]-minBound)*u)-(maxWidth/2),y));
                //x-axis might be flipped because of the test above, we make sure to put the axes back to normal
                //g2.setTransform(orig);
                //Drawing the intersections directly on the pov lines
                toDraw.add(new Segment(intersection1[0],intersection1[1],intersection2[0],intersection2[1], seg.getColor()));
            }

            //If one of the angles is bigger and one is smaller, we know the segment is cut by one of the povLines, in this case
            //point (x1,y1) of the segment is inside the view, we want to know the intersection with povLine1 OR povLine2 in order
            //to display the cut segment
            else if(toAbsDeg(angle1) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) > (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {

                //We compute the position of the segment relative to the povLines
                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                //If there are two intersections, this means we intersect one of the segments making the pov, but also the
                //other line of the pov (segment goes from inside the view to behind the pov)
                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    //In order to determine which of the povLines is really intersected in front of the pov (giving us
                    //the information of which cut segment to display) we compute the distance from the point (x1,y1) which
                    //we know is inside the view (in front of the pov) to the intersections we just computed (one of them beeing
                    //in front of the pov and the other beeing behind)
                    double norm1 = Math.sqrt( Math.pow(inter1[0]-seg.getX1(),2) + Math.pow(inter1[1]-seg.getY1(),2));
                    double norm2 = Math.sqrt( Math.pow(inter2[0]-seg.getX1(),2) + Math.pow(inter2[1]-seg.getY1(),2));

                    //The smaller norm gives us which intersection is in front of the pov, thus telling us which part of the
                    //segment to display

                    //displaying from (x1,y1) projected on the projection line to bound1 or bound2 (those are the projections
                    //of the intersection between the segment and projectionLines)
                    if ( Math.min(norm1,norm2) == norm1) {
                        double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                        double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                        toDraw.add(new Segment(bound1[0],bound1[1],intersection[0],intersection[1],seg.getColor()));
                    }
                    else{
                        double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                        double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                        toDraw.add(new Segment(bound2[0],bound2[1],intersection[0],intersection[1],seg.getColor()));
                    }

                }
                //bound coté povsegment1 + seg x1,x2
                //If we only have one intersection, we know what to display (from (x1,y1) to that intersection) projected
                //on the projection line, with the projection of the intersection beeing one of the bounds
                else if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0])) {
                    double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                    double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                    toDraw.add(new Segment(bound1[0],bound1[1],intersection[0],intersection[1],seg.getColor()));
                }
                else if(!Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                    double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                    toDraw.add(new Segment(bound2[0],bound2[1],intersection[0],intersection[1],seg.getColor()));
                }
            }


            //Symetric case with inversed point inside the view
            else if(toAbsDeg(angle1) > (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {

                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double norm1 = Math.sqrt( Math.pow(inter1[0]-seg.getX2(),2) + Math.pow(inter1[1]-seg.getY2(),2));
                    double norm2 = Math.sqrt( Math.pow(inter2[0]-seg.getX2(),2) + Math.pow(inter2[1]-seg.getY2(),2));

                    if ( Math.min(norm1,norm2) == norm1) {
                        double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                        double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                        toDraw.add(new Segment(bound1[0],bound1[1],intersection[0],intersection[1],seg.getColor()));
                    }
                    else {
                        double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                        double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                        toDraw.add(new Segment(bound2[0],bound2[1],intersection[0],intersection[1],seg.getColor()));
                    }

                }

                else if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0])) {
                    double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                    double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                    toDraw.add(new Segment(bound1[0],bound1[1],intersection[0],intersection[1],seg.getColor()));
                }

                else if(!Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double[] povToSegmentExtremity = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                    double[] intersection = this.computeIntersection(povToSegmentExtremity,projectionLine);

                    toDraw.add(new Segment(bound2[0],bound2[1],intersection[0],intersection[1],seg.getColor()));
                }
            }

            //If both angles are bigger than the semi angle, one bigger on the left and one bigger on the right (so that
            //a segment completely on the left of the pov is not considered) and at least one of the angle is <90° (so we
            //don't consider segments behind the pov) ait means the segment crosses the point of view in front of it.
            else if( (toAbsDeg(angle1) < 90 || toAbsDeg(angle2) < 90) && toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) < 0) | (toDeg(angle1) < 0 && toDeg(angle2) >= 0)) ) {

                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                //If there are two intersections, we know to display only the segment between the intersection recursively
                //Either it will be completely inside the projection (case 1) or it will be behind it and will be discarded
                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0]) ) {
                    java.util.List<Segment> tempList = new LinkedList<>();
                    tempList.add(new Segment(inter1[0],inter1[1],inter2[0],inter2[1],seg.getColor()));
                    this.scanConvert(tempList,pov,toDraw);
                }

            }
        }
    }


    private double toDeg(double x) {
        return Math.toDegrees(x);
    }

    private double toAbs(double x) {
        return Math.abs(x);
    }
    private double toAbsDeg(double x) {
        return Math.abs(Math.toDegrees(x));
    }

    public static double computeAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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
        if (Math.abs(x2 - x1) <= Heuristic.EPSILON) {
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
            if(Math.abs(line1[0]-1)<= Heuristic.EPSILON && line1[1]==0) {

                intersectionPoint[0] = line1[2]/(-line1[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line2[0]) + line2[2];
            }
            else if (Math.abs(line2[0]-1)<= Heuristic.EPSILON && line2[1]==0){
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
