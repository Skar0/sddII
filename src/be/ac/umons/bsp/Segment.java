package be.ac.umons.bsp;

import java.awt.*;

/**
 * Created by clement on 2/20/16.
 */
public class Segment {
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private Color c;

    public Segment(float x1, float y1, float x2, float y2, String color) {
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

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }
}
