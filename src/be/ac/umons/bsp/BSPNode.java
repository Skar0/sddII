package be.ac.umons.bsp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
     * Display a node and recursively his children
     * @param spaceSize the space necessary for the display (depends of the size of de coordinates)
     * @param node current node displayed
     * @param prefix precedent nodes already built as a string
     * @param isTail if the second child
     * @param sb built string (with new node inside)
     * @return the String which is a representation of the tree
     */
    public static StringBuilder printNode(int spaceSize, BSPNode node, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        // Inspired of http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
        Segment value; //Current segment to display
        if (node.getSegmentsInLine().size()>0)
            value = node.getSegmentsInLine().get(0);
        else
            value = node.getSegmentsInHyperplane().get(0);

        DecimalFormat df = new DecimalFormat("0.##"); //To force 2 decimals maximum
        String toPrint = "[("+df.format(value.getX1())+";" +df.format(value.getY1())+")" +
                "("+df.format(value.getX2())+";"+df.format(value.getY2())+")]";
        int newSize = toPrint.length()+1;

        if(!node.hasNoRightSon()) {
            printNode(newSize, node.getRightSon(), new StringBuilder().append(prefix).append(
                    isTail ? "│" + repeat(spaceSize-1," ") : repeat(spaceSize," ")), false, sb);
        }

        sb.append(prefix).append(isTail ? "└────── " : "┌────── ").append(toPrint).append("\n");

        if(!node.hasNoLeftSon()) {
            printNode(newSize, node.getLeftSon(), new StringBuilder().append(prefix).append(
                    isTail ? repeat(spaceSize," ") : "│" + repeat(spaceSize-1," ")), true, sb);
            // Left son is tail because we are drawing a binary tree
        }
        return sb;
    }

    /**
     * Displays the tree from the given node
     * @param node the root node of the tree
     */
    public static void printNode(BSPNode node) {
        DecimalFormat df = new DecimalFormat("0.##");
        String toPrint = "[("+df.format(node.getSegmentsInLine().get(0).getX1())+ ";"
                + df.format(node.getSegmentsInLine().get(0).getY1())+ ")" +
                "("+df.format(node.getSegmentsInLine().get(0).getX2())+ ";" +
                df.format(node.getSegmentsInLine().get(0).getY2())+ ")]";
        int size = toPrint.length();
        System.out.println("Affichage de l'arbre BSP, précision à deux décimales près : ");
        System.out.println(printNode(size, node, new StringBuilder(), true, new StringBuilder()).toString());
    }

    /**
     * Builds a string with the buffer and the desired number of spaces
     * @param count the number of desired spaces
     * @param buffer the first character of the string
     * @return a string containing the buffer followed by a number (count) of spaces
     */
    public static String repeat(int count, String buffer) {
        if (count == 0)
            return buffer;
        return repeat (count-1, buffer + " ");
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
     * Recursively computes the number of segments in the tree this BSPNode is the root of.
     * @return An integer giving the number of segments in the tree.
     */
    public int getSizeInSegments() {

        if(this.isLeaf()) {
            return this.getSegmentsInLine().size();
        }
        if(this.hasNoRightSon()) {
            return this.getSegmentsInLine().size() + this.leftSon.getSizeInSegments();
        }
        else if (this.hasNoLeftSon()) {
            return this.getSegmentsInLine().size() + this.rightSon.getSizeInSegments();
        }
        else return (this.getSegmentsInLine().size() +this.leftSon.getSizeInSegments()+this.rightSon.getSizeInSegments());
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
