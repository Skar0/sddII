package be.ac.umons.bsp;

import org.w3c.dom.ls.LSInput;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by clement on 2/22/16.
 */
public class InOrderHeuristic implements Heuristic {

    /**
     * Creates a BSP tree using the in-order heuristic
     * @param segmentList List of segments to make a BSP tree out of
     * @return The root of the created BSP tree
     */
    public BSPNode createTree(List<Segment> segmentList) {
        Segment firstLineInList = segmentList.get(0);
        segmentList.remove(0);
        BSPNode root = new BSPNode(null, null, segmentList,firstLineInList);
        createRoot(root);
        //TODO remove
        return root;
    }

    public void createRoot(BSPNode node) {


        if(node !=  null) {
            //System.out.println("Segment actuel : "+node+" "+node.getSegmentsInLine().get(0).getX1()+" "+node.getSegmentsInLine().get(0).getY1());
            List<Segment> toRemove = new LinkedList<>();
            for (Segment seg : node.getSegmentsInHyperplane()) {
                //if lines are equals, seg is contained in the cutting line of the node
                if( (Math.abs(seg.getSide(node.getLine(), seg.getX1(), seg.getY1()))<Heuristic.EPSILON) && (Math.abs(seg.getSide(node.getLine(), seg.getX2(), seg.getY2()))<Heuristic.EPSILON) ) {
                   // System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" dans la ligne du segment actuel");
                    node.addSegment(seg);
                    toRemove.add(seg);

                }
            }
            node.getSegmentsInHyperplane().removeAll(toRemove);
            List<Segment> leftNodeSegments = new LinkedList<>();
            List<Segment> rightNodeSegments = new LinkedList<>();
            //

            List<Segment> newSegments = new LinkedList<>();
            //
            for (Segment seg : node.getSegmentsInHyperplane()) {
                double[] intersection = seg.computePosition(node.getLine(), node.getSegmentsInLine().get(0));
                //System.out.println("-----------"+intersection[0]);
                if(Double.isInfinite(intersection[0])) {
                    rightNodeSegments.add(seg);
                   // System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" a droite du segment actuel");
                }
                //old line : else if(intersection[0] == Double.POSITIVE_INFINITY) {
                //can return + or - inf, only a general inf so i cant differenciate
                else if(Double.isNaN(intersection[0])) {
                   leftNodeSegments.add(seg);
                    //System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" a gauche du segment actuel");
                }
                else {
                   // System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" est coupé");
                    Segment leftSegment = new Segment(intersection[0], intersection[1],seg.getX1(), seg.getY1(), seg.getColor());
                    Segment rightSegment = new Segment(intersection[0], intersection[1],seg.getX2(), seg.getY2(), seg.getColor());
                    newSegments.add(leftSegment);
                    newSegments.add(rightSegment);
                }
            }
            for (Segment seg : newSegments) {

                double[] intersection = seg.computePosition(node.getLine(), node.getSegmentsInLine().get(0));
                if (Double.isInfinite(intersection[0])) {
                  //  System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" a droite");
                    rightNodeSegments.add(seg);

                } else if (Double.isNaN(intersection[0])) {
                   // System.out.println("Segment "+seg+" "+seg.getX1()+" "+seg.getY1()+" a gauche");
                    leftNodeSegments.add(seg);
                }
            }
            if(!leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {
                Segment rightNodeLineSegment = rightNodeSegments.get(0);
                rightNodeSegments.remove(0);
                BSPNode rightNode = new BSPNode(null,null,rightNodeSegments,rightNodeLineSegment);
                Segment leftNodeLineSegment = leftNodeSegments.get(0);
                leftNodeSegments.remove(0);
                BSPNode leftNode = new BSPNode(null,null,leftNodeSegments,leftNodeLineSegment);

                node.setLeftSon(leftNode);
                node.setRightSon(rightNode);
            }

            else if (leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {
                Segment rightNodeLineSegment = rightNodeSegments.get(0);
                rightNodeSegments.remove(0);
                BSPNode rightNode = new BSPNode(null,null,rightNodeSegments,rightNodeLineSegment);

                node.setRightSon(rightNode);
            }

            else if (!leftNodeSegments.isEmpty() && rightNodeSegments.isEmpty()) {
                Segment leftNodeLineSegment = leftNodeSegments.get(0);
                leftNodeSegments.remove(0);
                BSPNode leftNode = new BSPNode(null,null,leftNodeSegments,leftNodeLineSegment);

                node.setLeftSon(leftNode);
            }
            createRoot(node.getLeftSon());
            createRoot(node.getRightSon());
        }
    }
    @Override public String toString() {
        return "In order";
    }
}
