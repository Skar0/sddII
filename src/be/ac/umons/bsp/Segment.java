package be.ac.umons.bsp;

import java.awt.*;

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

    public boolean inLine(double a, double b) {
        double slope = y2 - y1 / x2 -x2;
        double y_intercept = y1+slope*x1;
        return (slope == a) && (y_intercept == b);
    }

    public double computePosition(double a, double b) {
        double slope = y2 - y1 / x2 -x2;
        double y_intercept = y1+slope*x1;

        return 42;
    }
}
