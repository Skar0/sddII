package be.ac.umons.bsp;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremy on 2/26/16.
 */
public class FreeSplitsHeuristic implements Heuristic {
    /**
     * Creates a BSP tree using the random heuristic with free splits optimisation
     * @param segmentList List of segments to make a BSP tree out of
     * @return The root of the created BSP tree
     */
    public BSPNode createTree(List<Segment> segmentList){
        //Collections.shuffle(segmentList);
        Segment firstSegment = segmentList.get(0);
        segmentList.remove(0);
        BSPNode root = new BSPNode(null, null, segmentList, firstSegment);
        treeConstruction(root);
        root.printTree();
        return root;
    }

    /**
     * @param currentNode Current root in the construction of the tree which contains the segment
     *                    defining the new line we use to create the new hyperplane
     */
    public void treeConstruction(BSPNode currentNode){
        System.out.println("Beginning  algo : " + currentNode);
        //We have at least a segment which determines two new hyperplanes
        if(currentNode.getSegmentsInLine() != null) {
            //If lines are equals, seg is contained in the splitting line of the node
            List<Segment> toRemove = new LinkedList<>();
            for (Segment seg : currentNode.getSegmentsInHyperplane()) {
                if ((seg.computeLine()[0] == currentNode.getLine()[0])
                        && (seg.computeLine()[1] == currentNode.getLine()[1])
                        && (seg.computeLine()[2] == currentNode.getLine()[2])) {
                    System.out.println("Segment Same");
                    currentNode.addSegment(seg);
                    toRemove.add(seg);
                }
            }
            currentNode.getSegmentsInHyperplane().removeAll(toRemove);

            //Creation of the lists of segments of the two new hyperplanes
            List<Segment> leftNodeSegments = new LinkedList<>();
            List<Segment> rightNodeSegments = new LinkedList<>();
            double hyperplaneLine[] = currentNode.getLine();
            Segment segInHyperplaneLine = currentNode.getSegmentsInLine().get(0);

            for (Segment seg : currentNode.getSegmentsInHyperplane()) {
                double[] intersection = seg.computePosition(hyperplaneLine, segInHyperplaneLine);
                //If void intersection, put segment in right or left list of segments
                if (Double.isInfinite(intersection[0])) {
                    rightNodeSegments.add(seg);
                } else if (Double.isNaN(intersection[0])) {
                    leftNodeSegments.add(seg);
                }
                //If intersection, split the segment into two new segments
                //TODO : peut être erreur lors de l'ajout des deux nouveaux segments, car gardent leur intersection ?
                else {
                    Color color = seg.getColor();
                    Segment firstSegment = new Segment(intersection[0], intersection[1], seg.getX1(), seg.getY1(), color);
                    Segment secondSegment = new Segment(intersection[0], intersection[1], seg.getX2(), seg.getY2(), color);
                    firstSegment.incrementCutCount();
                    secondSegment.incrementCutCount();
                    double firstIntersection[] = firstSegment.computePosition(hyperplaneLine, segInHyperplaneLine);
                    if (Double.isInfinite(firstIntersection[0])) {
                        rightNodeSegments.add(firstSegment);
                        leftNodeSegments.add(secondSegment);
                    } else {
                        rightNodeSegments.add(secondSegment);
                        leftNodeSegments.add(firstSegment);
                    }
                }
            }

            //Free splits strategy
            BSPNode leftNode;
            BSPNode rightNode;
            boolean leftIsLeaf = false;
            boolean rightIsLeaf = false;
            boolean freeSplitLeftOk = false;
            boolean freeSplitRightOk = false;
            //Useless method if only one segment in the list
            if (leftNodeSegments.size() > 1) {
                for (Segment seg : leftNodeSegments) {
                    if (seg.isFreeSplit()) {
                        freeSplitLeftOk = true;
                        leftNodeSegments.remove(seg);
                        leftNode = new BSPNode(null, null, leftNodeSegments, seg);
                        currentNode.setLeftSon(leftNode);
                        break;
                    }
                }
            }
            if (rightNodeSegments.size() > 1) {
                for (Segment seg : rightNodeSegments) {
                    if (seg.isFreeSplit()) {
                        freeSplitRightOk = true;
                        rightNodeSegments.remove(seg);
                        rightNode = new BSPNode(null, null, rightNodeSegments, seg);
                        currentNode.setRightSon(rightNode);
                        break;
                    }
                }
            }

            //Random choice of the new splitting segment if free splits strategy failed
            Segment splittingSegment;
            //Left part
            if (!freeSplitLeftOk){
                if (!leftNodeSegments.isEmpty()){
                    if (leftNodeSegments.size() == 1){
                        leftNode = new BSPNode(null, null, leftNodeSegments);
                        leftIsLeaf = true;
                    }
                    else {
                        splittingSegment = leftNodeSegments.get(0);
                        leftNodeSegments.remove(0);
                        leftNode = new BSPNode(null, null, leftNodeSegments, splittingSegment);
                    }
                    System.out.println("setleft  :"  + leftNode);
                    currentNode.setLeftSon(leftNode);
                }
            }
            //Right part
            if (!freeSplitRightOk){
                if (!rightNodeSegments.isEmpty()){
                    if (rightNodeSegments.size() == 1){
                        rightNode = new BSPNode(null, null, rightNodeSegments);
                        rightIsLeaf = true;
                    }
                    else {
                        splittingSegment = rightNodeSegments.get(0);
                        rightNodeSegments.remove(0);
                        rightNode = new BSPNode(null, null, rightNodeSegments, splittingSegment);
                    }
                    currentNode.setRightSon(rightNode);
                }
            }
            //Maybe there is no left or right son or it is a leaf, then the work is done
            if (!leftIsLeaf && currentNode.getLeftSon() != null)
                treeConstruction(currentNode.getLeftSon());
            if (!rightIsLeaf && currentNode.getRightSon() != null)
                treeConstruction(currentNode.getRightSon());
        }
    }

    @Override public String toString() {
        return "Free splits";
    }

    public static void main (String [] args){
        SegmentLoader loader = new SegmentLoader("assets/other/wikipediaExample.txt");
        List<Segment>myList = loader.loadAsList();
        FreeSplitsHeuristic test = new FreeSplitsHeuristic();
        test.createTree(myList);
    }
}
