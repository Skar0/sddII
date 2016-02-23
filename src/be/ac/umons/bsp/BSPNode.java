package be.ac.umons.bsp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by clement on 2/20/16.
 */
public class BSPNode {

    private BSPNode rightSon;
    private BSPNode leftSon;
    private List<Segment> segmentsInHyperplane;
    private List<Segment> segmentsInLine = new LinkedList<>();
    private double[] line;

    public BSPNode(BSPNode rightSon, BSPNode leftSon, List<Segment> segmentsInHyperplane, Segment segment) {
        this.rightSon = rightSon;
        this.leftSon = leftSon;
        this.segmentsInHyperplane = segmentsInHyperplane;
        this.segmentsInLine.add(segment);
        this.line = segment.computeLine();
    }

    public double[] getLine() {
        return line;
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

        for(Segment temp : this.segmentsInHyperplane) {
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

    public List<Segment> getSegmentsInHyperplane() {
        return segmentsInHyperplane;
    }

    public void setSegmentsInHyperplane(List<Segment> segmentsInHyperplane) {
        this.segmentsInHyperplane = segmentsInHyperplane;
    }

    /**
     * Adds a segment to the list of segmentsInHyperplane contained in the BSPNode
     * @param s the segment to add
     */
    public void addSegment(Segment s) {
        this.segmentsInLine.add(s);
    }

    public static void main(String [] args) {

    }
}
