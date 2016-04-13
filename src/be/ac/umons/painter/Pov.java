package be.ac.umons.painter;

import java.awt.geom.Line2D;

/**
 * This class represents a point of view
 * @author Clément Tamines
 */
public class Pov {
    /** The director vector of the point of view (line between the two lines of the pov) **/
    private Line2D directorVector;

    /** The line on which the scene will be projected **/
    private Line2D projectionLine;

    /** The position of the point of view **/
    private double[] position = new double[2];

    /** The first line defining the pov **/
    private Line2D line1 = null;

    /** The second line defining the pov **/
    private Line2D line2 = null;

    public Pov() {}

    public Pov(Line2D line1, Line2D line2) {
        this.line1 = line1;
        this.line2 = line2;
        this.position[0] = line1.getX1();
        this.position[1] = line1.getY1();
        this.projectionLine = new Line2D.Double(line1.getX2(),line1.getY2(),line2.getX2(),line2.getY2());
        this.directorVector = new Line2D.Double(line1.getX1(),line1.getY1(),(line2.getX2()+line1.getX2())/2D,(line2.getY2()+line1.getY2())/2D);
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public Line2D getLine1() {
        return line1;
    }

    public void setLine1(Line2D line1) {
        this.line1 = line1;
    }

    public Line2D getLine2() {
        return line2;
    }

    public void setLine2(Line2D line2) {
        this.line2 = line2;
    }

    public Line2D getProjectionLine() { return this.projectionLine; }

    public Line2D getDirectorVector() { return this.directorVector; }

    public void setProjectionLine(Line2D projectionLine) { this.projectionLine = projectionLine; }
}
