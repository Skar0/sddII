package be.ac.umons.bsp;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Objects;

/**
 * This class represents a segment with its two points and its color. It is also used to compute the cutting line from a
 * segment and determine the position of a segment relative to a line.
 *
 * @author Clément Tamines
 * @author Jérémy Gheisen for everything regarding free splits.
 */
public class Segment {

    /**
     * the x coordinate of the first point.
     */
    private double x1;

    /**
     * the y coordinate of the first point.
     */
    private double y1;

    /**
     * the x coordinate of the second point.
     */
    private double x2;

    /**
     * the y coordinate of the second point.
     */
    private double y2;

    private int cutCount;
    private boolean intersected1; //Boolean to know if the vertex (x1, y1) is intersected with another segment
    private boolean intersected2; //Boolean to know if the vertex (x2, y2) is intersected with another segment
    private boolean isFreeSplit;

    /**
     * The segment's color.
     */
    private Color color;

    /**
     *
     * @param x1 the x coordinate of the first point.
     * @param y1 the y coordinate of the first point.
     * @param x2 the x coordinate of the second point.
     * @param y2 the y coordinate of the second point.
     * @param color the color of the segment.
     */
    public Segment(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.intersected1 = false;
        this.intersected2 = false;
        this.cutCount = 0;
        this.isFreeSplit = false;
        this.color = color;
    }

    /**
     * Computes the that contains the two points of the segment.
     * @return a 3-elements double array containg the a,b,c coeficient of the equation ax + by + c = 0 of the line.
     */
    public double[] computeLine() {

        double[] line = new double[3];

        //If x2-x1 == 0 the line is vertical, its equation is 1*x + 0*y - x1
        if (Math.abs(x2 - x1) <= Heuristic.EPSILON) {
            line[0] = 1;
            line[1] = 0;
            line[2] = -x1;
            return line;
        }

        else {
            line[0] = (y2 - y1) / (x2 - x1);
            line[1] = -1;
            line[2] = y1 - (line[0] * x1);
            return line;
        }
    }

    /**
     * This method replaces the point (x,y) inside the equation of a line and returns the result.
     * @param line double array containing the coeficents a,b,c of the line equation in the form ax + by + c = 0
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @return a double value resulting of the replacement of the point in the equation.
     */
    public double getSide(double[] line, double x, double y) {

        //If the equation is in the form x - c = 0 (vertical line)
        if(Math.abs(line[0]-1)<= Heuristic.EPSILON && (line[1] == 0)) {
            return -(line[0]*x + line[1]*y +line[2]);
        }

        //Positive slope
        if( line[0] > 0 ) {
            return -(line[0]*x + line[1]*y +line[2]);
        }

        return (line[0]*x + line[1]*y +line[2]);
    }

