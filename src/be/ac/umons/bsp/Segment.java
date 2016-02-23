package be.ac.umons.bsp;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Created by clement on 2/20/16.
 */
public class Segment {

    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private Color c;

    public Segment(double x1, double y1, double x2, double y2, String color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        switch (color) {
            case "Bleu" :
                this.c = Color.blue;
                break;
            case "Rouge" :
                this.c = Color.red;
                break;
            case "Orange" :
                this.c = Color.orange;
                break;
            case "Jaune" :
                this.c = Color.yellow;
                break;
            case "Noir" :
                this.c = Color.black;
                break;
            case "Violet" :
                this.c = new Color(255,0,255);
                break;
            case "Marron" :
                this.c = new Color(102,51,0);
                break;
            case "Vert" :
                this.c = Color.green;
                break;
            case "Gris" :
                this.c = new Color(128,128,128);
                break;
            case "Rose" :
                this.c = Color.pink;
                break;
        }
    }

    /**
     * Computes the line joining the two points
     * @return the line in ax + by + c = 0 with a,b,c in a double array
     */
    public double[] computeLine() {
        /*
        double[] line = new double[3];

        //If x2-x1 == 0 the line is vertical, its equation is 1*x + 0*y - x1
        if(x2-x1 == 0) {
            line[0] = 1;
            line[1] = 0;
            line[2] = -x1;
            return line;
        }
        else {
            line[0] = y2 - y1 / x2 - x1;
            line[1] = y1+line[0]*x1;
        }
        */
        double[] line = new double[3];
        line[0] = y1-y2;
        line[1] = x2-x1;
        line[2] = x1*y2 - x2*y1;
        return line;
    }

    public double[] computePosition(double[] line) {
        //TODO calculer intersection de la droite (this.computeLine) de ce segment avec la droite "line"
        //si dedans, retourne point, si gauche -inf, si droite +inf
        double[] lineBis = this.computeLine();

        return null;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public Color getColor() {
        return c;
    }

    public void setColor(Color c) {
        this.c = c;
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

    public void setY2(double y2) {
        this.y2 = y2;
    }

}
