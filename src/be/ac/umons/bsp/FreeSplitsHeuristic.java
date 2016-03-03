package be.ac.umons.bsp;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremy on 2/26/16.
 */
public class FreeSplitsHeuristic {
    /**
     * Creates a BSP tree using the random heuristic with free splits optimisation
     * @param segmentList List of segments to make a BSP tree out of
     * @return The root of the created BSP tree
     */
    public BSPNode createTree(List<Segment> segmentList){
        Collections.shuffle(segmentList);
        Segment firstSegment = segmentList.get(0);
        segmentList.remove(0);
        BSPNode root = new BSPNode(null, null,segmentList, firstSegment);
        treeConstruction(root);
        return root;
    }

    /**
     * @param currentNode Current root in the construction of the tree which contains the segment
     *                    defining the new line we use to create the new hyperplane
     */
    public void treeConstruction(BSPNode currentNode){
        //We have at least a segment which determines two new hyperplanes
        if(currentNode.getSegmentsInLine().size() != 0) {
            //If lines are equals, seg is contained in the splitting line of the node
            List<Segment> toRemove = new LinkedList<>();
            for (Segment seg : currentNode.getSegmentsInHyperplane()) {
                if ((seg.computeLine()[0] == currentNode.getLine()[0])
                        && (seg.computeLine()[1] == currentNode.getLine()[1])
                        && (seg.computeLine()[2] == currentNode.getLine()[2])) {
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

            //TODO: Modifier les conditions liées au free splits
            if (!freeSplitLeftOk || !freeSplitRightOk) {
                //Random choice of the new splitting segment if free splits strategy failed
                Segment splittingSegment;
                //Each new hyperplane contains at least one element
                if (!leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {
                    splittingSegment = leftNodeSegments.get(0);
                    leftNodeSegments.remove(0);
                    leftNode = new BSPNode(null, null, leftNodeSegments, splittingSegment);
                    splittingSegment = rightNodeSegments.get(0);
                    rightNodeSegments.remove(0);
                    rightNode = new BSPNode(null, null, rightNodeSegments, splittingSegment);

                    currentNode.setLeftSon(leftNode);
                    currentNode.setRightSon(rightNode);
                }
                //Only new left hyperplane contains at least one element
                else if (!leftNodeSegments.isEmpty() && rightNodeSegments.isEmpty()) {
                    //Only one element, no new definition of hyperplane, just one element inside the older one
                    if (leftNodeSegments.size() == 1) {
                        leftNode = new BSPNode(null, null, leftNodeSegments, null);
                        currentNode.setLeftSon(leftNode);
                    } else {
                        splittingSegment = leftNodeSegments.get(0);
                        leftNodeSegments.remove(0);
                        leftNode = new BSPNode(null, null, leftNodeSegments, splittingSegment);
                        currentNode.setLeftSon(leftNode);
                    }
                    currentNode.setLeftSon(leftNode);
                }
                //Only new right hyperplane contains at least one element
                else if (leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {
                    //Only one element, no new definition of hyperplane, just one element inside the older one
                    if (rightNodeSegments.size() == 1) {
                        rightNode = new BSPNode(null, null, rightNodeSegments, null);
                        currentNode.setRightSon(rightNode);
                    } else {
                        splittingSegment = rightNodeSegments.get(0);
                        rightNodeSegments.remove(0);
                        rightNode = new BSPNode(null, null, rightNodeSegments, splittingSegment);
                    }
                    currentNode.setRightSon(rightNode);
                }
            }
            treeConstruction(currentNode.getLeftSon());
            treeConstruction(currentNode.getRightSon());
        }
    }

    public static void main (String [] args){
        /*
        SegmentLoader loader = new SegmentLoader("assets/random/randomSmall.txt");
        List<Segment>myList = loader.loadAsList();
        FreeSplitsHeuristic test = new FreeSplitsHeuristic();
        test.createTree(myList);
        */
    }
}
