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
        Collections.shuffle(segmentList);
        Segment firstSegment = segmentList.get(0);
        segmentList.remove(0);
        BSPNode root = new BSPNode(null, null, segmentList, firstSegment);
        treeConstruction(root);
        return root;
    }

    /**
     * @param currentNode Current root in the construction of the tree which contains the segment
     *                    defining the new line we use to create the new hyperplane
     */
    public void treeConstruction(BSPNode currentNode){

        /* If currentNode is null, we can't do anything on it, its the right or left son of a leaf,
        we are done with the construction for this node. */
        if(currentNode != null) {

            //If lines are equals, seg is contained in the splitting line of the node
            List<Segment> toRemove = new LinkedList<>();

            //For each segment contained in the hyperplane we check if its contained in the cutting line
            for (Segment seg : currentNode.getSegmentsInHyperplane()) {

                //If it's the case, we add the segment to the currentNode and to the list to remove from the hyperplane
                if ((Math.abs(seg.getSide(currentNode.getLine(), seg.getX1(), seg.getY1()))<Heuristic.EPSILON)
                        && (Math.abs(seg.getSide(currentNode.getLine(), seg.getX2(), seg.getY2()))<Heuristic.EPSILON)){
                    currentNode.addSegment(seg);
                    toRemove.add(seg);
                }
            }

            //Removing of the list of segments created above of the hyperplane
            currentNode.getSegmentsInHyperplane().removeAll(toRemove);

            //Creation of the lists of segments of the two new hyperplanes
            List<Segment> leftNodeSegments = new LinkedList<>();
            List<Segment> rightNodeSegments = new LinkedList<>();

            //We retrieve the cutting line and segment for further manipulations
            double hyperplaneLine[] = currentNode.getLine();
            Segment segInHyperplaneLine = currentNode.getSegmentsInLine().get(0);

            //For each segment contained in the hyperplane we check its position in comparison with the new cutting line
            for (Segment seg : currentNode.getSegmentsInHyperplane()) {

                //This array of double contains the relative position of the segment to the cutting line
                double[] intersection = seg.computePosition(hyperplaneLine, segInHyperplaneLine);

                //If void intersection, put segment in right or left list of segments
                if (Double.isInfinite(intersection[0])) {
                    sideIntersection(seg, hyperplaneLine);
                    rightNodeSegments.add(seg);
                }

                else if (Double.isNaN(intersection[0])) {
                    sideIntersection(seg, hyperplaneLine);
                    leftNodeSegments.add(seg);
                }

                //If intersection, split the segment into two new segments
                else {

                    //Recovery of the color of the original segment
                    Color color = seg.getColor();
                    Segment firstSegment = new Segment(intersection[0], intersection[1], seg.getX1(), seg.getY1(), color);
                    Segment secondSegment = new Segment(intersection[0], intersection[1], seg.getX2(), seg.getY2(), color);

                    //If the first segment created has already been split, then it's a free split
                    if (seg.getIsIntersected() && (Math.abs((seg.getIntersection()[0] - seg.getX1())) < Heuristic.EPSILON )
                            && Math.abs((seg.getIntersection()[1] - seg.getY1())) < Heuristic.EPSILON){
                        firstSegment.setFreeSplit();
                    }

                    //Otherwise we mark it as intersected
                    else {
                        firstSegment.setIntersected(true);
                        firstSegment.setIntersection(intersection);
                    }

                    //Same procedure with the second segment
                    if (seg.getIsIntersected() && (Math.abs((seg.getIntersection()[0] - seg.getX2())) < Heuristic.EPSILON )
                            && (Math.abs(seg.getIntersection()[1] - seg.getY2()) < Heuristic.EPSILON)) {
                        secondSegment.setFreeSplit();
                    }
                    else{
                        secondSegment.setIntersected(true);
                        secondSegment.setIntersection(intersection);
                    }

                    //Computing of the intersection between the first segment and the line to know theirs relatives positions
                    double firstIntersection[] = firstSegment.computePosition(hyperplaneLine, segInHyperplaneLine);

                    //If the first segment is on the right, then obviously the second segment is on the left
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
            boolean freeSplitLeftOk = false; //Boolean used to know if free-split was successful or not for left son
            boolean freeSplitRightOk = false; //Same for right son

            //Useless method if only one segment in the list
            if (leftNodeSegments.size() > 1){

                //We look for a segment which is a free-split
                for (Segment seg : leftNodeSegments) {
                    if (seg.isFreeSplit()) {
                        //We warn that we found a free-split
                        freeSplitLeftOk = true;
                        //Removing of the segment of the list
                        leftNodeSegments.remove(seg);
                        //Creating and adding of the left son
                        leftNode = new BSPNode(null, null, leftNodeSegments, seg);
                        currentNode.setLeftSon(leftNode);
                        //We are done with the search, we can stop
                        break;
                    }
                }
            }

            //Same procedure for the right side
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
            //If there are segments for left and right side of the line
            if(!leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {

                //If there is no free-split found for right part we take the first segment of the list
                if (!freeSplitRightOk) {
                    Segment rightNodeLineSegment = rightNodeSegments.get(0);
                    rightNodeSegments.remove(0);
                    rightNode = new BSPNode(null, null, rightNodeSegments, rightNodeLineSegment);
                    currentNode.setRightSon(rightNode);
                }

                //Same procedure for left part
                if (!freeSplitLeftOk) {
                    Segment leftNodeLineSegment = leftNodeSegments.get(0);
                    leftNodeSegments.remove(0);
                    leftNode = new BSPNode(null, null, leftNodeSegments, leftNodeLineSegment);
                    currentNode.setLeftSon(leftNode);
                }
            }

            //If there are only segments in right side of the hyperplane and no free-split found
            else if (!freeSplitRightOk && leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {
                Segment rightNodeLineSegment = rightNodeSegments.get(0);
                rightNodeSegments.remove(0);
                rightNode = new BSPNode(null,null,rightNodeSegments,rightNodeLineSegment);
                currentNode.setRightSon(rightNode);
            }

            //If there are only segments in left side of the hyperplane and no free-split found
            else if (!freeSplitLeftOk && !leftNodeSegments.isEmpty() && rightNodeSegments.isEmpty()) {
                Segment leftNodeLineSegment = leftNodeSegments.get(0);
                leftNodeSegments.remove(0);
                leftNode = new BSPNode(null,null,leftNodeSegments,leftNodeLineSegment);
                currentNode.setLeftSon(leftNode);
            }

            //Recursive call
            treeConstruction(currentNode.getLeftSon());
            treeConstruction(currentNode.getRightSon());
        }
    }

    /**
     * Check the special case of the intersection between the cutting line and the extremities of a segment. In
     * fact the method computePosition doesn't handle the case where there is an intersection at one of the extremity
     * of the segment. This method modifies the attributes isIntersected and isFreeSplit of the segment if necessary.
     * @param seg the segment we compute the relative position
     * @param line the line we compute the intersection with the segment
     */
    public void sideIntersection(Segment seg, double [] line) {

        //If the extremity (X2,Y2) of the segment is on the line and not (X1,Y1)
        if (((Math.abs(seg.getSide(line, seg.getX2(), seg.getY2())) < Heuristic.EPSILON)
                && !(Math.abs(seg.getSide(line, seg.getX1(), seg.getY1())) < Heuristic.EPSILON))) {

            //If already intersected, then free-split
            if (seg.getIsIntersected()) {
                seg.setFreeSplit();
            }

            //Otherwise, we save the intersection
            else {
                seg.setIntersected(true);
                double intersection [] = {seg.getX2(),seg.getY2()};
                seg.setIntersection(intersection);
            }
        }

        //If the extremity (X1,Y1) of the segment is on the line and not (X2,Y2)
        else if (!(Math.abs(seg.getSide(line, seg.getX2(), seg.getY2())) < Heuristic.EPSILON)
                && (Math.abs(seg.getSide(line, seg.getX1(), seg.getY1()))) < Heuristic.EPSILON) {

            //Same procedure
            if (seg.getIsIntersected()) {
                seg.setFreeSplit();
            }
            else {
                seg.setIntersected(true);
                double intersection [] = {seg.getX1(),seg.getY1()};
                seg.setIntersection(intersection);
            }
        }
    }

    @Override public String toString() {
        return "Free splits";
    }
}