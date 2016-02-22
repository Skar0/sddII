package be.ac.umons.bsp;

import java.util.Iterator;
import java.util.List;

/**
 * Created by clement on 2/20/16.
 */
public class BSPTree {
    private BSPTree rightSon;
    private BSPTree leftSon;
    private List<Segment> segments;
    private double a;
    private double b;
    private double c;

    public BSPTree(BSPTree rightSon, BSPTree leftSon, List<Segment> segments) {
        this.rightSon = rightSon;
        this.segments = segments;
        this.leftSon = leftSon;
    }

    public boolean isLeaf() {
        return (rightSon == null) && (leftSon == null);
    }

    public boolean hasNoRightSon() {
        return rightSon == null;
    }

    public boolean hasNoLeftSon() {
        return leftSon == null;
    }
    public void printTree() {
        for(Segment temp : this.segments) {
            System.out.println(temp.getColor());
        }
        System.out.println("\n");
        if (!this.isLeaf()) {
            if (this.hasNoLeftSon()) {
                this.rightSon.printTree();
            }

            if (this.hasNoRightSon()) {
                this.leftSon.printTree();
            }

            else {
                this.leftSon.printTree();
                this.rightSon.printTree();
            }
        }
    }

    public BSPTree getRightSon() {
        return rightSon;
    }

    public void setRightSon(BSPTree rightSon) {
        this.rightSon = rightSon;
    }

    public BSPTree getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(BSPTree leftSon) {
        this.leftSon = leftSon;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public void addSegment(Segment s) {
        this.segments.add(s);
    }

    public void computeLine() {
        Segment basis = this.segments.get(0);
        this.a = (basis.getY2() - basis.getY1()) / (basis.getX2()-basis.getX1());
        this.b = basis.getY1()-a*basis.getX1();
    }

    public double getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public double getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public double getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public static void main(String [] args) {

    }
}
