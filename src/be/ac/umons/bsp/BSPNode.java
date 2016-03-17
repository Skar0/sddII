package be.ac.umons.bsp;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Clément Tamines
 * This class represents a node of a BSP tree. We construct a BSP tree recursively by keeping a reference to a root node and adding to each node a left and/or right son.
 * There is no empty node, a leaf is a node that doesn't have any son.
 */
public class BSPNode {

    private BSPNode rightSon;
    private BSPNode leftSon;

    /**
     * This list contains Segment objects. They are the segments contained in the hyperplane formed by following the nodes from the root node until this one.
     */
    private List<Segment> segmentsInHyperplane;

    /**
     * This list contains the segments that are on the same line as this node's line.
     */
    private List<Segment> segmentsInLine = new LinkedList<>();

    /**
     * The line used to cut the space in two partitions.
     */
    private double[] line;

    /**
     * BSPNode constructor
     * @param rightSon
     * @param leftSon
     * @param segmentsInHyperplane
     * @param segment The segment from which the line used to cut the space is created.
     */
    public BSPNode(BSPNode rightSon, BSPNode leftSon, List<Segment> segmentsInHyperplane, Segment segment) {
        this.rightSon = rightSon;
        this.leftSon = leftSon;
        this.segmentsInHyperplane = segmentsInHyperplane;
        this.segmentsInLine.add(segment);
        this.line = segment.computeLine();
    }

    public BSPNode(BSPNode rightSon, BSPNode leftSon, List<Segment> segmentsInHyperplane){
        this.rightSon = rightSon;
        this.leftSon = leftSon;
        this.segmentsInHyperplane = segmentsInHyperplane;
        this.line = null;
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
        System.out.println("---------------------");
        System.out.println("Node : "+this);
        for(Segment temp : this.segmentsInLine) {
            System.out.print("[ ("+temp.getX1()+","+temp.getY1()+") ("+temp.getX2()+","+temp.getY2()+") ] ");
        }
        System.out.println();
        System.out.println("Left son : " +leftSon);
        System.out.println("Right son : "+rightSon);
        System.out.println("---------------------\n");

        if (!this.isLeaf()) {

            if (this.hasNoLeftSon()) {
                this.rightSon.printTree();
            }

            else if (this.hasNoRightSon()) {
                this.leftSon.printTree();
            }

            else {
                this.leftSon.printTree();
                this.rightSon.printTree();
            }
        }
    }

    /**
     * Recursively computes the height of the tree this BSPNode is the root of.
     * @return An integer giving the height of the tree.
     */
    public int getHeight() {
        if(this.isLeaf()) {
            return 1;
        }
        if(this.hasNoRightSon()) {
            return 1 + this.leftSon.getHeight();
        }
        else if (this.hasNoLeftSon()) {
            return 1 + this.rightSon.getHeight();
        }
        else return Math.max(( 1 + this.leftSon.getHeight() ), ( 1 + this.rightSon.getHeight() ));
    }

    /**
     * Recursively computes the number of nodes of the tree this BSPNode is the root of.
     * @return An integer giving the size of the tree.
     */
    public int getSize() {

        if(this.isLeaf()) {
            return 1;
        }
        if(this.hasNoRightSon()) {
            return 1 + this.leftSon.getSize();
        }
        else if (this.hasNoLeftSon()) {
            return 1 + this.rightSon.getSize();
        }
        else return (1+this.leftSon.getSize()+this.rightSon.getSize());
    }

    /**
     *
     * @return the segments on the same line as the cutting line.
     */
    public List<Segment> getSegmentsInLine() {
        return segmentsInLine;
    }

    /**
     *
     * @return the segments contained in the hyperplane formed by the successions of cuts from the root node up to this node.
     */
    public List<Segment> getSegmentsInHyperplane() {
        return segmentsInHyperplane;
    }

    /**
     * Replaces the segments contained in the hyperplane by new ones.
     * @param segmentsInHyperplane the new segments in the hyperplane.
     */
    public void setSegmentsInHyperplane(List<Segment> segmentsInHyperplane) {
        this.segmentsInHyperplane = segmentsInHyperplane;
    }

    /**
     * Adds a new segment to this node's hyperplane.
     * @param seg the new Segment object.
     */
    public void addSegmentInHyperplane(Segment seg) {
        this.segmentsInHyperplane.add(seg);
    }

    /**
     * Adds a segment to the list of segments contained in this node's cutting line.
     * @param s the segment to add in the list.
     */
    public void addSegment(Segment s) {
        this.segmentsInLine.add(s);
    }

    /**
     *
     * @return the coefficents a,b,c of equation of the cutting line ax + by + c = 0 in a 3 element array.
     */
    public double[] getLine() {
        return line;
    }

    /**
     *
     * @return the BSPNode that is the right son of this node.
     */
    public BSPNode getRightSon() {
        return rightSon;
    }

    /**
     * Replaces the current right son of this BSPNode by a new one.
     * @param rightSon the new BSPNode that will replace the current right son.
     */
    public void setRightSon(BSPNode rightSon) {
        this.rightSon = rightSon;
    }

    /**
     *
     * @return the BSPNode that is the left son of this node.
     */
    public BSPNode getLeftSon() {
        return leftSon;
    }

    /**
     * Replaces the current left son of this BSPNode by a new one.
     * @param leftSon the new BSPNode that will replace the current left son.
     */
    public void setLeftSon(BSPNode leftSon) {
        this.leftSon = leftSon;
    }

}
