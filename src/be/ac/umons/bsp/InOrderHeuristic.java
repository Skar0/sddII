package be.ac.umons.bsp;

import org.w3c.dom.ls.LSInput;

import java.util.LinkedList;
import java.util.List;

/**
 * This heuristic reads all segments in the list in order and builds the tree by using the first segment it reads to build the cutting line.
 * @author Clément Tamines
 */
public class InOrderHeuristic implements Heuristic {

    /**
     * Creates a BSP tree using the in-order heuristic
     * @param segmentList List of segments to make a BSP tree out of
     * @return The BSPNode that is the root of the created tree.
     */
    public BSPNode createTree(List<Segment> segmentList) {
        //First segment in the list is used to build the cutting line, so we remove it from the list.
        Segment firstLineInList = segmentList.get(0);
        segmentList.remove(0);

        //We create the root of the tree by giving it the list of segments and the first segment used to build the cutting line.
        BSPNode root = new BSPNode(null, null, segmentList,firstLineInList);
        createRoot(root);
        return root;
    }

    /**
     * Recursive method to build a BSP tree from an incomplete BSPNode only containing a list of segment and a cutting line.
     * The method removes all segments contained in the cutting line and then creates two lists of segments that are on the left and right of the line.
     * These lists are then used to create an incomplete BSPNode and the method is called recursively until the tree is fully built.
     * @param node The incomplete BSPNode that only has a list of segments and a cutting line.
     */
    public void createRoot(BSPNode node) {

        /*  Because the method is recursive, a node that doesn't have a left or right son or a leaf will call this method on its left and right son resulting
            in a call of this method with a null node parameter. Here we just make sure to do nothing is that is the case.
         */
        if(node !=  null) {

            //List of segments to remove (ie segments that are contained in the cutting line)
            List<Segment> toRemove = new LinkedList<>();

            //We go trough the list of segments to check if it is contained in the cutting line, allowing precision errors on the equality with the EPSILON value
            for (Segment seg : node.getSegmentsInHyperplane()) {

                //If the getSide method returns a value close enough to 0, it means the point is on the line. If both points are on the line, the segment is contained in it.
                if( (Math.abs(seg.getSide(node.getLine(), seg.getX1(), seg.getY1()))<Heuristic.EPSILON) && (Math.abs(seg.getSide(node.getLine(), seg.getX2(), seg.getY2()))<Heuristic.EPSILON) ) {
                    node.addSegment(seg);
                    toRemove.add(seg);
                }
            }

            //After making sure we added the segments contained in the cutting line to the corresponding list in the node, we delete those segments from the list of segments to process further down
            node.getSegmentsInHyperplane().removeAll(toRemove);

            //We created the lists that will contain segments on the left and right of the cutting line.
            List<Segment> leftNodeSegments = new LinkedList<>();
            List<Segment> rightNodeSegments = new LinkedList<>();

            //This list of segments will contain both parts of segments that are cut in two parts by the cutting line.
            List<Segment> newSegments = new LinkedList<>();

            //For each segments left in the hyperplane, we determine their position
            for (Segment seg : node.getSegmentsInHyperplane()) {

                //This 2-elements array contains the position of the segments in relation of the cutting line
                double[] intersection = seg.computePosition(node.getLine(), node.getSegmentsInLine().get(0));

                if(Double.isInfinite(intersection[0])) {
                    rightNodeSegments.add(seg);
                }

                else if(Double.isNaN(intersection[0])) {
                   leftNodeSegments.add(seg);
                }

                //Segment is cut in two, we create two segments with it and the intersection point
                else {
                    Segment leftSegment = new Segment(intersection[0], intersection[1],seg.getX1(), seg.getY1(), seg.getColor());
                    Segment rightSegment = new Segment(intersection[0], intersection[1],seg.getX2(), seg.getY2(), seg.getColor());
                    newSegments.add(leftSegment);
                    newSegments.add(rightSegment);
                }
            }

            //We need to know if the cut segments are on the left or right of the cutting line
            for (Segment seg : newSegments) {

                double[] intersection = seg.computePosition(node.getLine(), node.getSegmentsInLine().get(0));

                if (Double.isInfinite(intersection[0])) {
                    rightNodeSegments.add(seg);
                }

                else if (Double.isNaN(intersection[0])) {
                    leftNodeSegments.add(seg);
                }
            }

            //If there were segments to the left and to the right we create a left and right son
            if(!leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {

                //Same process as in the createTree method to initialize a BSPNode

                //We get the first segment which will be used to build the cutting line and we delete it from the list
                Segment rightNodeLineSegment = rightNodeSegments.get(0);
                rightNodeSegments.remove(0);

                //We create a BSPNode with no left and right son
                BSPNode rightNode = new BSPNode(null,null,rightNodeSegments,rightNodeLineSegment);

                Segment leftNodeLineSegment = leftNodeSegments.get(0);
                leftNodeSegments.remove(0);

                BSPNode leftNode = new BSPNode(null,null,leftNodeSegments,leftNodeLineSegment);

                //We can now give the current node a left and right son
                node.setLeftSon(leftNode);
                node.setRightSon(rightNode);
            }

            //If we only have segments to the right, we only make a right son
            else if (leftNodeSegments.isEmpty() && !rightNodeSegments.isEmpty()) {

                Segment rightNodeLineSegment = rightNodeSegments.get(0);
                rightNodeSegments.remove(0);

                BSPNode rightNode = new BSPNode(null,null,rightNodeSegments,rightNodeLineSegment);

                node.setRightSon(rightNode);
            }

            //If we only have segments to the left, we only make a left son
            else if (!leftNodeSegments.isEmpty() && rightNodeSegments.isEmpty()) {

                Segment leftNodeLineSegment = leftNodeSegments.get(0);
                leftNodeSegments.remove(0);

                BSPNode leftNode = new BSPNode(null,null,leftNodeSegments,leftNodeLineSegment);

                node.setLeftSon(leftNode);
            }

            //Recursive call on the left and right son
            createRoot(node.getLeftSon());
            createRoot(node.getRightSon());
        }
    }

    //toString is overriden to be used in the gui comboBoxes
    @Override public String toString() {
        return "In order";
    }
}
