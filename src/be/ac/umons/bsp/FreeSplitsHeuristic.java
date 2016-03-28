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
        //BSPNode.printNode(root);
        return root;
    }

    /**
     * @param currentNode Current root in the construction of the tree which contains the segment
     *                    defining the new line we use to create the new hyperplane
     */
    public void treeConstruction(BSPNode currentNode){
        //We have at least a segment which determines two new hyperplanes
        if(currentNode.getSegmentsInLine() != null) {
            //If lines are equals, seg is contained in the splitting line of the node
            List<Segment> toRemove = new LinkedList<>();
            for (Segment seg : currentNode.getSegmentsInHyperplane()) {
                if ((Math.abs(seg.getSide(currentNode.getLine(), seg.getX1(), seg.getY1()))<Heuristic.EPSILON)
                        && (Math.abs(seg.getSide(currentNode.getLine(), seg.getX2(), seg.getY2()))<Heuristic.EPSILON)){
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
                    sideIntersection(seg, hyperplaneLine);
                    rightNodeSegments.add(seg);
                } else if (Double.isNaN(intersection[0])) {
                    sideIntersection(seg, hyperplaneLine);
                    leftNodeSegments.add(seg);
                }
                //If intersection, split the segment into two new segments
                //TODO : peut être erreur lors de l'ajout des deux nouveaux segments, car gardent leur intersection ?
                else {
                    Color color = seg.getColor();
                    Segment firstSegment = new Segment(intersection[0], intersection[1], seg.getX1(), seg.getY1(), color);
                    Segment secondSegment = new Segment(intersection[0], intersection[1], seg.getX2(), seg.getY2(), color);
                    if (seg.getIsIntersected()){
                        firstSegment.incrementCutCount();
                    }
                    firstSegment.setIntersected(true);
                    firstSegment.incrementCutCount();
                    //No condition about a new intersection for the second segment because he is really new
                    secondSegment.setIntersected(true);
                    secondSegment.incrementCutCount();

                    double firstIntersection[] = firstSegment.computePosition(hyperplaneLine, segInHyperplaneLine);
                    if (Double.isInfinite(firstIntersection[0])) {
                        //TODO Maybe need epsilon
                        /*
                        System.out.println("Seg intersected ? : " + seg.getIsIntersected1());
                        System.out.println("X1 de seg : " + "(" + seg.getX1() +" ; " + seg.getY1() + ")");
                        System.out.println("X2 de seg : " + "(" + seg.getX2() +" ; " + seg.getY2() + ")");
                        System.out.println("X2 de firstSegment : " + "(" + firstSegment.getX2() +" ; " + firstSegment.getY2() + ")");
                        System.out.println("X2 de secondSegment : " + "(" + secondSegment.getX2() +" ; " + secondSegment.getY2() + ")");
                        */
                        rightNodeSegments.add(firstSegment);
                        leftNodeSegments.add(secondSegment);

                    } else {
                        /*
                        System.out.println("Seg intersected ? : " + seg.getIsIntersected1());
                        System.out.println("X1 de seg : " + "(" + seg.getX1() +" ; " + seg.getY1() + ")");
                        System.out.println("X2 de seg : " + "(" + seg.getX2() +" ; " + seg.getY2() + ")");
                        System.out.println("X2 de firstSegment : " + "(" + firstSegment.getX2() +" ; " + firstSegment.getY2() + ")");
                        System.out.println("X2 de secondSegment : " + "(" + secondSegment.getX2() +" ; " + secondSegment.getY2() + ")");
                        */
                        rightNodeSegments.add(secondSegment);
                        leftNodeSegments.add(firstSegment);
                    }
                    /*
                    System.out.println("#######################################################");
                    System.out.println("Coordonnées : " +
                            "(" + firstSegment.getX1() +" ; " + firstSegment.getY1() + ") , ("
                            + firstSegment.getX2() + " ; " + firstSegment.getY2() + ").");
                    System.out.println("First seg cut count : " + firstSegment.getCutCount());
                    System.out.println("Sec seg cut count : " + secondSegment.getCutCount());
                    System.out.println("Coordonnées : " +
                            "(" + secondSegment.getX1() +" ; " + secondSegment.getY1() + ") , ("
                            + secondSegment.getX2() + " ; " + secondSegment.getY2() + ").");
                    System.out.println("#######################################################");
                    */
                }
                //System.out.println("-----------------");
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
                        /*
                        System.out.println("Free split 1 worked with : " + leftNode + "\n");
                        System.out.println("Coordonnées : " +
                                "(" + leftNode.getSegmentsInLine().get(0).getX1() +" ; "
                                + leftNode.getSegmentsInLine().get(0).getY1() + ") , ("
                                + leftNode.getSegmentsInLine().get(0).getX2() + " ; "
                                + leftNode.getSegmentsInLine().get(0).getY2() + ").");
                        */
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
                        /*
                        System.out.println("Free split 2 worked with : " + rightNode + "\n");
                        System.out.println("Coordonnées : " +
                                "(" + rightNode.getSegmentsInLine().get(0).getX1() +" ; "
                                + rightNode.getSegmentsInLine().get(0).getY1() + ") , ("
                                + rightNode.getSegmentsInLine().get(0).getX2() + " ; "
                                + rightNode.getSegmentsInLine().get(0).getY2() + ").");
                        */
                        break;
                    }
                }
            }

            //Random choice of the new splitting segment if free splits strategy failed
            Segment splittingSegment;
            //Left part
            if (!freeSplitLeftOk){
                if (!leftNodeSegments.isEmpty()){
                    splittingSegment = leftNodeSegments.get(0);
                    leftNodeSegments.remove(0);
                    leftNode = new BSPNode(null, null, leftNodeSegments, splittingSegment);
                    currentNode.setLeftSon(leftNode);
                }
            }
            //Right part
            if (!freeSplitRightOk){
                if (!rightNodeSegments.isEmpty()){
                    splittingSegment = rightNodeSegments.get(0);
                    rightNodeSegments.remove(0);
                    rightNode = new BSPNode(null, null, rightNodeSegments, splittingSegment);
                    currentNode.setRightSon(rightNode);
                }
            }
            //Maybe there is no left or right son, then the work is done
            if (currentNode.getLeftSon() != null)
                treeConstruction(currentNode.getLeftSon());
            if (currentNode.getRightSon() != null)
                treeConstruction(currentNode.getRightSon());
        }
    }

    public boolean sideIntersection(Segment seg, double [] line) {
        if (((Math.abs(seg.getSide(line, seg.getX2(), seg.getY2())) < Heuristic.EPSILON) && !(Math.abs(seg.getSide(line, seg.getX1(), seg.getY1())) < Heuristic.EPSILON)) ||
                (!(Math.abs(seg.getSide(line, seg.getX2(), seg.getY2())) < Heuristic.EPSILON) && (Math.abs(seg.getSide(line, seg.getX1(), seg.getY1()))) < Heuristic.EPSILON)) {
            if (seg.getIsIntersected()) {
                seg.incrementCutCount();
            } else {
                seg.incrementCutCount();
                seg.setIntersected(true);
            }
            return true;
        }
        return false;
    }

    @Override public String toString() {
        return "Free splits";
    }

    public static void main (String [] args){
        SegmentLoader loader = new SegmentLoader();
        List<Segment>myList = loader.loadAsList("assets/other/free_splits.txt");
        FreeSplitsHeuristic test = new FreeSplitsHeuristic();
        test.createTree(myList);
    }
}