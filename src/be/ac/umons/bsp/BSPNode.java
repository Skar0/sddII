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

    public BSPNode(BSPNode rightSon, BSPNode leftSon, List<Segment> segmentsInHyperplane){
        this.rightSon = rightSon;
        this.leftSon = leftSon;
        this.segmentsInHyperplane = segmentsInHyperplane;
        this.line = null;
    }

    public List<Segment> getSegmentsInLine() {
        return segmentsInLine;
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
        System.out.println("---------------------");
        System.out.println("Node : "+this);
        for(Segment temp : this.segmentsInLine) {
            System.out.print("[ ("+temp.getX1()+","+temp.getY1()+") ("+temp.getX2()+","+temp.getY2()+") ] ");
        }
        System.out.println();
        System.out.println("Left son : " +leftSon);
        System.out.println("Right son : "+rightSon);
        System.out.println("---------------------\n");

        /*
        if (leftSon != null) {
            if (leftSon.isLeaf())
                System.out.print("[ (" + leftSon.getSegmentsInHyperplane().get(0).getX1() +
                        "," + leftSon.getSegmentsInHyperplane().get(0).getY1() +
                        ") (" + leftSon.getSegmentsInHyperplane().get(0).getX2() +
                        "," + leftSon.getSegmentsInHyperplane().get(0).getY2() + ") ] \n");
            System.out.println("Right son : " + rightSon);
        }
        if (rightSon != null) {
            if (rightSon.isLeaf())
                System.out.print("[ (" + rightSon.getSegmentsInHyperplane().get(0).getX1() +
                        "," + rightSon.getSegmentsInHyperplane().get(0).getY1() +
                        ") (" + rightSon.getSegmentsInHyperplane().get(0).getX2() +
                        "," + rightSon.getSegmentsInHyperplane().get(0).getY2() + ") ] \n" );
            System.out.println("---------------------\n");
        }

        */
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
        else return Math.max( (1+this.leftSon.getHeight()), (1+this.rightSon.getHeight()) );
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

    public void addSegmentInHyperplane(Segment seg) {
        this.segmentsInHyperplane.add(seg);
    }

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
}
