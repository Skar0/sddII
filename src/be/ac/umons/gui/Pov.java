package be.ac.umons.gui;

/**
 * Created by mr_robot on 3/3/16.
 */
public class Pov {
    private double[] position;
    private double[] direction;
    private double angle;

    private double[] projectionLine;

    public Pov(double[] position, double[] direction, double angle, double[] projectionLine) {
        this.position = position;
        this.direction = direction;
        this.angle = angle;
        this.projectionLine = projectionLine;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getDirection() {
        return direction;
    }

    public void setDirection(double[] direction) {
        this.direction = direction;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double[] getProjectionLine() {
        return projectionLine;
    }

    public void setProjectionLine(double[] projectionLine) {
        this.projectionLine = projectionLine;
    }
}