    /**
     * Computes the position of the segment relative to the line given as parameter.
     * @param line double array containing the coeficents a,b,c of the line equation in the form ax + by + c = 0
     * @param segment the segment used to compute the line.
     * @return [Infinity,Infinity] if segment is on the right, [NaN,NaN] if segment is on the left, the intersection point of the line and the segment otherwise.
     */
    public double[] computePosition(double[] line, Segment segment) {

        //The line containing this segment.
        double[] lineBis = this.computeLine();

        //The array that will contain the intersection point.
        double[] intersectionPoint = new double[2];

        //If the slopes are equal, there is no intersection.
        if(Math.abs(line[0]-lineBis[0])<= Heuristic.EPSILON) {

            //If the point (x1,y1) from this segment is to the right of the line, return [+inf,+inf]
            if(getSide(line, x2, y2) > Heuristic.EPSILON) {

                intersectionPoint[0] = Double.POSITIVE_INFINITY;
                intersectionPoint[1] = Double.POSITIVE_INFINITY;
                return intersectionPoint;
            }

            //If both points are on the same line as the cutting line, throw error (the segments contained inside the cutting line are supposed to be handled by the heuristic.
            else if(Math.abs(getSide(line, x2, y2)) <= Heuristic.EPSILON && Math.abs(getSide(line, x1, y1)) <= Heuristic.EPSILON) {
                return null;
            }

            //If the point (x1,y1) from this segment is left of the line, return [NaN,NaN]
            else {
                intersectionPoint[0] = Double.NaN;
                intersectionPoint[1] = Double.NaN;
                return intersectionPoint;
            }
        }

        //If the slopes are not equal, we compute the intersection between the two lines.
        else {

            //If the cutting line is vertical, computing the intersection is handled in a different way.
            if(Math.abs(line[0]-1)<= Heuristic.EPSILON && line[1]==0) {

                intersectionPoint[0] = line[2]/(-line[0]);
                intersectionPoint[1] = (intersectionPoint[0] * lineBis[0]) + lineBis[2];
            }

            //Same if the line computed from this segment is vertical
            else if (Math.abs(lineBis[0]-1)<= Heuristic.EPSILON && lineBis[1]==0){
                intersectionPoint[0] = lineBis[2]/(-lineBis[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line[0]) + line[2];

            }

            //Else we just use the simple method of equating both equations of lines to find the intersection point.
            else {
                intersectionPoint[0] = (double) ((lineBis[2] - line[2]) / (line[0] - lineBis[0]));
                intersectionPoint[1] = (intersectionPoint[0] * line[0]) + line[2];
            }

            //If the intersection point is contained in the segment, we return the intersection point because the segment is cut
            if( ( ( ( (x1 <= intersectionPoint[0]+Heuristic.EPSILON) && (intersectionPoint[0]<= x2+Heuristic.EPSILON)) || ( (x2 <= intersectionPoint[0]+Heuristic.EPSILON) && (intersectionPoint[0]<= x1+Heuristic.EPSILON) ) ) &&
                    ( ( (y1 <= intersectionPoint[1]+Heuristic.EPSILON) && (intersectionPoint[1]<= y2+Heuristic.EPSILON) ) || ( (y2 <= intersectionPoint[1]+Heuristic.EPSILON) && (intersectionPoint[1]<= y1+Heuristic.EPSILON) ) ) )
                    && !(Math.abs(intersectionPoint[0]-x1)<=Heuristic.EPSILON && Math.abs(intersectionPoint[1]-y1)<=Heuristic.EPSILON) && !(Math.abs(intersectionPoint[0]-x2)<=Heuristic.EPSILON && Math.abs(intersectionPoint[1]-y2)<=Heuristic.EPSILON ) ) {

                return intersectionPoint;
            }

            //If both points are on the same line as the cutting line, throw error (the segments contained inside the cutting line are supposed to be handled by the heuristic.
            else if(Math.abs(getSide(line, x2, y2))<=Heuristic.EPSILON && Math.abs(getSide(line, x1, y1))<=Heuristic.EPSILON) {
                return null;
            }

            //If the point (x1,y1) or (x2,y2) from this segment is right of the line, return [+inf,+inf] (one of them can be contained in the segment, so it is a or condition)
            else if (((getSide(line, x2, y2) > 0) || (getSide(line, x1, y1) > 0))) {
                intersectionPoint[0] = Double.POSITIVE_INFINITY;
                intersectionPoint[1] = Double.POSITIVE_INFINITY;
                return intersectionPoint;
            }

            //Else it it on the left of the line and we return [NaN,NaN]
            else {
                intersectionPoint[0] = Double.NaN;
                intersectionPoint[1] = Double.NaN;
                return intersectionPoint;
            }
        }
    }

    /**
     * Increment the number of times
     */
    public void incrementCutCount(){
        cutCount++;
        if (cutCount >= 2)
            isFreeSplit = true;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Segment))
            return false;
        Segment castedSegment = (Segment) obj;
        return (Math.abs(this.x1 - castedSegment.x1) < Heuristic.EPSILON) && (Math.abs(this.x2 - castedSegment.x2) < Heuristic.EPSILON)
                && (Math.abs(this.y1 - castedSegment.y1) < Heuristic.EPSILON) && (Math.abs(this.y2 - castedSegment.y2) < Heuristic.EPSILON);
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) { this.y2 = y2; }

    public boolean getIsIntersected1() { return intersected1; }

    public void setIntersected1(boolean intersected1) { this.intersected1 = intersected1; }

    public boolean getIsIntersected2() { return intersected2;}

    public void setIntersected2(boolean intersected2) { this.intersected2 = intersected2; }

    public int getCutCount() {return cutCount; }

    public void setCutCount(int cutCount) { this.cutCount = cutCount; }

    public boolean isFreeSplit() { return isFreeSplit; }

}
