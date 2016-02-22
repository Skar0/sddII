package be.ac.umons.bsp;

import java.util.List;

/**
 * Created by clement on 2/20/16.
 */
public class BSPNode {

    private BSPNode rightSon;
    private BSPNode leftSon;
    private List<Segment> segments;
    private double a;
    private double b;
    private double c;

    public BSPNode(BSPNode rightSon, BSPNode leftSon, List<Segment> segments) {
        this.rightSon = rightSon;
        this.leftSon = leftSon;
        this.segments = segments;
    }

    /**
     * Computes the line that is prolonged from the first segment in the segments list
     */
    public void computeLine() {
        Segment basis = this.segments.get(0);
        this.a = (basis.getY2() - basis.getY1()) / (basis.getX2()-basis.getX1());
        this.b = basis.getY1()-a*basis.getX1();
        this.c = -1;
    }

    /**
     * Determines if the BSPNode is a leaf
     * @return True if the node is a leaf, false otherwise
     */
    public boolean isLeaf() {
        return (rightSon == null) && (leftSon == null);
    }

    /**
     * Determines if the BSPNode has a right son
     * @return True is it has no right son
     */
    public boolean hasNoRightSon() {
        return rightSon == null;
    }

    /**
     * Determines if the BSPNode has a left son
     * @return True is it has no left son
     */
    public boolean hasNoLeftSon() {
        return leftSon == null;
    }

    /**
     * Prints the tree in the command line
     */
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

    public BSPNode getRightSon() {
        return rightSon;
    }

    public void setRightSon(BSPNode rightSon) {
        this.rightSon = rightSon;
    }

    public BSPNode getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(BSPNode leftSon) {
        this.leftSon = leftSon;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * Adds a segment to the list of segments contained in the BSPNode
     * @param s the segment to add
     */
    public void addSegment(Segment s) {
        this.segments.add(s);
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
