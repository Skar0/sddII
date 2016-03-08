package be.ac.umons.gui;

import java.awt.geom.Line2D;

/**
 * Created by mr_robot on 3/3/16.
 */
public class Pov {
    private Line2D projectionLine;
    private double[] position = new double[2];
    private double angle;
    private Line2D line1 = null;
    private Line2D line2 = null;

    public Pov() {}

    public Pov(Line2D line1, Line2D line2) {
        this.line1 = line1;
        this.line2 = line2;
        this.angle = this.computeAngle(line1,line2);
        this.position[0] = line1.getX1();
        this.position[1] = line1.getY1();
        this.projectionLine = new Line2D.Double(line1.getX2(),line1.getY2(),line2.getX2(),line2.getY2());
    }

    private double computeAngle(Line2D line1, Line2D line2) {
        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),line1.getX1() - line1.getX2());
        double angle2 = Math.atan2(line2.getY1() - line2.getY2(), line2.getX1() - line2.getX2());
        return angle1-angle2;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
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

    public void setProjectionLine(Line2D projectionLine) { this.projectionLine = projectionLine; }
}
